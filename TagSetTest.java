package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

import static org.jsoup.parser.Parser.NamespaceHtml;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for TagSet functionality including tag retrieval, customization, and case sensitivity handling.
 */
public class TagSetTest {

    @Test 
    void shouldHandleNewTagsWithCaseSensitiveSettings() {
        // Given: A document parsed with case-preserving settings
        Document doc = Jsoup.parse("<div><p>One</p></div>", "", 
            Parser.htmlParser().settings(ParseSettings.preserveCase));
        TagSet tagSet = doc.parser().tagSet();

        // When/Then: Should contain standard HTML tags
        Tag metaTag = tagSet.get("meta", NamespaceHtml);
        assertNotNull(metaTag, "Standard HTML meta tag should exist");
        assertTrue(metaTag.isKnownTag(), "Meta tag should be recognized as known");

        Element paragraphElement = doc.expectFirst("p");
        assertTrue(paragraphElement.tag().isKnownTag(), "Paragraph tag should be known");

        // When: Creating a new custom tag with uppercase name
        assertNull(tagSet.get("FOO", NamespaceHtml), "Custom tag should not exist initially");
        paragraphElement.tagName("FOO");
        Tag customTag = paragraphElement.tag();

        // Then: Custom tag should preserve case but have normalized name
        assertEquals("FOO", customTag.name(), "Tag name should preserve original case");
        assertEquals("foo", customTag.normalName(), "Normal name should be lowercase");
        assertEquals(NamespaceHtml, customTag.namespace(), "Tag should be in HTML namespace");
        assertFalse(customTag.isKnownTag(), "Custom tag should not be marked as known");

        // And: Tag should be retrievable from TagSet
        assertSame(customTag, tagSet.get("FOO", NamespaceHtml), 
            "Should retrieve same tag instance by exact name");
        assertSame(customTag, tagSet.valueOf("FOO", NamespaceHtml), 
            "valueOf should return same tag instance");
        assertNull(tagSet.get("FOO", "SomeOtherNamespace"), 
            "Tag should not exist in different namespace");
    }

    @Test 
    void shouldHandleNewTagsWithCaseInsensitiveSettings() {
        // Given: A document parsed with default (case-insensitive) settings
        Document doc = Jsoup.parse("<div><p>One</p></div>");
        TagSet tagSet = doc.parser().tagSet();

        // When/Then: Should contain standard HTML tags
        Tag metaTag = tagSet.get("meta", NamespaceHtml);
        assertNotNull(metaTag, "Standard HTML meta tag should exist");
        assertTrue(metaTag.isKnownTag(), "Meta tag should be recognized as known");

        Element paragraphElement = doc.expectFirst("p");
        assertTrue(paragraphElement.tag().isKnownTag(), "Paragraph tag should be known");

        // When: Creating a new custom tag with uppercase name
        assertNull(tagSet.get("FOO", NamespaceHtml), "Custom tag should not exist initially");
        paragraphElement.tagName("FOO");
        Tag customTag = paragraphElement.tag();

        // Then: Custom tag should be normalized to lowercase
        assertEquals("foo", customTag.name(), "Tag name should be normalized to lowercase");
        assertEquals("foo", customTag.normalName(), "Normal name should match lowercase name");
        assertEquals(NamespaceHtml, customTag.namespace(), "Tag should be in HTML namespace");
        assertFalse(customTag.isKnownTag(), "Custom tag should not be marked as known");

        // And: Tag should be retrievable from TagSet
        assertSame(customTag, tagSet.get("foo", NamespaceHtml), 
            "Should retrieve same tag instance by normalized name");
        assertSame(customTag, tagSet.valueOf("FOO", NamespaceHtml, doc.parser().settings()), 
            "valueOf should return same tag instance regardless of case");
        assertNull(tagSet.get("foo", "SomeOtherNamespace"), 
            "Tag should not exist in different namespace");
    }

