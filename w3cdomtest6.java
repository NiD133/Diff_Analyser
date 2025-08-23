package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests the conversion from a jsoup Document to a W3C Document,
 * focusing on security aspects and handling of edge-case HTML.
 */
public class W3CDomSecurityAndEdgeCaseTest {

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
    @ParameterizedTest(name = "[{index}] with {0}")
    @MethodSource("parserProvider")
    void doesNotExpandEntities(Parser parser) {
        // Arrange
        // This XML contains a "Billion Laughs" entity attack. Jsoup does not parse entities within the DTD,
        // so it is not vulnerable. This test confirms that behavior and protects against future regressions.
        String billionLaughsXml = """
            <?xml version="1.0"?>
            <!DOCTYPE lolz [
             <!ENTITY lol "lol">
             <!ENTITY lol1 "&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;">
            ]>
            <html><body><p>&lol1;</p></body></html>""";

        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(billionLaughsXml, parser);

        // Act
        Document w3cDoc = W3CDom.convert(jsoupDoc);

        // Assert
        assertNotNull(w3cDoc);

        // 1. Check the W3C DOM structure
        NodeList pElements = w3cDoc.getElementsByTagName("p");
        assertEquals(1, pElements.getLength(), "Should find one <p> element.");
        // The entity should remain unexpanded in the text content
        assertEquals("&lol1;", pElements.item(0).getTextContent());

        // 2. Check the serialized string output
        String outputXml = W3CDom.asString(w3cDoc, W3CDom.OutputXml());
        assertFalse(outputXml.contains("lololol"), "Entity should not be expanded in the output string.");
        assertTrue(outputXml.contains("&amp;lol1;"), "Entity reference should be correctly escaped in the output string.");
    }

    @Test
    @DisplayName("Invalid HTML tag should be treated as text during conversion")
    void invalidHtmlTagIsTreatedAsTextDuringConversion() {
        // Arrange
        // The string "<インセンティブで高収入！>" is not a valid HTML tag name.
        String htmlWithInvalidTag = "<インセンティブで高収入！>Text <p>More</p>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(htmlWithInvalidTag);

        // The expected XML output where the invalid tag is escaped as text content.
        // Formatted for readability in source code.
        String expectedXml = """
            <?xml version="1.0" encoding="UTF-8"?>\
            <html xmlns="http://www.w3.org/1999/xhtml">\
            <head/>\
            <body>&lt;インセンティブで高収入！&gt;Text <p>More</p></body>\
            </html>""";

        // Act
        Document w3cDoc = W3CDom.convert(jsoupDoc);
        String actualXml = W3CDom.asString(w3cDoc, W3CDom.OutputXml());

        // Assert
        assertEquals(expectedXml, actualXml, "Invalid tag should be escaped and treated as text.");
    }
}