package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.jsoup.parser.Parser.NamespaceHtml;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for TagSet behaviors:
 * - Retrieving known and unknown tags with case-sensitive vs. insensitive settings
 * - Supplying and customizing TagSet instances
 * - Known vs. unknown tag semantics
 * - Parser integration and customization preservation
 */
public class TagSetTest {

    private static final String OTHER_NS = "SomeOtherNamespace";

    // Helpers

    private static Parser caseSensitiveHtmlParser() {
        return Parser.htmlParser().settings(ParseSettings.preserveCase);
    }

    private static Document parseHtml(String html) {
        return Jsoup.parse(html);
    }

    private static Document parseHtml(String html, Parser parser) {
        return Jsoup.parse(html, "", parser);
    }

    private static void assertKnown(Tag tag) {
        assertTrue(tag.isKnownTag(), "Expected tag to be known");
    }

    private static void assertUnknown(Tag tag) {
        assertFalse(tag.isKnownTag(), "Expected tag to be unknown");
    }

    // Tests

    @Test
    void retrievingNewTags_withCaseSensitiveSettings() {
        // Arrange
        Document doc = parseHtml("<div><p>One</p></div>", caseSensitiveHtmlParser());
        TagSet tags = doc.parser().tagSet();

        // Assert baseline: default HTML tags are present and known
        Tag meta = tags.get("meta", NamespaceHtml);
        assertNotNull(meta);
        assertKnown(meta);

        Element pEl = doc.expectFirst("p");
        assertKnown(pEl.tag());

        // Act: introduce a new tag in a case-sensitive context
        assertNull(tags.get("FOO", NamespaceHtml)); // not present yet
        pEl.tagName("FOO");
        Tag foo = pEl.tag();

        // Assert: tag casing preserved, but normalName is lowercase; tag is unknown
        assertAll(
            () -> assertEquals("FOO", foo.name()),
            () -> assertEquals("foo", foo.normalName()),
            () -> assertEquals(NamespaceHtml, foo.namespace()),
            () -> assertUnknown(foo)
        );

        // Assert: TagSet returns the exact same instance for same name/namespace only
        assertSame(foo, tags.get("FOO", NamespaceHtml));
        assertSame(foo, tags.valueOf("FOO", NamespaceHtml));
        assertNull(tags.get("FOO", OTHER_NS));
    }

    @Test
    void retrievingNewTags_withCaseInsensitiveSettings() {
        // Arrange
        Document doc = parseHtml("<div><p>One</p></div>");
        TagSet tags = doc.parser().tagSet();

        // Assert baseline: default HTML tags are present and known
        Tag meta = tags.get("meta", NamespaceHtml);
        assertNotNull(meta);
        assertKnown(meta);

        Element pEl = doc.expectFirst("p");
        assertKnown(pEl.tag());

        // Act: introduce a new tag in a case-insensitive context (default)
        assertNull(tags.get("FOO", NamespaceHtml)); // not present yet
        pEl.tagName("FOO");
        Tag foo = pEl.tag();

        // Assert: name/normalName both lowercased; tag is unknown
        assertAll(
            () -> assertEquals("foo", foo.name()),
            () -> assertEquals("foo", foo.normalName()),
            () -> assertEquals(NamespaceHtml, foo.namespace()),
            () -> assertUnknown(foo)
        );

        // Assert: lookups normalize casing per parser settings
        assertSame(foo, tags.get("foo", NamespaceHtml));
        assertSame(foo, tags.valueOf("FOO", NamespaceHtml, doc.parser().settings()));
        assertNull(tags.get("foo", OTHER_NS));
    }

    @Test
    void customTagSet_canDefineBehaviorAndSerialization() {
        // Arrange: start from default HTML TagSet and mark <custom> as block + preserve whitespace
        TagSet tags = TagSet.Html();
        tags.valueOf("custom", NamespaceHtml)
            .set(Tag.PreserveWhitespace)
            .set(Tag.Block);

        Parser parser = Parser.htmlParser().tagSet(tags);

        // Act
        Document doc = parseHtml("<body><custom>\n\nFoo\n Bar</custom></body>", parser);
        Element custom = doc.expectFirst("custom");

        // Assert: custom tag has expected flags and outer HTML preserved
        assertAll(
            () -> assertTrue(custom.tag().preserveWhitespace()),
            () -> assertTrue(custom.tag().isBlock()),
            () -> assertEquals(
                "<custom>\n" +
                "\n" +
                "Foo\n" +
                " Bar" +
                "</custom>", custom.outerHtml()
            )
        );
    }

