package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

import static org.jsoup.parser.Parser.NamespaceHtml;
import static org.junit.jupiter.api.Assertions.*;

public class TagSetTest {

    @Test
    void newTagCreatedWithCaseSensitivity() {
        // Setup: Parse with case preservation
        Document doc = Jsoup.parse("<div><p>One</p></div>", "", Parser.htmlParser().settings(ParseSettings.preserveCase));
        TagSet tags = doc.parser().tagSet();

        // Verify initial known tags
        Tag meta = tags.get("meta", NamespaceHtml);
        assertNotNull(meta, "Standard tag should exist");
        assertTrue(meta.isKnownTag(), "Standard tag should be known");

        Element p = doc.expectFirst("p");
        assertTrue(p.tag().isKnownTag(), "Existing tag should be known");

        // Verify non-existent tag
        assertNull(tags.get("FOO", NamespaceHtml), "Custom tag should not exist initially");

        // Create new tag by renaming element
        p.tagName("FOO");
        Tag foo = p.tag();

        // Validate new tag properties
        assertEquals("FOO", foo.name(), "Should preserve case in name");
        assertEquals("foo", foo.normalName(), "Normalized name should be lowercase");
        assertEquals(NamespaceHtml, foo.namespace(), "Should use HTML namespace");
        assertFalse(foo.isKnownTag(), "New tag should not be known initially");

        // Verify tag set behavior after creation
        assertSame(foo, tags.get("FOO", NamespaceHtml), "Should retrieve new tag with exact case");
        assertSame(foo, tags.valueOf("FOO", NamespaceHtml), "Should find new tag via valueOf");
        assertNull(tags.get("FOO", "SomeOtherNamespace"), "Should not find tag in different namespace");
    }

    @Test
    void newTagCreatedWithCaseInsensitivity() {
        Document doc = Jsoup.parse("<div><p>One</p></div>");
        TagSet tags = doc.parser().tagSet();

        // Verify initial known tags
        Tag meta = tags.get("meta", NamespaceHtml);
        assertNotNull(meta, "Standard tag should exist");
        assertTrue(meta.isKnownTag(), "Standard tag should be known");

        Element p = doc.expectFirst("p");
        assertTrue(p.tag().isKnownTag(), "Existing tag should be known");

        // Verify non-existent tag
        assertNull(tags.get("FOO", NamespaceHtml), "Custom tag should not exist initially");

        // Create new tag by renaming element
        p.tagName("FOO");
        Tag foo = p.tag();

        // Validate new tag properties (case normalization)
        assertEquals("foo", foo.name(), "Should normalize name to lowercase");
        assertEquals("foo", foo.normalName(), "Normalized name should match");
        assertEquals(NamespaceHtml, foo.namespace(), "Should use HTML namespace");
        assertFalse(foo.isKnownTag(), "New tag should not be known initially");

        // Verify tag set behavior after creation
        assertSame(foo, tags.get("foo", NamespaceHtml), "Should retrieve by normalized name");
        assertSame(foo, tags.valueOf("FOO", NamespaceHtml, doc.parser().settings()), "Should find via case-insensitive lookup");
        assertNull(tags.get("foo", "SomeOtherNamespace"), "Should not find in different namespace");
    }

    @Test
    void customTagSetWithModifiedTagProperties() {
        // Create custom tag set with special properties
        TagSet tags = TagSet.Html();
        tags.valueOf("custom", NamespaceHtml).set(Tag.PreserveWhitespace).set(Tag.Block);
        Parser parser = Parser.htmlParser().tagSet(tags);

        // Parse document with custom tag
        Document doc = Jsoup.parse("<body><custom>\n\nFoo\n Bar</custom></body>", parser);
        Element custom = doc.expectFirst("custom");

        // Verify tag properties
        assertTrue(custom.tag().preserveWhitespace(), "Should preserve whitespace");
        assertTrue(custom.tag().isBlock(), "Should be block element");

        // Verify serialization behavior
        String expectedHtml = "<custom>\n\nFoo\n Bar</custom>";
        assertEquals(expectedHtml, custom.outerHtml(), "Should maintain whitespace in output");
    }

    @Test
    void knownTagStatusBasedOnRegistration() {
        TagSet tags = TagSet.Html();

        // Test explicitly added tag
        Tag custom = new Tag("custom");
        assertFalse(custom.isKnownTag(), "New tag should not be known before registration");

        // Test standard tag from set
        Tag br = tags.get("br", NamespaceHtml);
        assertNotNull(br, "Standard tag should exist");
        assertTrue(br.isKnownTag(), "Standard tag should be known");
        assertSame(br, tags.valueOf("br", NamespaceHtml), "Should retrieve same instance");

        // Test dynamically created tag
        Tag foo = tags.valueOf("foo", NamespaceHtml);
        assertFalse(foo.isKnownTag(), "Dynamically created tag should not be known");

        // Test after explicit registration
        tags.add(custom);
        assertTrue(custom.isKnownTag(), "Should become known after registration");
        assertSame(custom, tags.get("custom", NamespaceHtml), "Should retrieve registered tag");
        assertSame(custom, tags.valueOf("custom", NamespaceHtml), "Should find via valueOf");

        // Test case-normalized version
        Tag capCustom = tags.valueOf("Custom", NamespaceHtml);
        assertTrue(capCustom.isKnownTag(), "Case-variant should inherit known status");

        // Test flag manipulation
        Tag bar = new Tag("bar");
        assertFalse(bar.isKnownTag(), "New tag should not be known");
        
        bar.set(Tag.Block);
        assertTrue(bar.isKnownTag(), "Setting flag should mark as known");
        
        bar.clear(Tag.Block);
        assertTrue(bar.isKnownTag(), "Clearing flag should maintain known status");
        
        bar.clear(Tag.Known);
        assertFalse(bar.isKnownTag(), "Explicitly clearing known status should work");
    }

