package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link W3CDom} conversion.
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

    @ParameterizedTest
    @MethodSource("parserProvider")
    void doesNotExpandEntitiesInBillionLaughsAttack(Parser parser) {
        // Arrange
        // A "Billion Laughs" XML document to test for entity expansion vulnerabilities.
        // Jsoup does not parse entities within the doctype, so it is not directly vulnerable.
        // This test confirms this behavior is maintained when converting to a W3C DOM.
        String billionLaughsXml =
            "<?xml version=\"1.0\"?>\n" +
            "<!DOCTYPE lolz [\n" +
            " <!ENTITY lol \"lol\">\n" +
            " <!ENTITY lol1 \"&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;\">\n" +
            "]>\n" +
            "<html><body><p>&lol1;</p></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(billionLaughsXml, parser);
        W3CDom w3cDom = new W3CDom();

        // Act
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Assert
        // 1. Verify the <p> element's content is the unexpanded entity.
        NodeList pElements = w3cDoc.getElementsByTagName("p");
        assertEquals(1, pElements.getLength());
        assertEquals("&lol1;", pElements.item(0).getTextContent());

        // 2. Verify the full string output also contains the unexpanded, escaped entity.
        String outputString = W3CDom.asString(w3cDoc, W3CDom.OutputXml());
        assertFalse(outputString.contains("lololol"), "Entity should not be expanded");
        assertTrue(outputString.contains("&amp;lol1;"), "Entity should be present but escaped");
    }

    @Test
    void attributesWithUndeclaredNamespacesArePreservedAsString() {
        // Arrange
        // This test addresses a bug where attributes with undeclared namespaces (e.g., v-bind:class)
        // were not handled correctly. See https://github.com/jhy/jsoup/issues/2087
        String htmlWithUndeclaredNamespace = "<html><body><div v-bind:class='test'></div></body></html>";
        String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                             "<html xmlns=\"http://www.w3.org/1999/xhtml\">" +
                             "<head/>" +
                             "<body>" +
                             "<div xmlns:v-bind=\"undefined\" v-bind:class=\"test\"/>" +
                             "</body>" +
                             "</html>";

        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(htmlWithUndeclaredNamespace);
        W3CDom w3CDom = new W3CDom();

        // Act
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        String actualXml = w3CDom.asString(w3cDoc);

        // Assert
        assertEquals(expectedXml, actualXml);
    }
}