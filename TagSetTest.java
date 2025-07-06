package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.junit.jupiter.api.Test;

import static org.jsoup.parser.Parser.NamespaceHtml;
import static org.junit.jupiter.api.Assertions.*;

public class TagSetTest {

    @Test
    void testCanRetrieveNewTagsSensitive() {
        // Arrange
        Document doc = Jsoup.parse("<div><p>One</p></div>", "", Parser.htmlParser().settings(ParseSettings.preserveCase));
        TagSet tags = doc.parser().tagSet();

        // Act and Assert
        testTagIsKnown(tags, "meta");
        testTagIsKnown(doc, "p");

        testTagIsUnknown(tags, "FOO");

        // Change the tag name and verify the new tag
        Element p = doc.expectFirst("p");
        p.tagName("FOO");
        Tag foo = p.tag();
        assertTagProperties(foo, "FOO", "foo", NamespaceHtml, false);
        testSameTagIsReturned(tags, foo, "FOO", NamespaceHtml);
        testSameTagIsReturned(tags, foo, "FOO", NamespaceHtml);
        testTagIsUnknown(tags, "FOO", "SomeOtherNamespace");
    }

    @Test
    void testCanRetrieveNewTagsInsensitive() {
        // Arrange
        Document doc = Jsoup.parse("<div><p>One</p></div>");
        TagSet tags = doc.parser().tagSet();

        // Act and Assert
        testTagIsKnown(tags, "meta");
        testTagIsKnown(doc, "p");

        testTagIsUnknown(tags, "FOO");

        // Change the tag name and verify the new tag
        Element p = doc.expectFirst("p");
        p.tagName("FOO");
        Tag foo = p.tag();
        assertTagProperties(foo, "foo", "foo", NamespaceHtml, false);
        testSameTagIsReturned(tags, foo, "foo", NamespaceHtml);
        testSameTagIsReturned(tags, foo, "FOO", NamespaceHtml, doc.parser().settings());
        testTagIsUnknown(tags, "foo", "SomeOtherNamespace");
    }

    @Test
    void testSupplyCustomTagSet() {
        // Arrange
        TagSet tags = TagSet.Html();
        tags.valueOf("custom", NamespaceHtml).set(Tag.PreserveWhitespace).set(Tag.Block);
        Parser parser = Parser.htmlParser().tagSet(tags);

        // Act
        Document doc = Jsoup.parse("<body><custom>\n\nFoo\n Bar</custom></body>", parser);

        // Assert
        Element custom = doc.expectFirst("custom");
        assertTrue(custom.tag().preserveWhitespace());
        assertTrue(custom.tag().isBlock());
        assertEquals("<custom>\n" +
            "\n" +
            "Foo\n" +
            " Bar" +
            "</custom>", custom.outerHtml());
    }

    @Test
    void testKnownTags() {
        // Arrange
        TagSet tags = TagSet.Html();
        Tag custom = new Tag("custom");

        // Act and Assert
        testTagIsUnknown(custom);
        testTagIsKnown(tags, "br");

        Tag foo = tags.valueOf("foo", NamespaceHtml);
        testTagIsUnknown(foo);

        // Add the custom tag and verify its properties
        tags.add(custom);
        testTagIsKnown(tags, "custom");
        testSameTagIsReturned(tags, custom, "custom", NamespaceHtml);
        testSameTagIsReturned(tags, custom, "Custom", NamespaceHtml);
    }

    @Test
    void testCanCustomizeAll() {
        // Arrange
        TagSet tags = TagSet.Html();
        tags.onNewTag(tag -> tag.set(Tag.SelfClose));

        // Act and Assert
        testTagIsSelfClosing(tags, "script");
        testTagIsSelfClosing(tags, "SCRIPT");
        testTagIsSelfClosing(tags, "custom");

        // Create a new tag and verify its properties
        Tag foo = new Tag("foo", NamespaceHtml);
        assertFalse(foo.is(Tag.SelfClose));
        tags.add(foo);
        assertTrue(foo.is(Tag.SelfClose));
    }

    @Test
    void testCanCustomizeSome() {
        // Arrange
        TagSet tags = TagSet.Html();
        tags.onNewTag(tag -> {
            if (!tag.isKnownTag()) {
                tag.set(Tag.SelfClose);
            }
        });

        // Act and Assert
        testTagIsNotSelfClosing(tags, "script");
        testTagIsNotSelfClosing(tags, "SCRIPT");
        testTagIsSelfClosing(tags, "custom-tag");
    }