    @Test
    void globalTagCustomization() {
        TagSet tags = TagSet.Html();
        tags.onNewTag(tag -> tag.set(Tag.SelfClose));

        // Verify existing tag customization
        assertTrue(tags.get("script", NamespaceHtml).is(Tag.SelfClose), "Existing tag should be customized");

        // Verify new tag creation
        assertTrue(tags.valueOf("SCRIPT", NamespaceHtml).is(Tag.SelfClose), "Case variant should be customized");
        assertTrue(tags.valueOf("custom", NamespaceHtml).is(Tag.SelfClose), "New tag should be customized");

        // Verify explicit tag addition
        Tag foo = new Tag("foo", NamespaceHtml);
        assertFalse(foo.is(Tag.SelfClose), "Should not be customized before registration");
        tags.add(foo);
        assertTrue(foo.is(Tag.SelfClose), "Should be customized during registration");
    }

    @Test
    void selectiveTagCustomization() {
        TagSet tags = TagSet.Html();
        tags.onNewTag(tag -> {
            if (!tag.isKnownTag()) {
                tag.set(Tag.SelfClose);
            }
        });

        // Known tags should not be customized
        assertFalse(tags.valueOf("script", NamespaceHtml).is(Tag.SelfClose), "Known tag should not be customized");
        assertFalse(tags.valueOf("SCRIPT", NamespaceHtml).is(Tag.SelfClose), "Known tag variant should not be customized");

        // Unknown tags should be customized
        assertTrue(tags.valueOf("custom-tag", NamespaceHtml).is(Tag.SelfClose), "Unknown tag should be customized");
    }

    @Test
    void parseWithSpecificTagCustomization() {
        Parser parser = Parser.htmlParser();
        parser.tagSet().onNewTag(tag -> {
            if (tag.normalName().equals("script")) {
                tag.set(Tag.SelfClose);
            }
        });

        Document doc = Jsoup.parse("<script />Text", parser);
        String expected = "<html>\n"
            + " <head>\n"
            + "  <script></script>\n"
            + " </head>\n"
            + " <body>Text</body>\n"
            + "</html>";
        assertEquals(expected, doc.html(), "Should parse self-closing script correctly");
    }

    @Test
    void parseWithUnknownTagCustomization() {
        Parser parser = Parser.htmlParser();
        parser.tagSet().onNewTag(tag -> {
            if (!tag.isKnownTag()) {
                tag.set(Tag.SelfClose);
            }
        });

        Document doc = Jsoup.parse("<custom-data />Bar <script />Text", parser);
        String expectedBodyHtml = "<custom-data></custom-data>Bar\n<script>Text</script>";
        assertEquals(expectedBodyHtml, doc.body().html(), "Should customize unknown tags only");
    }

    @Test
    void multipleTagCustomizersApplyCorrectly() {
        TagSet tags = TagSet.Html();
        tags.onNewTag(tag -> {
            if (tag.normalName().equals("script")) {
                tag.set(Tag.SelfClose);
            }
        });
        tags.onNewTag(tag -> {
            if (!tag.isKnownTag()) {
                tag.set(Tag.RcData);
            }
        });

        // Known tag with specific condition
        Tag scriptTag = tags.valueOf("script", NamespaceHtml);
        assertTrue(scriptTag.is(Tag.SelfClose), "Should apply first customizer");
        assertFalse(scriptTag.is(Tag.RcData), "Should not apply second customizer");

        // Unknown tag
        Tag customTag = tags.valueOf("custom-tag", NamespaceHtml);
        assertTrue(customTag.is(Tag.RcData), "Should apply second customizer");
    }

    @Test
    void tagSetCopyPreservesCustomizers() {
        TagSet source = TagSet.Html();
        source.onNewTag(tag -> tag.set(Tag.RcData));

        // Copy should inherit customizers
        TagSet copy = new TagSet(source);
        assertTrue(copy.valueOf("script", NamespaceHtml).is(Tag.RcData), "Copy should inherit customizer");
        assertTrue(source.valueOf("script", NamespaceHtml).is(Tag.RcData), "Source should maintain customizer");

        // Modifications shouldn't affect source
        copy.onNewTag(tag -> tag.set(Tag.Void));
        assertTrue(copy.valueOf("custom-tag", NamespaceHtml).is(Tag.Void), "Copy should apply new customizer");
        assertFalse(source.valueOf("custom-tag", NamespaceHtml).is(Tag.Void), "Source should not be modified");
    }
}