package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

import static org.jsoup.parser.Parser.NamespaceHtml;
import static org.junit.jupiter.api.Assertions.*;

public class TagSetTest {

    @Test
    void testRetrieveNewTagsWithCaseSensitivity() {
        // Parse HTML with case sensitivity preserved
        Document document = Jsoup.parse("<div><p>One</p></div>", "", Parser.htmlParser().settings(ParseSettings.preserveCase));
        TagSet tagSet = document.parser().tagSet();

        // Verify that 'meta' is a known tag
        Tag metaTag = tagSet.get("meta", NamespaceHtml);
        assertNotNull(metaTag);
        assertTrue(metaTag.isKnownTag());

        // Verify that 'p' is a known tag
        Element paragraph = document.expectFirst("p");
        assertTrue(paragraph.tag().isKnownTag());

        // Verify behavior with unknown tags
        assertNull(tagSet.get("FOO", NamespaceHtml));
        paragraph.tagName("FOO");
        Tag fooTag = paragraph.tag();
        assertEquals("FOO", fooTag.name());
        assertEquals("foo", fooTag.normalName());
        assertEquals(NamespaceHtml, fooTag.namespace());
        assertFalse(fooTag.isKnownTag());

        // Verify that the new tag is retrievable
        assertSame(fooTag, tagSet.get("FOO", NamespaceHtml));
        assertSame(fooTag, tagSet.valueOf("FOO", NamespaceHtml));
        assertNull(tagSet.get("FOO", "SomeOtherNamespace"));
    }

    @Test
    void testRetrieveNewTagsWithoutCaseSensitivity() {
        // Parse HTML without preserving case sensitivity
        Document document = Jsoup.parse("<div><p>One</p></div>");
        TagSet tagSet = document.parser().tagSet();

        // Verify that 'meta' is a known tag
        Tag metaTag = tagSet.get("meta", NamespaceHtml);
        assertNotNull(metaTag);
        assertTrue(metaTag.isKnownTag());

        // Verify that 'p' is a known tag
        Element paragraph = document.expectFirst("p");
        assertTrue(paragraph.tag().isKnownTag());

        // Verify behavior with unknown tags
        assertNull(tagSet.get("FOO", NamespaceHtml));
        paragraph.tagName("FOO");
        Tag fooTag = paragraph.tag();
        assertEquals("foo", fooTag.name());
        assertEquals("foo", fooTag.normalName());
        assertEquals(NamespaceHtml, fooTag.namespace());
        assertFalse(fooTag.isKnownTag());

        // Verify that the new tag is retrievable
        assertSame(fooTag, tagSet.get("foo", NamespaceHtml));
        assertSame(fooTag, tagSet.valueOf("FOO", NamespaceHtml, document.parser().settings()));
        assertNull(tagSet.get("foo", "SomeOtherNamespace"));
    }

    @Test
    void testCustomTagSet() {
        // Create a custom TagSet and configure a custom tag
        TagSet tagSet = TagSet.Html();
        tagSet.valueOf("custom", NamespaceHtml).set(Tag.PreserveWhitespace).set(Tag.Block);
        Parser parser = Parser.htmlParser().tagSet(tagSet);

        // Parse HTML with the custom tag
        Document document = Jsoup.parse("<body><custom>\n\nFoo\n Bar</custom></body>", parser);
        Element customElement = document.expectFirst("custom");

        // Verify custom tag properties
        assertTrue(customElement.tag().preserveWhitespace());
        assertTrue(customElement.tag().isBlock());
        assertEquals("<custom>\n\nFoo\n Bar</custom>", customElement.outerHtml());
    }

    @Test
    void testKnownTags() {
        // Test known and unknown tags in a TagSet
        TagSet tagSet = TagSet.Html();
        Tag customTag = new Tag("custom");
        assertEquals("custom", customTag.name());
        assertEquals(NamespaceHtml, customTag.namespace());
        assertFalse(customTag.isKnownTag());

        // Verify known tag behavior
        Tag brTag = tagSet.get("br", NamespaceHtml);
        assertNotNull(brTag);
        assertTrue(brTag.isKnownTag());
        assertSame(brTag, tagSet.valueOf("br", NamespaceHtml));

        // Verify unknown tag behavior
        Tag fooTag = tagSet.valueOf("foo", NamespaceHtml);
        assertFalse(fooTag.isKnownTag());

        // Add custom tag and verify it becomes known
        tagSet.add(customTag);
        assertTrue(customTag.isKnownTag());
        assertSame(customTag, tagSet.get("custom", NamespaceHtml));
        assertSame(customTag, tagSet.valueOf("custom", NamespaceHtml));
        Tag capCustomTag = tagSet.valueOf("Custom", NamespaceHtml);
        assertTrue(capCustomTag.isKnownTag());

        // Test known tag status with set and clear operations
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
        // Customize all new tags to be self-closing
        TagSet tagSet = TagSet.Html();
        tagSet.onNewTag(tag -> tag.set(Tag.SelfClose));

        // Verify customization
        assertTrue(tagSet.get("script", NamespaceHtml).is(Tag.SelfClose));
        assertTrue(tagSet.valueOf("SCRIPT", NamespaceHtml).is(Tag.SelfClose));
        assertTrue(tagSet.valueOf("custom", NamespaceHtml).is(Tag.SelfClose));

        // Verify that added tags are also customized
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
        // Customize 'script' tag to be self-closing during parsing
        Parser parser = Parser.htmlParser();
        parser.tagSet().onNewTag(tag -> {
            if (tag.normalName().equals("script")) {
                tag.set(Tag.SelfClose);
            }
        });

        // Parse HTML and verify result
        Document document = Jsoup.parse("<script />Text", parser);
        assertEquals("<html>\n <head>\n  <script></script>\n </head>\n <body>Text</body>\n</html>", document.html());
    }

    @Test
    void testParseWithGeneralCustomization() {
        // Customize all unknown tags to be self-closing during parsing
        Parser parser = Parser.htmlParser();
        parser.tagSet().onNewTag(tag -> {
            if (!tag.isKnownTag()) {
                tag.set(Tag.SelfClose);
            }
        });

        // Parse HTML and verify result
        Document document = Jsoup.parse("<custom-data />Bar <script />Text", parser);
        assertEquals("<custom-data></custom-data>Bar\n<script>Text</script>", document.body().html());
    }

    @Test
    void testMultipleCustomizers() {
        // Apply multiple customizers to the TagSet
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
        // Verify that customizers are preserved when copying a TagSet
        TagSet sourceTagSet = TagSet.Html();
        sourceTagSet.onNewTag(tag -> tag.set(Tag.RcData));
        TagSet copiedTagSet = new TagSet(sourceTagSet);

        // Verify customizations in both source and copy
        assertTrue(copiedTagSet.valueOf("script", NamespaceHtml).is(Tag.RcData));
        assertTrue(sourceTagSet.valueOf("script", NamespaceHtml).is(Tag.RcData));

        // Verify that additional customizations in the copy do not affect the source
        copiedTagSet.onNewTag(tag -> tag.set(Tag.Void));
        assertTrue(copiedTagSet.valueOf("custom-tag", NamespaceHtml).is(Tag.Void));
        assertFalse(sourceTagSet.valueOf("custom-tag", NamespaceHtml).is(Tag.Void));
    }
}