    @Test 
    void shouldAllowCustomTagSetWithSpecialProperties() {
        // Given: A custom TagSet with a tag configured for whitespace preservation and block display
        TagSet customTagSet = TagSet.Html();
        customTagSet.valueOf("custom", NamespaceHtml)
            .set(Tag.PreserveWhitespace)
            .set(Tag.Block);
        Parser parserWithCustomTags = Parser.htmlParser().tagSet(customTagSet);

        // When: Parsing HTML with the custom tag
        Document doc = Jsoup.parse("<body><custom>\n\nFoo\n Bar</custom></body>", parserWithCustomTags);
        Element customElement = doc.expectFirst("custom");

        // Then: Custom tag should have the configured properties
        assertTrue(customElement.tag().preserveWhitespace(), 
            "Custom tag should preserve whitespace");
        assertTrue(customElement.tag().isBlock(), 
            "Custom tag should be block-level");
        
        String expectedHtml = "<custom>\n\nFoo\n Bar</custom>";
        assertEquals(expectedHtml, customElement.outerHtml(), 
            "Whitespace should be preserved in output");
    }

    @Test 
    void shouldDistinguishBetweenKnownAndUnknownTags() {
        // Given: A fresh HTML TagSet
        TagSet tagSet = TagSet.Html();
        
        // When: Creating a custom tag (not yet added to set)
        Tag customTag = new Tag("custom");
        assertEquals("custom", customTag.name());
        assertEquals(NamespaceHtml, customTag.namespace());
        assertFalse(customTag.isKnownTag(), "New tag should not be known initially");

        // Then: Standard HTML tags should be known
        Tag breakTag = tagSet.get("br", NamespaceHtml);
        assertNotNull(breakTag, "Standard br tag should exist");
        assertTrue(breakTag.isKnownTag(), "Standard br tag should be known");
        assertSame(breakTag, tagSet.valueOf("br", NamespaceHtml), 
            "valueOf should return same instance for known tags");

        // And: Tags created via valueOf should not be known
        Tag unknownTag = tagSet.valueOf("foo", NamespaceHtml);
        assertFalse(unknownTag.isKnownTag(), "Tags created via valueOf should not be known");

        // When: Explicitly adding custom tag to set
        tagSet.add(customTag);
        
        // Then: Added tag should become known
        assertTrue(customTag.isKnownTag(), "Explicitly added tag should be known");
        assertSame(customTag, tagSet.get("custom", NamespaceHtml), 
            "Should retrieve same added tag instance");
        assertSame(customTag, tagSet.valueOf("custom", NamespaceHtml), 
            "valueOf should return same added tag instance");
        
        Tag capitalizedCustomTag = tagSet.valueOf("Custom", NamespaceHtml);
        assertTrue(capitalizedCustomTag.isKnownTag(), 
            "Tag cloned from known tag should also be known");

        // And: Tags become known when properties are set
        Tag configurableTag = new Tag("bar");
        assertFalse(configurableTag.isKnownTag(), "New tag should not be known initially");
        
        configurableTag.set(Tag.Block);
        assertTrue(configurableTag.isKnownTag(), "Tag should be known after setting properties");
        
        configurableTag.clear(Tag.Block);
        assertTrue(configurableTag.isKnownTag(), "Tag should remain known after clearing properties");
        
        configurableTag.clear(Tag.Known);
        assertFalse(configurableTag.isKnownTag(), "Tag should not be known after clearing Known flag");
    }

    @Test 
    void shouldApplyCustomizerToAllTags() {
        // Given: A TagSet with a customizer that makes all tags self-closing
        TagSet tagSet = TagSet.Html();
        tagSet.onNewTag(tag -> tag.set(Tag.SelfClose));

        // Then: All tags (existing and new) should be self-closing
        assertTrue(tagSet.get("script", NamespaceHtml).is(Tag.SelfClose), 
            "Existing standard tag should be customized");
        assertTrue(tagSet.valueOf("SCRIPT", NamespaceHtml).is(Tag.SelfClose), 
            "Case variant of standard tag should be customized");
        assertTrue(tagSet.valueOf("custom", NamespaceHtml).is(Tag.SelfClose), 
            "New custom tag should be customized");

        // And: Tags added after customizer registration should also be customized
        Tag newTag = new Tag("foo", NamespaceHtml);
        assertFalse(newTag.is(Tag.SelfClose), "Tag should not be self-closing before adding to set");
        
        tagSet.add(newTag);
        assertTrue(newTag.is(Tag.SelfClose), "Tag should be customized when added to set");
    }

