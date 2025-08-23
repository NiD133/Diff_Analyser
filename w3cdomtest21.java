package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.w3c.dom.NodeList;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link W3CDom}, focusing on conversion security and namespace handling.
 */
public class W3CDomTest {

    /**
     * Provides HTML and XML parsers for parameterized tests.
     */
    private static Stream<Arguments> parserProvider() {
        return Stream.of(
            Arguments.of(Parser.htmlParser()),
            Arguments.of(Parser.xmlParser())
        );
    }

    @DisplayName("Conversion should not expand XML entities to prevent Billion Laughs / XXE attacks")
    @ParameterizedTest(name = "with {0}")
    @MethodSource("parserProvider")
    void doesNotExpandEntities(Parser parser) {
        // Arrange: An XML string with a "Billion Laughs" attack vector.
        // Jsoup does not parse entities within the doctype, so it is not vulnerable. This test confirms
        // that this safe behavior is preserved when converting to a W3C DOM.
        final String billionLaughsXml =
            "<?xml version=\"1.0\"?>\n" +
            "<!DOCTYPE lolz [\n" +
            " <!ENTITY lol \"lol\">\n" +
            " <!ENTITY lol1 \"&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;\">\n" +
            "]>\n" +
            "<html><body><p>&lol1;</p></body></html>";
        final W3CDom w3cDom = new W3CDom();

        // Act
        final org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(billionLaughsXml, parser);
        final org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Assert
        assertNotNull(w3cDoc);

        // 1. Verify the W3C DOM node contains the unexpanded entity name.
        final NodeList pElements = w3cDoc.getElementsByTagName("p");
        assertEquals(1, pElements.getLength());
        assertEquals("&lol1;", pElements.item(0).getTextContent());

        // 2. Verify the serialized string also contains the unexpanded, escaped entity.
        final String outputXml = W3CDom.asString(w3cDoc, W3CDom.OutputXml());
        assertFalse(outputXml.contains("lololol"), "Entity should not be expanded.");
        assertTrue(outputXml.contains("&amp;lol1;"), "Entity should be escaped in output.");
    }

    @DisplayName("Handles attributes with undeclared namespace prefixes gracefully")
    @Test
    void handlesUndeclaredNamespacePrefixesInHtml() {
        // Arrange: HTML with attributes that use a colon (e.g., v-bind), which resembles a namespace but is not declared.
        final String htmlWithUndeclaredPrefix = "<html><body><div v-bind:class='test'><span v-bind:style='color:red'></span></div></body></html>";
        final W3CDom w3cDom = new W3CDom();

        // The converter should add a dummy namespace declaration (`xmlns:v-bind="undefined"`) to create valid XML.
        // It also adds the default HTML namespace and a <head> element, as per standard HTML parsing.
        final String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<html xmlns=\"http://www.w3.org/1999/xhtml\">" +
            "<head/>" +
            "<body>" +
            "<div xmlns:v-bind=\"undefined\" v-bind:class=\"test\">" +
            "<span v-bind:style=\"color:red\"/>" +
            "</div>" +
            "</body>" +
            "</html>";

        // Act
        final org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(htmlWithUndeclaredPrefix);
        final org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        final String actualXml = w3cDom.asString(w3cDoc);

        // Assert
        assertEquals(expectedXml, actualXml);
    }
}