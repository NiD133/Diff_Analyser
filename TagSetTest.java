package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

import static org.jsoup.parser.Parser.NamespaceHtml;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link TagSet} class, which manages and provides access to {@link Tag} objects
 * during HTML parsing.  This includes testing the retrieval of known and unknown tags,
 * customization of tags, and preservation of tag customizations.
 */
public class TagSetTest {

    @Test
    void canRetrieveNewTagsSensitive() {
        // GIVEN: A document parsed with case preservation enabled.
        Document doc = Jsoup.parse("<div><p>One</p></div>", "", Parser.htmlParser().settings(ParseSettings.preserveCase));
        TagSet tags = doc.parser().tagSet();

        // WHEN: Retrieving both known and unknown tags with case-sensitive lookups.

        // THEN:  Known tags should be retrieved successfully and marked as known.
        Tag meta = tags.get("meta", NamespaceHtml);
        assertNotNull(meta, "Should be able to retrieve the 'meta' tag.");
        assertTrue(meta.isKnownTag(), "'meta' tag should be a known tag.");

        Element p = doc.expectFirst("p");
        assertTrue(p.tag().isKnownTag(), "'p' tag should be a known tag.");

        // AND:  Unknown tags should not be initially present.
        assertNull(tags.get("FOO", NamespaceHtml), "Tag 'FOO' should not initially exist.");

        // AND:  Creating a new tag with a specific name should work, and it should be case-sensitive.
        p.tagName("FOO");
        Tag foo = p.tag();
        assertEquals("FOO", foo.name(), "Tag name should be 'FOO'.");
        assertEquals("foo", foo.normalName(), "Normal tag name should be 'foo'.");
        assertEquals(NamespaceHtml, foo.namespace(), "Namespace should be HTML.");
        assertFalse(foo.isKnownTag(), "New tag should not be a known tag.");

        // AND:  Retrieving the same tag again (case-sensitively) should return the same instance.
        assertSame(foo, tags.get("FOO", NamespaceHtml), "Should retrieve the same 'FOO' tag instance.");
        assertSame(foo, tags.valueOf("FOO", NamespaceHtml), "valueOf should return the same instance.");

        // AND: Retrieving tag from another namespace should not return tag.
        assertNull(tags.get("FOO", "SomeOtherNamespace"), "Tag 'FOO' in another namespace should not be found.");
    }

    @Test
    void canRetrieveNewTagsInsensitive() {
        // GIVEN: A document parsed without case preservation (default).
        Document doc = Jsoup.parse("<div><p>One</p></div>");
        TagSet tags = doc.parser().tagSet();

        // WHEN: Retrieving both known and unknown tags with case-insensitive lookups.

        // THEN: Known tags should be retrieved successfully and marked as known.
        Tag meta = tags.get("meta", NamespaceHtml);
        assertNotNull(meta, "Should be able to retrieve the 'meta' tag.");
        assertTrue(meta.isKnownTag(), "'meta' tag should be a known tag.");

        Element p = doc.expectFirst("p");
        assertTrue(p.tag().isKnownTag(), "'p' tag should be a known tag.");

        // AND: Unknown tags should not be initially present.
        assertNull(tags.get("FOO", NamespaceHtml), "Tag 'FOO' should not initially exist.");

        // AND: Creating a new tag with a specific name should work, and the name should be normalized.
        p.tagName("FOO");
        Tag foo = p.tag();
        assertEquals("foo", foo.name(), "Tag name should be 'foo'.");
        assertEquals("foo", foo.normalName(), "Normal tag name should be 'foo'.");
        assertEquals(NamespaceHtml, foo.namespace(), "Namespace should be HTML.");
        assertFalse(foo.isKnownTag(), "New tag should not be a known tag.");

        // AND: Retrieving the same tag again (case-insensitively) should return the same instance.
        assertSame(foo, tags.get("foo", NamespaceHtml), "Should retrieve the same 'foo' tag instance.");
        assertSame(foo, tags.valueOf("FOO", NamespaceHtml, doc.parser().settings()), "valueOf should return the same instance.");

        // AND: Retrieving tag from another namespace should not return tag.
        assertNull(tags.get("foo", "SomeOtherNamespace"), "Tag 'foo' in another namespace should not be found.");
    }

