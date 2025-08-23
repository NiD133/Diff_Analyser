package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

import static org.jsoup.parser.Parser.NamespaceHtml;
import static org.junit.jupiter.api.Assertions.*;

public class TagSetTest {

    @Test
    void testRetrieveNewTagsCaseSensitive() {
        // Parse the HTML with case-sensitive settings
        Document doc = Jsoup.parse("<div><p>One</p></div>", "", Parser.htmlParser().settings(ParseSettings.preserveCase));
        TagSet tagSet = doc.parser().tagSet();

        // Verify that 'meta' is a known tag
        Tag metaTag = tagSet.get("meta", NamespaceHtml);
        assertNotNull(metaTag);
        assertTrue(metaTag.isKnownTag());

        // Verify that 'p' is a known tag
        Element paragraph = doc.expectFirst("p");
        assertTrue(paragraph.tag().isKnownTag());

        // Verify that 'FOO' is not a known tag initially
        assertNull(tagSet.get("FOO", NamespaceHtml));

        // Change the tag name to 'FOO' and verify properties
        paragraph.tagName("FOO");
        Tag fooTag = paragraph.tag();
        assertEquals("FOO", fooTag.name());
        assertEquals("foo", fooTag.normalName());
        assertEquals(NamespaceHtml, fooTag.namespace());
        assertFalse(fooTag.isKnownTag());

        // Verify that 'FOO' is now recognized in the tag set
        assertSame(fooTag, tagSet.get("FOO", NamespaceHtml));
        assertSame(fooTag, tagSet.valueOf("FOO", NamespaceHtml));
        assertNull(tagSet.get("FOO", "SomeOtherNamespace"));
    }

    @Test
    void testRetrieveNewTagsCaseInsensitive() {
        // Parse the HTML with default (case-insensitive) settings
        Document doc = Jsoup.parse("<div><p>One</p></div>");
        TagSet tagSet = doc.parser().tagSet();

        // Verify that 'meta' is a known tag
        Tag metaTag = tagSet.get("meta", NamespaceHtml);
        assertNotNull(metaTag);
        assertTrue(metaTag.isKnownTag());

        // Verify that 'p' is a known tag
        Element paragraph = doc.expectFirst("p");
        assertTrue(paragraph.tag().isKnownTag());

        // Verify that 'FOO' is not a known tag initially
        assertNull(tagSet.get("FOO", NamespaceHtml));

        // Change the tag name to 'FOO' and verify properties
        paragraph.tagName("FOO");
        Tag fooTag = paragraph.tag();
        assertEquals("foo", fooTag.name());
        assertEquals("foo", fooTag.normalName());
        assertEquals(NamespaceHtml, fooTag.namespace());
        assertFalse(fooTag.isKnownTag());

        // Verify that 'foo' is now recognized in the tag set
        assertSame(fooTag, tagSet.get("foo", NamespaceHtml));
        assertSame(fooTag, tagSet.valueOf("FOO", NamespaceHtml, doc.parser().settings()));
        assertNull(tagSet.get("foo", "SomeOtherNamespace"));
    }

    @Test
    void testSupplyCustomTagSet() {
        // Create a custom tag set and configure a custom tag
        TagSet customTagSet = TagSet.Html();
        customTagSet.valueOf("custom", NamespaceHtml).set(Tag.PreserveWhitespace).set(Tag.Block);
        Parser customParser = Parser.htmlParser().tagSet(customTagSet);

        // Parse the document with the custom parser
        Document doc = Jsoup.parse("<body><custom>\n\nFoo\n Bar</custom></body>", customParser);
        Element customElement = doc.expectFirst("custom");

        // Verify custom tag properties
        assertTrue(customElement.tag().preserveWhitespace());
        assertTrue(customElement.tag().isBlock());
        assertEquals("<custom>\n\nFoo\n Bar</custom>", customElement.outerHtml());
    }

    @Test
    void testKnownTags() {
        // Test known and unknown tags in a tag set
        TagSet tagSet = TagSet.Html();
        Tag customTag = new Tag("custom");

        // Verify initial properties of custom tag
        assertEquals("custom", customTag.name());
        assertEquals(NamespaceHtml, customTag.namespace());
        assertFalse(customTag.isKnownTag());

        // Verify known 'br' tag
        Tag brTag = tagSet.get("br", NamespaceHtml);
        assertNotNull(brTag);
        assertTrue(brTag.isKnownTag());
        assertSame(brTag, tagSet.valueOf("br", NamespaceHtml));

        // Verify unknown 'foo' tag
        Tag fooTag = tagSet.valueOf("foo", NamespaceHtml);
        assertFalse(fooTag.isKnownTag());

        // Add custom tag and verify it becomes known
        tagSet.add(customTag);
        assertTrue(customTag.isKnownTag());
        assertSame(customTag, tagSet.get("custom", NamespaceHtml));
        assertSame(customTag, tagSet.valueOf("custom", NamespaceHtml));

        // Verify case-insensitive known tag behavior
        Tag capCustomTag = tagSet.valueOf("Custom", NamespaceHtml);
        assertTrue(capCustomTag.isKnownTag());

        // Test setting and clearing known tag status
        Tag barTag = new Tag("bar");
        assertFalse(barTag.isKnownTag());
        barTag.set(Tag.Block);
        assertTrue(barTag.isKnownTag());
        barTag.clear(Tag.Block);
        assertTrue(barTag.isKnownTag());
        barTag.clear(Tag.Known);
        assertFalse(barTag.isKnownTag());
    }