    @Test 
    void shouldApplyCustomizerSelectivelyToUnknownTags() {
        // Given: A TagSet with a customizer that only affects unknown tags
        TagSet tagSet = TagSet.Html();
        tagSet.onNewTag(tag -> {
            if (!tag.isKnownTag()) {
                tag.set(Tag.SelfClose);
            }
        });

        // Then: Known tags should not be affected
        assertFalse(tagSet.valueOf("script", NamespaceHtml).is(Tag.SelfClose), 
            "Known standard tag should not be customized");
        assertFalse(tagSet.valueOf("SCRIPT", NamespaceHtml).is(Tag.SelfClose), 
            "Case variant of known tag should not be customized");
        
        // But: Unknown tags should be customized
        assertTrue(tagSet.valueOf("custom-tag", NamespaceHtml).is(Tag.SelfClose), 
            "Unknown custom tag should be customized");
    }

    @Test 
    void shouldParseDocumentWithTagCustomization() {
        // Given: A parser with customized script tag behavior
        Parser customParser = Parser.htmlParser();
        customParser.tagSet().onNewTag(tag -> {
            if (tag.normalName().equals("script")) {
                tag.set(Tag.SelfClose);
            }
        });

        // When: Parsing HTML with script tag
        Document doc = Jsoup.parse("<script />Text", customParser);

        // Then: Document should be parsed with customized behavior
        String expectedHtml = "<html>\n <head>\n  <script></script>\n </head>\n <body>Text</body>\n</html>";
        assertEquals(expectedHtml, doc.html(), 
            "Script tag should be handled as self-closing but produce valid HTML");
    }

    @Test 
    void shouldParseDocumentWithGeneralTagCustomization() {
        // Given: A parser that makes all unknown tags self-closing
        Parser customParser = Parser.htmlParser();
        customParser.tagSet().onNewTag(tag -> {
            if (!tag.isKnownTag()) {
                tag.set(Tag.SelfClose);
            }
        });

        // When: Parsing HTML with both known and unknown tags
        Document doc = Jsoup.parse("<custom-data />Bar <script />Text", customParser);

        // Then: Only unknown tags should be affected
        String expectedBodyHtml = "<custom-data></custom-data>Bar\n<script>Text</script>";
        assertEquals(expectedBodyHtml, doc.body().html(), 
            "Unknown tags should be self-closing, known tags should behave normally");
    }

    @Test 
    void shouldSupportMultipleTagCustomizers() {
        // Given: A TagSet with multiple customizers
        TagSet tagSet = TagSet.Html();
        
        // First customizer: make script tags self-closing
        tagSet.onNewTag(tag -> {
            if (tag.normalName().equals("script")) {
                tag.set(Tag.SelfClose);
            }
        });
        
        // Second customizer: make unknown tags RcData
        tagSet.onNewTag(tag -> {
            if (!tag.isKnownTag()) {
                tag.set(Tag.RcData);
            }
        });

        // Then: Both customizers should be applied appropriately
        Tag scriptTag = tagSet.valueOf("script", NamespaceHtml);
        assertTrue(scriptTag.is(Tag.SelfClose), 
            "Script tag should be affected by first customizer");
        assertFalse(scriptTag.is(Tag.RcData), 
            "Known script tag should not be affected by second customizer");

        Tag customTag = tagSet.valueOf("custom-tag", NamespaceHtml);
        assertTrue(customTag.is(Tag.RcData), 
            "Unknown tag should be affected by second customizer");
    }

    @Test 
    void shouldPreserveCustomizersWhenCopyingTagSet() {
        // Given: A source TagSet with a customizer
        TagSet sourceTagSet = TagSet.Html();
        sourceTagSet.onNewTag(tag -> tag.set(Tag.RcData));
        
        // When: Creating a copy of the TagSet
        TagSet copiedTagSet = new TagSet(sourceTagSet);

        // Then: Both TagSets should apply the original customizer
        assertTrue(copiedTagSet.valueOf("script", NamespaceHtml).is(Tag.RcData), 
            "Copied TagSet should apply inherited customizer");
        assertTrue(sourceTagSet.valueOf("script", NamespaceHtml).is(Tag.RcData), 
            "Source TagSet should still apply its customizer");

        // When: Adding a new customizer to the copy
        copiedTagSet.onNewTag(tag -> tag.set(Tag.Void));

        // Then: Copy should have both customizers, source should have only original
        Tag copiedCustomTag = copiedTagSet.valueOf("custom-tag", NamespaceHtml);
        assertTrue(copiedCustomTag.is(Tag.Void), 
            "Copied TagSet should apply new customizer");
        
        Tag sourceCustomTag = sourceTagSet.valueOf("custom-tag", NamespaceHtml);
        assertFalse(sourceCustomTag.is(Tag.Void), 
            "Source TagSet should not be affected by copy's new customizer");
    }
}