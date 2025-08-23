package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.w3c.dom.NodeList;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for W3CDom conversion, focusing on security aspects and namespace handling.
 */
public class W3CDomSecurityAndNamespaceTest {

    private static Stream<Arguments> parserProvider() {
        return Stream.of(
            Arguments.of(Parser.htmlParser()),
            Arguments.of(Parser.xmlParser())
        );
    }

    @ParameterizedTest
    @MethodSource("parserProvider")
    void doesNotExpandEntities(Parser parser) {
        // Arrange
        // Tests against the "billion laughs" XML bomb and XXE. Jsoup does not parse entities within the DTD,
        // so they are not expanded during W3C conversion. This test confirms that behavior.
        String billionLaughs = "<?xml version=\"1.0\"?>\n" +
            "<!DOCTYPE lolz [\n" +
            " <!ENTITY lol \"lol\">\n" +
            " <!ENTITY lol1 \"&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;\">\n" +
            "]>\n" +
            "<html><body><p>&lol1;</p></body></html>";

        Document jsoupDoc = Jsoup.parse(billionLaughs, parser);
        W3CDom w3cDom = new W3CDom();

        // Act
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Assert
        assertNotNull(w3cDoc);

        // 1. Check the DOM content to ensure the entity is not expanded.
        NodeList pElements = w3cDoc.getElementsByTagName("p");
        assertEquals(1, pElements.getLength());
        assertEquals("&lol1;", pElements.item(0).getTextContent());

        // 2. Check the serialized string output to be certain.
        String outputXml = W3CDom.asString(w3cDoc, W3CDom.OutputXml());
        assertFalse(outputXml.contains("lololol"));
        // The text content is the literal string "&lol1;". When serialized to XML, the ampersand
        // is escaped to "&amp;", so we expect to find "&amp;lol1;".
        assertTrue(outputXml.contains("&amp;lol1;"));
    }

    @Test
    void customNamespaceIsPreservedDuringConversionAndSerialization() {
        // Arrange
        W3CDom w3cDom = new W3CDom();
        String htmlWithCustomNamespace = "<html xmlns:v-bind=\"http://example.com\"><body><div v-bind:class='test'></div></body></html>";
        Document jsoupDoc = Jsoup.parse(htmlWithCustomNamespace);

        String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v-bind=\"http://example.com\">" +
            "<head/>" +
            "<body>" +
            "<div v-bind:class=\"test\"/>" +
            "</body>" +
            "</html>";

        // Act
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        String actualXml = w3cDom.asString(w3cDoc);

        // Assert
        assertEquals(expectedXml, actualXml);
    }
}