    @Test
    void supplyCustomTagSet() {
        // GIVEN: A custom TagSet with a custom tag configured to preserve whitespace and be a block element.
        TagSet tags = TagSet.Html();
        tags.valueOf("custom", NamespaceHtml).set(Tag.PreserveWhitespace).set(Tag.Block);
        Parser parser = Parser.htmlParser().tagSet(tags);

        // WHEN: Parsing HTML with the custom tag set.
        Document doc = Jsoup.parse("<body><custom>\n\nFoo\n Bar</custom></body>", parser);
        Element custom = doc.expectFirst("custom");

        // THEN: The custom tag should have the configured properties.
        assertTrue(custom.tag().preserveWhitespace(), "Custom tag should preserve whitespace.");
        assertTrue(custom.tag().isBlock(), "Custom tag should be a block element.");
        assertEquals("<custom>\n" +
                "\n" +
                "Foo\n" +
                " Bar" +
                "</custom>", custom.outerHtml(), "Outer HTML should reflect whitespace preservation.");
    }

    @Test
    void knownTags() {
        // GIVEN: An HTML TagSet.
        TagSet tags = TagSet.Html();
        Tag custom = new Tag("custom");

        // WHEN: Interacting with the TagSet and custom tags.

        // THEN: New tags should initially be unknown.
        assertEquals("custom", custom.name(), "Tag name should be 'custom'.");
        assertEquals(NamespaceHtml, custom.namespace(), "Namespace should be HTML.");
        assertFalse(custom.isKnownTag(), "New tag should initially be unknown.");

        // AND: Existing HTML tags should be known.
        Tag br = tags.get("br", NamespaceHtml);
        assertNotNull(br, "'br' tag should exist.");
        assertTrue(br.isKnownTag(), "'br' tag should be known.");
        assertSame(br, tags.valueOf("br", NamespaceHtml), "valueOf should return the same 'br' instance.");

        // AND: Tags retrieved via valueOf (if not already defined) are not initially known.
        Tag foo = tags.valueOf("foo", NamespaceHtml);
        assertFalse(foo.isKnownTag(), "'foo' tag should not be known initially.");

        // AND: Adding a tag to the TagSet makes it known.
        tags.add(custom);
        assertTrue(custom.isKnownTag(), "Custom tag should now be known.");
        assertSame(custom, tags.get("custom", NamespaceHtml), "Should retrieve the same 'custom' tag instance.");
        assertSame(custom, tags.valueOf("custom", NamespaceHtml), "valueOf should return the same 'custom' tag instance.");

        // AND: Cloning from a known tag preserves the 'known' status.
        Tag capCustom = tags.valueOf("Custom", NamespaceHtml);
        assertTrue(capCustom.isKnownTag(), "Cloned tag should still be known.");

        // AND: Setting or clearing attributes on a tag makes it known even if it wasn't before
        Tag c1 = new Tag("bar");
        assertFalse(c1.isKnownTag());
        c1.set(Tag.Block);
        assertTrue(c1.isKnownTag());
        c1.clear(Tag.Block);
        assertTrue(c1.isKnownTag());
        c1.clear(Tag.Known);
        assertFalse(c1.isKnownTag());
    }

    @Test
    void canCustomizeAll() {
        // GIVEN: An HTML TagSet with a customizer that sets the SelfClose flag on all new tags.
        TagSet tags = TagSet.Html();
        tags.onNewTag(tag -> tag.set(Tag.SelfClose));

        // WHEN: Retrieving existing and new tags.

        // THEN: All tags should have the SelfClose flag set.
        assertTrue(tags.get("script", NamespaceHtml).is(Tag.SelfClose), "'script' tag should be self-closing.");
        assertTrue(tags.valueOf("SCRIPT", NamespaceHtml).is(Tag.SelfClose), "'SCRIPT' tag should be self-closing.");
        assertTrue(tags.valueOf("custom", NamespaceHtml).is(Tag.SelfClose), "'custom' tag should be self-closing.");

        // AND: Adding a tag manually should also apply the customizer.
        Tag foo = new Tag("foo", NamespaceHtml);
        assertFalse(foo.is(Tag.SelfClose));
        tags.add(foo);
        assertTrue(foo.is(Tag.SelfClose), "'foo' tag should be self-closing after being added.");
    }

