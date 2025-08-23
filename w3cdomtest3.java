package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for security and edge-case handling in the W3CDom converter.
 */
public class W3CDomSecurityTest {

    /**
     * Provides HTML and XML parsers for parameterized tests.
     */
    private static Stream<Arguments> parserProvider() {
        return Stream.of(
            Arguments.of(Parser.htmlParser()),
            Arguments.of(Parser.xmlParser())
        );
    }

    /**
     * Verifies that W3CDom does not expand XML entities, protecting against "Billion Laughs" style attacks.
     * This is a safeguard test. Jsoup's parser does not process DTD entities, and this test confirms
     * that behavior is preserved through the W3C conversion.
     *
     * @param parser The Jsoup parser (HTML or XML) to use.
     */
    @ParameterizedTest
    @MethodSource("parserProvider")
    void doesNotExpandEntities(Parser parser) {
        // Arrange
        // The "Billion Laughs" attack payload defines a recursive entity.
        String billionLaughsXml =
            "<?xml version=\"1.0\"?>" +
            "<!DOCTYPE lolz [" +
            " <!ENTITY lol \"lol\">" +
            " <!ENTITY lol1 \"&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;\">" +
            "]>" +
            "<html><body><p>&lol1;</p></body></html>";

        // Act
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(billionLaughsXml, parser);
        Document w3cDoc = W3CDom.convert(jsoupDoc);
        String outputString = W3CDom.asString(w3cDoc, W3CDom.OutputXml());

        // Assert
        // 1. Verify the W3C DOM structure reflects the unexpanded entity.
        NodeList pElements = w3cDoc.getElementsByTagName("p");
        assertEquals(1, pElements.getLength(), "Should find exactly one <p> element.");
        assertEquals("&lol1;", pElements.item(0).getTextContent(), "Entity in <p> tag should not be expanded in the DOM.");

        // 2. Verify the serialized string output also shows the entity was not expanded.
        assertFalse(outputString.contains("lololol"), "Output string should not contain the expanded entity payload.");
        assertTrue(outputString.contains("&amp;lol1;"), "Output string should contain the escaped, unexpanded entity.");
    }

    /**
     * Verifies that attribute names that are valid in HTML5 but invalid in XML
     * (e.g., containing quotes) are sanitized during the conversion to a W3C DOM.
     * The W3C DOM XML standards are stricter than HTML5 regarding attribute name characters.
     */
    @Test
    void handlesInvalidAttributeNames() {
        // Arrange
        // This HTML has attributes `"` and `name"` which are valid in HTML5 but not in XML.
        String htmlWithInvalidXmlAttrs = "<html><head></head><body style=\"color: red\" \" name\"></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(htmlWithInvalidXmlAttrs);
        Element body = jsoupDoc.selectFirst("body");

        // Sanity check that Jsoup parsed the attributes as expected before conversion.
        assertTrue(body.hasAttr("\""), "Jsoup should correctly parse the '\"' attribute.");
        assertTrue(body.hasAttr("name\""), "Jsoup should correctly parse the 'name\"' attribute.");

        // Act
        Document w3cDoc = W3CDom.convert(jsoupDoc);
        String outputXml = W3CDom.asString(w3cDoc, W3CDom.OutputXml());

        // Assert
        // The invalid attributes `"` and `name"` are expected to be sanitized to `_` and `name_` respectively.
        String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                             "<html xmlns=\"http://www.w3.org/1999/xhtml\">" +
                             "<head/>" +
                             "<body _=\"\" name_=\"\" style=\"color: red\"/>" +
                             "</html>";
        assertEquals(expectedXml, outputXml, "Invalid XML attribute names should be sanitized on conversion.");
    }
}