    @Test
    void testCustomizeAllTags() {
        // Customize all tags to be self-closing
        TagSet tagSet = TagSet.Html();
        tagSet.onNewTag(tag -> tag.set(Tag.SelfClose));

        // Verify customization
        assertTrue(tagSet.get("script", NamespaceHtml).is(Tag.SelfClose));
        assertTrue(tagSet.valueOf("SCRIPT", NamespaceHtml).is(Tag.SelfClose));
        assertTrue(tagSet.valueOf("custom", NamespaceHtml).is(Tag.SelfClose));

        // Add a new tag and verify it is self-closing
        Tag fooTag = new Tag("foo", NamespaceHtml);
        assertFalse(fooTag.is(Tag.SelfClose));
        tagSet.add(fooTag);
        assertTrue(fooTag.is(Tag.SelfClose));
    }

    @Test
    void testCustomizeSomeTags() {
        // Customize only unknown tags to be self-closing
        TagSet tagSet = TagSet.Html();
        tagSet.onNewTag(tag -> {
            if (!tag.isKnownTag()) {
                tag.set(Tag.SelfClose);
            }
        });

        // Verify customization
        assertFalse(tagSet.valueOf("script", NamespaceHtml).is(Tag.SelfClose));
        assertFalse(tagSet.valueOf("SCRIPT", NamespaceHtml).is(Tag.SelfClose));
        assertTrue(tagSet.valueOf("custom-tag", NamespaceHtml).is(Tag.SelfClose));
    }

    @Test
    void testParseWithCustomization() {
        // Customize 'script' tag to be self-closing
        Parser parser = Parser.htmlParser();
        parser.tagSet().onNewTag(tag -> {
            if (tag.normalName().equals("script")) {
                tag.set(Tag.SelfClose);
            }
        });

        // Parse document and verify HTML output
        Document doc = Jsoup.parse("<script />Text", parser);
        assertEquals("<html>\n <head>\n  <script></script>\n </head>\n <body>Text</body>\n</html>", doc.html());
    }

    @Test
    void testParseWithGeneralCustomization() {
        // Customize all unknown tags to be self-closing
        Parser parser = Parser.htmlParser();
        parser.tagSet().onNewTag(tag -> {
            if (!tag.isKnownTag()) {
                tag.set(Tag.SelfClose);
            }
        });

        // Parse document and verify HTML output
        Document doc = Jsoup.parse("<custom-data />Bar <script />Text", parser);
        assertEquals("<custom-data></custom-data>Bar\n<script>Text</script>", doc.body().html());
    }

    @Test
    void testMultipleCustomizers() {
        // Apply multiple customizers to a tag set
        TagSet tagSet = TagSet.Html();
        tagSet.onNewTag(tag -> {
            if (tag.normalName().equals("script")) {
                tag.set(Tag.SelfClose);
            }
        });
        tagSet.onNewTag(tag -> {
            if (!tag.isKnownTag()) {
                tag.set(Tag.RcData);
            }
        });

        // Verify customizations
        assertTrue(tagSet.valueOf("script", NamespaceHtml).is(Tag.SelfClose));
        assertFalse(tagSet.valueOf("script", NamespaceHtml).is(Tag.RcData));
        assertTrue(tagSet.valueOf("custom-tag", NamespaceHtml).is(Tag.RcData));
    }

    @Test
    void testCustomizersPreservedInSource() {
        // Verify customizers are preserved when copying a tag set
        TagSet sourceTagSet = TagSet.Html();
        sourceTagSet.onNewTag(tag -> tag.set(Tag.RcData));
        TagSet copiedTagSet = new TagSet(sourceTagSet);

        // Verify customizations in copied tag set
        assertTrue(copiedTagSet.valueOf("script", NamespaceHtml).is(Tag.RcData));
        assertTrue(sourceTagSet.valueOf("script", NamespaceHtml).is(Tag.RcData));

        // Add new customizer to copied tag set and verify it doesn't affect the source
        copiedTagSet.onNewTag(tag -> tag.set(Tag.Void));
        assertTrue(copiedTagSet.valueOf("custom-tag", NamespaceHtml).is(Tag.Void));
        assertFalse(sourceTagSet.valueOf("custom-tag", NamespaceHtml).is(Tag.Void));
    }
}