    @Test
    void canCustomizeSome() {
        // GIVEN: An HTML TagSet with a customizer that sets the SelfClose flag only on unknown tags.
        TagSet tags = TagSet.Html();
        tags.onNewTag(tag -> {
            if (!tag.isKnownTag()) {
                tag.set(Tag.SelfClose);
            }
        });

        // WHEN: Retrieving existing and new tags.

        // THEN: Existing tags should not be self-closing.
        assertFalse(tags.valueOf("script", NamespaceHtml).is(Tag.SelfClose), "'script' tag should not be self-closing.");
        assertFalse(tags.valueOf("SCRIPT", NamespaceHtml).is(Tag.SelfClose), "'SCRIPT' tag should not be self-closing.");

        // AND: Unknown tags should be self-closing.
        assertTrue(tags.valueOf("custom-tag", NamespaceHtml).is(Tag.SelfClose), "'custom-tag' should be self-closing.");
    }

    @Test
    void canParseWithCustomization() {
        // GIVEN: A parser configured to set the SelfClose flag on script tags.
        Parser parser = Parser.htmlParser();
        parser.tagSet().onNewTag(tag -> {
            if (tag.normalName().equals("script"))
                tag.set(Tag.SelfClose);
        });

        // WHEN: Parsing HTML containing a self-closing script tag.
        Document doc = Jsoup.parse("<script />Text", parser);

        // THEN: The script tag should be correctly parsed as self-closing.
        assertEquals("<html>\n" +
                " <head>\n" +
                "  <script></script>\n" +
                " </head>\n" +
                " <body>Text</body>\n" +
                "</html>", doc.html(), "HTML should contain a self-closed script tag.");
    }

    @Test
    void canParseWithGeneralCustomization() {
        // GIVEN: A parser configured to set the SelfClose flag on all unknown tags.
        Parser parser = Parser.htmlParser();
        parser.tagSet().onNewTag(tag -> {
            if (!tag.isKnownTag())
                tag.set(Tag.SelfClose);
        });

        // WHEN: Parsing HTML containing a self-closing custom tag and a script tag.
        Document doc = Jsoup.parse("<custom-data />Bar <script />Text", parser);

        // THEN: The custom tag should be self-closing, and the script tag should be parsed normally.
        assertEquals("<custom-data></custom-data>Bar\n<script>Text</script>", doc.body().html(), "HTML should contain a self-closed custom tag and a script tag.");
    }

    @Test
    void supportsMultipleCustomizers() {
        // GIVEN: An HTML TagSet with two customizers: one for script tags (SelfClose) and one for unknown tags (RcData).
        TagSet tags = TagSet.Html();
        tags.onNewTag(tag -> {
            if (tag.normalName().equals("script"))
                tag.set(Tag.SelfClose);
        });
        tags.onNewTag(tag -> {
            if (!tag.isKnownTag())
                tag.set(Tag.RcData);
        });

        // WHEN: Retrieving script and custom tags.

        // THEN: Script tags should be self-closing, but not RcData.
        assertTrue(tags.valueOf("script", NamespaceHtml).is(Tag.SelfClose), "'script' tag should be self-closing.");
        assertFalse(tags.valueOf("script", NamespaceHtml).is(Tag.RcData), "'script' tag should not be RcData.");

        // AND: Custom tags should be RcData.
        assertTrue(tags.valueOf("custom-tag", NamespaceHtml).is(Tag.RcData), "'custom-tag' should be RcData.");
    }

    @Test
    void customizersArePreservedInSource() {
        // GIVEN: A TagSet with a customizer that sets the RcData flag.
        TagSet source = TagSet.Html();
        source.onNewTag(tag -> tag.set(Tag.RcData));

        // WHEN: Creating a copy of the TagSet.
        TagSet copy = new TagSet(source);

        // THEN: The copy should inherit the customizer from the source.
        assertTrue(copy.valueOf("script", NamespaceHtml).is(Tag.RcData), "'script' tag in copy should be RcData.");
        assertTrue(source.valueOf("script", NamespaceHtml).is(Tag.RcData), "'script' tag in source should be RcData.");

        // AND: Adding a new customizer to the copy should not affect the source.
        copy.onNewTag(tag -> tag.set(Tag.Void));
        assertTrue(copy.valueOf("custom-tag", NamespaceHtml).is(Tag.Void), "'custom-tag' in copy should be Void.");
        assertFalse(source.valueOf("custom-tag", NamespaceHtml).is(Tag.Void), "'custom-tag' in source should not be Void.");
    }
}