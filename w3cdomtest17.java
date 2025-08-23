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

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for edge cases and security aspects of the W3CDom converter.
 */
@DisplayName("W3CDom Conversion")
public class W3CDomTest {

    private static Stream<Arguments> parserProvider() {
        return Stream.of(
            Arguments.of(Parser.htmlParser()),
            Arguments.of(Parser.xmlParser())
        );
    }

    @DisplayName("should not expand entities, preventing Billion Laughs and XXE attacks")
    @ParameterizedTest(name = "with {0}")
    @MethodSource("parserProvider")
    void preventsEntityExpansionAttacks(Parser parser) {
        // Arrange
        // This test ensures that XML entities, which can be used in Billion Laughs or XXE attacks,
        // are not expanded during the conversion to a W3C DOM. Jsoup itself does not parse
        // entities in the DOCTYPE, so this test confirms that behavior is preserved.
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
        assertNotNull(w3cDoc);

        // 1. Verify the DOM node's text content contains the unexpanded entity.
        NodeList pElements = w3cDoc.getElementsByTagName("p");
        assertEquals(1, pElements.getLength(), "Should find one <p> element");
        assertEquals("&lol1;", pElements.item(0).getTextContent(), "Entity should not be expanded in the DOM");

        // 2. Verify the serialized output contains the escaped, unexpanded entity.
        String outputXml = W3CDom.asString(w3cDoc, W3CDom.OutputXml());
        assertFalse(outputXml.contains("lololol"), "Output should not contain the expanded entity");
        assertTrue(outputXml.contains("&amp;lol1;"), "Output should contain the escaped, unexpanded entity");
    }

    @DisplayName("should handle an empty DOCTYPE without errors")
    @Test
    void handlesEmptyDoctype() {
        // Arrange
        String htmlWithEmptyDoctype = "<!doctype>Foo";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(htmlWithEmptyDoctype);
        W3CDom w3cDom = new W3CDom();

        // Act
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Assert
        assertNull(w3cDoc.getDoctype(), "W3C document should have no doctype from an empty one");
        assertEquals("Foo", w3cDoc.getFirstChild().getTextContent(), "Text content following the doctype should be preserved");
    }
}