package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

import static org.jsoup.parser.Parser.NamespaceHtml;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link TagSet} class, which manages known and discovered HTML tags during parsing.
 */
public class TagSetTest {

    @Test
    void newTagRetrievalIsCaseSensitiveWhenPreserveCaseIsEnabled() {
        // Arrange
        Parser parser = Parser.htmlParser().settings(ParseSettings.preserveCase);
        Document doc = Jsoup.parse("<div><p>One</p></div>", "", parser);
        TagSet tags = doc.parser().tagSet();

        // Assert: Default and parsed HTML tags are known
        assertTrue(tags.get("meta", NamespaceHtml).isKnownTag());
        assertTrue(doc.expectFirst("p").tag().isKnownTag());

        // Act: Create a new tag with a specific case by changing an element's tag name
        assertNull(tags.get("FOO", NamespaceHtml), "Tag should not exist before creation.");
        Element p = doc.expectFirst("p");
        p.tagName("FOO");
        Tag foo = p.tag();

        // Assert: The new tag has the correct properties and is not "known" by default
        assertEquals("FOO", foo.name());
        assertEquals("foo", foo.normalName());
        assertFalse(foo.isKnownTag());

        // Assert: The new tag can be retrieved from the set using its exact case
        assertSame(foo, tags.get("FOO", NamespaceHtml));
        assertSame(foo, tags.valueOf("FOO", NamespaceHtml));
        assertNull(tags.get("foo", NamespaceHtml), "Should not find with different case when preserveCase is on.");
        assertNull(tags.get("FOO", "SomeOtherNamespace"), "Should not find in a different namespace.");
    }

    @Test
    void newTagRetrievalIsCaseInsensitiveByDefault() {
        // Arrange
        Document doc = Jsoup.parse("<div><p>One</p></div>");
        TagSet tags = doc.parser().tagSet();
        ParseSettings settings = doc.parser().settings();

        // Assert: Default and parsed HTML tags are known
        assertTrue(tags.get("meta", NamespaceHtml).isKnownTag());
        assertTrue(doc.expectFirst("p").tag().isKnownTag());

        // Act: Create a new tag
        assertNull(tags.get("FOO", NamespaceHtml), "Tag should not exist before creation.");
        Element p = doc.expectFirst("p");
        p.tagName("FOO");
        Tag foo = p.tag();

        // Assert: The new tag is normalized to lowercase and is not "known" by default
        assertEquals("foo", foo.name());
        assertEquals("foo", foo.normalName());
        assertFalse(foo.isKnownTag());

        // Assert: The new tag can be retrieved from the set case-insensitively
        assertSame(foo, tags.get("foo", NamespaceHtml));
        assertSame(foo, tags.valueOf("FOO", NamespaceHtml, settings));
        assertNull(tags.get("foo", "SomeOtherNamespace"), "Should not find in a different namespace.");
    }

    @Test
    void parserCanUsePreConfiguredTagSet() {
        // Arrange: Create a custom TagSet where <custom> is a block, whitespace-preserving tag
        TagSet tags = TagSet.Html();
        tags.valueOf("custom", NamespaceHtml)
            .set(Tag.PreserveWhitespace)
            .set(Tag.Block);
        Parser parser = Parser.htmlParser().tagSet(tags);
        String html = "<body><custom>\n\nFoo\n Bar</custom></body>";

        // Act
        Document doc = Jsoup.parse(html, parser);
        Element custom = doc.expectFirst("custom");

        // Assert: The custom tag's properties were respected during parsing
        assertTrue(custom.tag().preserveWhitespace());
        assertTrue(custom.tag().isBlock());
        assertEquals("<custom>\n\nFoo\n Bar</custom>", custom.outerHtml());
    }

    @Test
    void defaultHtmlTagsAreKnown() {
        // Arrange
        TagSet tags = TagSet.Html();

        // Act
        Tag br = tags.get("br", NamespaceHtml);

        // Assert
        assertNotNull(br);
        assertTrue(br.isKnownTag());
        assertSame(br, tags.valueOf("br", NamespaceHtml), "valueOf should return the same known tag instance.");
    }

    @Test
    void dynamicallyCreatedTagsViaValueOfAreNotKnown() {
        // Arrange
        TagSet tags = TagSet.Html();

        // Act
        Tag foo = tags.valueOf("foo", NamespaceHtml);

        // Assert
        assertFalse(foo.isKnownTag(), "A tag created on-the-fly for an unknown name should not be 'known'.");
    }

    @Test
    void addingTagMakesItKnown() {
        // Arrange
        TagSet tags = TagSet.Html();
        Tag custom = new Tag("custom");
        assertFalse(custom.isKnownTag(), "A new Tag is not known by default.");

        // Act
        tags.add(custom);

        // Assert
        assertTrue(custom.isKnownTag(), "Tag should be known after being added to the set.");
        assertSame(custom, tags.get("custom", NamespaceHtml));
        assertSame(custom, tags.valueOf("custom", NamespaceHtml));
    }

    @Test
    void clonedKnownTagIsAlsoKnown() {
        // Arrange
        TagSet tags = TagSet.Html();
        Tag custom = new Tag("custom");
        tags.add(custom); // 'custom' is now a known tag in this set

        // Act: valueOf with a different case will find and clone the original known tag
        Tag capCustom = tags.valueOf("Custom", NamespaceHtml);

        // Assert
        assertTrue(capCustom.isKnownTag(), "A tag cloned from a known tag should also be known.");
        assertNotSame(custom, capCustom, "The tag should be a new, cloned instance.");
        assertEquals("custom", capCustom.normalName());
    }

