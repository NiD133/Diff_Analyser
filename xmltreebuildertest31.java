package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Tests for the {@link XmlTreeBuilder}.
 * This test focuses on the caching and reuse of Tag objects.
 */
public class XmlTreeBuilderTest {

    @Test
    @DisplayName("XML parser should reuse Tag objects for identical tag names (Flyweight pattern)")
    void xmlParserReusesTagInstancesForSameTagName() {
        // Arrange: Create an XML string with duplicate tags, differing only by case.
        String xml = "<foo>Foo</foo><foo>Foo</foo><FOO>FOO</FOO><FOO>FOO</FOO>";

        // Act: Parse the XML and retrieve the Tag object for each element.
        Document doc = Jsoup.parse(xml, Parser.xmlParser());
        Elements elements = doc.children();

        Tag firstFooTag = elements.get(0).tag();
        Tag secondFooTag = elements.get(1).tag();
        Tag firstUpperFooTag = elements.get(2).tag();
        Tag secondUpperFooTag = elements.get(3).tag();

        // Assert: Verify that for each distinct tag name, the same Tag object instance is used.
        // This confirms the Flyweight pattern is being used to conserve memory.

        // First, confirm we have the right tags and that they are case-sensitive.
        assertEquals("foo", firstFooTag.getName());
        assertEquals("FOO", firstUpperFooTag.getName());

        // Now, assert that identical tags point to the same object instance.
        assertSame(firstFooTag, secondFooTag,
            "Tag instances for lowercase 'foo' should be the same object.");
        assertSame(firstUpperFooTag, secondUpperFooTag,
            "Tag instances for uppercase 'FOO' should be the same object.");
    }
}