    @Test
    void knownVsUnknownTags_behaveAsExpected() {
        // Known tags are those explicitly added; implicit valueOf-created tags are unknown
        TagSet tags = TagSet.Html();

        Tag custom = new Tag("custom");
        assertAll(
            () -> assertEquals("custom", custom.name()),
            () -> assertEquals(NamespaceHtml, custom.namespace()),
            () -> assertUnknown(custom) // not yet added
        );

        Tag br = tags.get("br", NamespaceHtml);
        assertNotNull(br);
        assertKnown(br);
        assertSame(br, tags.valueOf("br", NamespaceHtml));

        Tag foo = tags.valueOf("foo", NamespaceHtml);
        assertUnknown(foo);

        // After explicit add, tag is known and accessible
        tags.add(custom);
        assertKnown(custom);
        assertSame(custom, tags.get("custom", NamespaceHtml));
        assertSame(custom, tags.valueOf("custom", NamespaceHtml));

        Tag capCustom = tags.valueOf("Custom", NamespaceHtml);
        assertKnown(capCustom); // clone of a known tag stays known

        // Known bit is sticky once set via .set/.clear, unless explicitly cleared
        Tag c1 = new Tag("bar");
        assertUnknown(c1);
        c1.set(Tag.Block);
        assertKnown(c1);
        c1.clear(Tag.Block);
        assertKnown(c1);
        c1.clear(Tag.Known);
        assertUnknown(c1);
    }

    @Test
    void onNewTag_appliesToAllTags_whenUnconditional() {
        // Arrange
        TagSet tags = TagSet.Html();
        tags.onNewTag(tag -> tag.set(Tag.SelfClose));

        // Assert: applies to built-ins, clones, and new tags
        assertTrue(tags.get("script", NamespaceHtml).is(Tag.SelfClose));       // existing default
        assertTrue(tags.valueOf("SCRIPT", NamespaceHtml).is(Tag.SelfClose));   // clone via different case
        assertTrue(tags.valueOf("custom", NamespaceHtml).is(Tag.SelfClose));   // new tag

        // Explicitly adding also applies customizer
        Tag foo = new Tag("foo", NamespaceHtml);
        assertFalse(foo.is(Tag.SelfClose));
        tags.add(foo);
        assertTrue(foo.is(Tag.SelfClose));
    }

    @Test
    void onNewTag_appliesOnlyToUnknownTags_whenConditioned() {
        // Arrange
        TagSet tags = TagSet.Html();
        tags.onNewTag(tag -> {
            if (!tag.isKnownTag()) tag.set(Tag.SelfClose);
        });

        // Assert: known tags unaffected, unknown tags customized
        assertFalse(tags.valueOf("script", NamespaceHtml).is(Tag.SelfClose));
        assertFalse(tags.valueOf("SCRIPT", NamespaceHtml).is(Tag.SelfClose));
        assertTrue(tags.valueOf("custom-tag", NamespaceHtml).is(Tag.SelfClose));
    }

    @Test
    void parserHonorsTagCustomizations_duringParse() {
        // Arrange: make <script> self-closing in the TagSet
        Parser parser = Parser.htmlParser();
        parser.tagSet().onNewTag(tag -> {
            if (tag.normalName().equals("script"))
                tag.set(Tag.SelfClose);
        });

        // Act
        Document doc = parseHtml("<script />Text", parser);

        // Assert: output is still valid HTML
        assertEquals(
            "<html>\n <head>\n  <script></script>\n </head>\n <body>Text</body>\n</html>",
            doc.html()
        );
    }

    @Test
    void parserHonorsGeneralCustomizations_duringParse() {
        // Arrange: make all unknown tags self-closing
        Parser parser = Parser.htmlParser();
        parser.tagSet().onNewTag(tag -> {
            if (!tag.isKnownTag())
                tag.set(Tag.SelfClose);
        });

        // Act
        Document doc = parseHtml("<custom-data />Bar <script />Text", parser);

        // Assert
        assertEquals("<custom-data></custom-data>Bar\n<script>Text</script>", doc.body().html());
    }

    @Test
    void multipleOnNewTagCustomizers_areAppliedInOrder() {
        // Arrange
        TagSet tags = TagSet.Html();

        Consumer<Tag> selfCloseScripts = tag -> {
            if (tag.normalName().equals("script"))
                tag.set(Tag.SelfClose);
        };
        Consumer<Tag> rcDataForUnknown = tag -> {
            if (!tag.isKnownTag())
                tag.set(Tag.RcData);
        };

        tags.onNewTag(selfCloseScripts);
        tags.onNewTag(rcDataForUnknown);

        // Assert: both applied; conditions ensure correct flags
        assertTrue(tags.valueOf("script", NamespaceHtml).is(Tag.SelfClose));
        assertFalse(tags.valueOf("script", NamespaceHtml).is(Tag.RcData));
        assertTrue(tags.valueOf("custom-tag", NamespaceHtml).is(Tag.RcData));
    }

    @Test
    void copyingATagSet_preservesSourceAndAllowsFurtherCustomization() {
        // Arrange: source with RCData for all tags
        TagSet source = TagSet.Html();
        source.onNewTag(tag -> tag.set(Tag.RcData));

        // Act: copy from source, then customize copy further
        TagSet copy = new TagSet(source);

        // Assert: copy sees source customizations
        assertTrue(copy.valueOf("script", NamespaceHtml).is(Tag.RcData));
        assertTrue(source.valueOf("script", NamespaceHtml).is(Tag.RcData));

        // And new customization on copy does not leak back to source
        copy.onNewTag(tag -> tag.set(Tag.Void));
        assertTrue(copy.valueOf("custom-tag", NamespaceHtml).is(Tag.Void));
        assertFalse(source.valueOf("custom-tag", NamespaceHtml).is(Tag.Void));
    }
}