    @Test
    void testCanParseWithCustomization() {
        // Arrange
        Parser parser = Parser.htmlParser();
        parser.tagSet().onNewTag(tag -> {
            if (tag.normalName().equals("script"))
                tag.set(Tag.SelfClose);
        });

        // Act
        Document doc = Jsoup.parse("<script />Text", parser);

        // Assert
        assertEquals("<html>\n <head>\n  <script></script>\n </head>\n <body>Text</body>\n</html>", doc.html());
    }

    @Test
    void testCanParseWithGeneralCustomization() {
        // Arrange
        Parser parser = Parser.htmlParser();
        parser.tagSet().onNewTag(tag -> {
            if (!tag.isKnownTag())
                tag.set(Tag.SelfClose);
        });

        // Act
        Document doc = Jsoup.parse("<custom-data />Bar <script />Text", parser);

        // Assert
        assertEquals("<custom-data></custom-data>Bar\n<script>Text</script>", doc.body().html());
    }

    @Test
    void testSupportsMultipleCustomizers() {
        // Arrange
        TagSet tags = TagSet.Html();
        tags.onNewTag(tag -> {
            if (tag.normalName().equals("script"))
                tag.set(Tag.SelfClose);
        });
        tags.onNewTag(tag -> {
            if (!tag.isKnownTag())
                tag.set(Tag.RcData);
        });

        // Act and Assert
        testTagIsSelfClosing(tags, "script");
        testTagIsNotRcData(tags, "script");
        testTagIsRcData(tags, "custom-tag");
    }

    @Test
    void testCustomizersArePreservedInSource() {
        // Arrange
        TagSet source = TagSet.Html();
        source.onNewTag(tag -> tag.set(Tag.RcData));
        TagSet copy = new TagSet(source);

        // Act and Assert
        testTagIsRcData(copy, "script");
        testTagIsRcData(source, "script");

        copy.onNewTag(tag -> tag.set(Tag.Void));
        testTagIsVoid(copy, "custom-tag");
        testTagIsNotVoid(source, "custom-tag");
    }

    private void testTagIsKnown(TagSet tags, String tagName) {
        Tag tag = tags.get(tagName, NamespaceHtml);
        assertNotNull(tag);
        assertTrue(tag.isKnownTag());
    }

    private void testTagIsKnown(Document doc, String tagName) {
        Element element = doc.expectFirst(tagName);
        assertTrue(element.tag().isKnownTag());
    }

    private void testTagIsUnknown(TagSet tags, String tagName) {
        Tag tag = tags.get(tagName, NamespaceHtml);
        assertNull(tag);
    }

    private void testTagIsUnknown(TagSet tags, String tagName, String namespace) {
        Tag tag = tags.get(tagName, namespace);
        assertNull(tag);
    }

    private void testSameTagIsReturned(TagSet tags, Tag expectedTag, String tagName, String namespace) {
        Tag tag = tags.get(tagName, namespace);
        assertSame(expectedTag, tag);
    }

    private void testSameTagIsReturned(TagSet tags, Tag expectedTag, String tagName, String namespace, ParseSettings settings) {
        Tag tag = tags.valueOf(tagName, namespace, settings);
        assertSame(expectedTag, tag);
    }

    private void testTagIsUnknown(Tag tag) {
        assertFalse(tag.isKnownTag());
    }

    private void assertTagProperties(Tag tag, String name, String normalName, String namespace, boolean isKnownTag) {
        assertEquals(name, tag.name());
        assertEquals(normalName, tag.normalName());
        assertEquals(namespace, tag.namespace());
        assertEquals(isKnownTag, tag.isKnownTag());
    }

    private void testTagIsSelfClosing(TagSet tags, String tagName) {
        Tag tag = tags.valueOf(tagName, NamespaceHtml);
        assertTrue(tag.is(Tag.SelfClose));
    }

    private void testTagIsNotSelfClosing(TagSet tags, String tagName) {
        Tag tag = tags.valueOf(tagName, NamespaceHtml);
        assertFalse(tag.is(Tag.SelfClose));
    }

    private void testTagIsRcData(TagSet tags, String tagName) {
        Tag tag = tags.valueOf(tagName, NamespaceHtml);
        assertTrue(tag.is(Tag.RcData));
    }

    private void testTagIsNotRcData(TagSet tags, String tagName) {
        Tag tag = tags.valueOf(tagName, NamespaceHtml);
        assertFalse(tag.is(Tag.RcData));
    }

    private void testTagIsVoid(TagSet tags, String tagName) {
        Tag tag = tags.valueOf(tagName, NamespaceHtml);
        assertTrue(tag.is(Tag.Void));
    }

    private void testTagIsNotVoid(TagSet tags, String tagName) {
        Tag tag = tags.valueOf(tagName, NamespaceHtml);
        assertFalse(tag.is(Tag.Void));
    }
}