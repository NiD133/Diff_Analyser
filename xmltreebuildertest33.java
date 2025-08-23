package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import static org.jsoup.parser.Parser.NamespaceXml;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link XmlTreeBuilder}, focusing on namespace handling.
 */
class XmlTreeBuilderTest {

    /**
     * Asserts that a given element is in the XML namespace.
     * @param el The element to check.
     */
    private static void assertInXmlNamespace(Element el) {
        assertEquals(NamespaceXml, el.tag().namespace(),
            () -> "Element <" + el.tagName() + "> should be in the XML namespace");
    }

    /**
     * Verifies that the XmlParser correctly assigns the XML namespace to all parsed elements
     * and that this namespace is preserved when the document is cloned.
     */
    @Test
    void xmlParserAssignsCorrectNamespaceAndPreservesItOnClone() {
        // Arrange: A string of XML with several nested tags.
        // The unclosed tags are intentional to test the parser's handling.
        String xml = "<foo><bar><div><svg><math>Qux</bar></foo>";

        // Act: Parse the XML string using the XML parser.
        Document doc = Jsoup.parse(xml, "", Parser.xmlParser());

        // Assert: Verify all elements in the original parsed document have the XML namespace.
        assertInXmlNamespace(doc);
        Elements allElements = doc.select("*");
        for (Element element : allElements) {
            assertInXmlNamespace(element);
        }

        // Act: Create both a deep and a shallow clone of the document.
        Document deepClone = doc.clone();
        Document shallowClone = doc.shallowClone();

        // Assert: Verify that both deep and shallow clones preserve the XML namespace.
        assertInXmlNamespace(deepClone);
        assertInXmlNamespace(deepClone.expectFirst("bar")); // Check a child in the deep clone
        assertInXmlNamespace(shallowClone); // The root element of the shallow clone
    }
}