    @Test
    void modifyingTagPropertiesMakesItKnown() {
        // Arrange
        Tag barTag = new Tag("bar");
        assertFalse(barTag.isKnownTag(), "A new Tag is not known by default.");

        // Act & Assert: Setting a property marks the tag as known
        barTag.set(Tag.Block);
        assertTrue(barTag.isKnownTag(), "Setting a property should mark the tag as known.");

        // Act & Assert: Clearing a property does not change its known status
        barTag.clear(Tag.Block);
        assertTrue(barTag.isKnownTag(), "Clearing a property should not make the tag unknown.");

        // Act & Assert: Explicitly clearing the 'Known' flag makes it unknown
        barTag.clear(Tag.Known);
        assertFalse(barTag.isKnownTag());
    }

    @Test
    void onNewTagCustomizerCanModifyAllTags() {
        // Arrange
        TagSet tags = TagSet.Html();

        // Act: Add a customizer to make all tags self-closing
        tags.onNewTag(tag -> tag.set(Tag.SelfClose));

        // Assert: The customizer affects known tags, case-variants, and newly created tags
        assertTrue(tags.get("script", NamespaceHtml).is(Tag.SelfClose), "Known tag should be customized.");
        assertTrue(tags.valueOf("SCRIPT", NamespaceHtml).is(Tag.SelfClose), "Case-variant of known tag should be customized.");
        assertTrue(tags.valueOf("custom", NamespaceHtml).is(Tag.SelfClose), "New tag should be customized.");

        // Arrange 2: A tag created before being added to the set
        Tag foo = new Tag("foo", NamespaceHtml);
        assertFalse(foo.is(Tag.SelfClose));

        // Act 2: Add the pre-existing tag to the set
        tags.add(foo);

        // Assert 2: The customizer is applied when the tag is added
        assertTrue(foo.is(Tag.SelfClose));
    }

    @Test
    void onNewTagCustomizerCanSelectivelyModifyTags() {
        // Arrange
        TagSet tags = TagSet.Html();

        // Act: Add a customizer that only affects tags that are not already known
        tags.onNewTag(tag -> {
            if (!tag.isKnownTag()) {
                tag.set(Tag.SelfClose);
            }
        });

        // Assert: Known tags are not affected
        assertFalse(tags.valueOf("script", NamespaceHtml).is(Tag.SelfClose));
        assertFalse(tags.valueOf("SCRIPT", NamespaceHtml).is(Tag.SelfClose));

        // Assert: A new, unknown tag is affected
        assertTrue(tags.valueOf("custom-tag", NamespaceHtml).is(Tag.SelfClose));
    }

    @Test
    void parserAppliesTagCustomizationDuringParsing() {
        // Arrange: Create a parser and customize its TagSet to make <script> self-closing
        Parser parser = Parser.htmlParser();
        parser.tagSet().onNewTag(tag -> {
            if (tag.normalName().equals("script"))
                tag.set(Tag.SelfClose);
        });

        // Act
        Document doc = Jsoup.parse("<script />Text", parser);

        // Assert: The parser treated <script> as self-closing, so "Text" becomes a sibling in the body.
        // The final output is still valid HTML, where an empty <script> is created in the head.
        assertEquals("<html>\n <head>\n  <script></script>\n </head>\n <body>Text</body>\n</html>", doc.html());
    }

    @Test
    void parserAppliesGeneralCustomizationToUnknownTags() {
        // Arrange: Customize the parser to make all unknown tags self-closing
        Parser parser = Parser.htmlParser();
        parser.tagSet().onNewTag(tag -> {
            if (!tag.isKnownTag())
                tag.set(Tag.SelfClose);
        });

        // Act
        Document doc = Jsoup.parse("<custom-data />Bar <script />Text", parser);

        // Assert: <custom-data> was treated as self-closing, so "Bar" is its sibling.
        // <script> is a known tag, so it was not affected and contains "Text".
        assertEquals("<custom-data></custom-data>Bar <script>Text</script>", doc.body().html());
    }

    @Test
    void multipleCustomizersAreAppliedInOrderOfAddition() {
        // Arrange
        TagSet tags = TagSet.Html();

        // Act: Add two customizers
        tags.onNewTag(tag -> {
            if (tag.normalName().equals("script"))
                tag.set(Tag.SelfClose);
        });
        tags.onNewTag(tag -> {
            if (!tag.isKnownTag())
                tag.set(Tag.RcData);
        });

        // Assert: The first customizer affects the known 'script' tag
        assertTrue(tags.valueOf("script", NamespaceHtml).is(Tag.SelfClose));
        assertFalse(tags.valueOf("script", NamespaceHtml).is(Tag.RcData), "'script' is a known tag, so the second customizer should not apply.");

        // Assert: The second customizer affects a new, unknown tag
        assertTrue(tags.valueOf("custom-tag", NamespaceHtml).is(Tag.RcData));
    }

    @Test
    void copiedTagSetInheritsAndCanExtendCustomizers() {
        // Arrange: Create a source TagSet with a customizer
        TagSet source = TagSet.Html();
        source.onNewTag(tag -> tag.set(Tag.RcData));

        // Act: Create a copy of the source TagSet
        TagSet copy = new TagSet(source);

        // Assert: The copy inherits the customizer from the source
        assertTrue(copy.valueOf("script", NamespaceHtml).is(Tag.RcData));
        assertTrue(source.valueOf("script", NamespaceHtml).is(Tag.RcData));

        // Act: Add a new customizer to the copy only
        copy.onNewTag(tag -> tag.set(Tag.Void));

        // Assert: The new customizer affects the copy but not the source
        assertTrue(copy.valueOf("custom-tag", NamespaceHtml).is(Tag.Void));
        assertFalse(source.valueOf("custom-tag", NamespaceHtml).is(Tag.Void), "Source should not be affected by changes to the copy.");
    }
}