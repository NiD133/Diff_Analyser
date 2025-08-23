package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link W3CDom} concerning security and HTML-specific conversions.
 */
@DisplayName("W3CDom Conversion")
public class W3CDomTest {

    private static Stream<Arguments> parserProvider() {
        return Stream.of(
            Arguments.of(Parser.htmlParser()),
            Arguments.of(Parser.xmlParser())
        );
    }

    @ParameterizedTest
    @MethodSource("parserProvider")
    @DisplayName("Should not expand XML entities to prevent billion laughs attack")
    void shouldNotExpandEntities(Parser parser) {
        // Arrange
        // This XML contains a "billion laughs" entity attack.
        String billionLaughsXml = """
            <?xml version="1.0"?>
            <!DOCTYPE lolz [
             <!ENTITY lol "lol">
             <!ENTITY lol1 "&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;">
            ]>
            <html><body><p>&lol1;</p></body></html>""";

        // Act
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(billionLaughsXml, parser);
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        String xmlOutput = W3CDom.asString(w3cDoc, W3CDom.OutputXml());

        // Assert
        // 1. Verify the DOM text content is the unexpanded entity name.
        NodeList p = w3cDoc.getElementsByTagName("p");
        assertEquals(1, p.getLength(), "There should be one <p> element.");
        assertEquals("&lol1;", p.item(0).getTextContent(), "Entity should not be expanded in the DOM.");

        // 2. Verify the serialized string output also does not contain the expanded entity.
        assertFalse(xmlOutput.contains("lololol"), "Output should not contain the expanded entity string.");
        assertTrue(xmlOutput.contains("&amp;lol1;"), "Output should contain the escaped entity name.");
    }

    @Test
    @DisplayName("Should preserve unicode attribute names during HTML conversion")
    void shouldPreserveUnicodeAttributeNamesInHtml() {
        // Arrange
        String html = "<!DOCTYPE html><html><body><p hành=\"1\" hình=\"2\">unicode attr names</p></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);

        // Act
        Document w3cDoc = W3CDom.convert(jsoupDoc);
        String outHtml = W3CDom.asString(w3cDoc, W3CDom.OutputHtml());

        // Assert
        // 1. Verify the W3C DOM structure directly for robustness.
        // This ensures the attributes are correctly parsed into the DOM model.
        NodeList pElements = w3cDoc.getElementsByTagName("p");
        assertEquals(1, pElements.getLength(), "Should find one <p> element.");
        Node pNode = pElements.item(0);
        assertTrue(pNode instanceof Element, "Node should be an Element.");
        Element pElement = (Element) pNode;

        assertEquals("1", pElement.getAttribute("hành"), "Value of attribute 'hành' should be preserved.");
        assertEquals("2", pElement.getAttribute("hình"), "Value of attribute 'hình' should be preserved.");

        // 2. Verify the final string output contains the critical part.
        // This is a less brittle check than comparing the full string, focusing on the core requirement.
        assertTrue(outHtml.contains("<p hành=\"1\" hình=\"2\">unicode attr names</p>"),
            "Output HTML should contain the <p> tag with unicode attributes preserved.");
    }
}