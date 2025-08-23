package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.TextUtil;
import org.jsoup.nodes.Parser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link W3CDom} helper class, focusing on the conversion
 * from a Jsoup Document to a W3C DOM Document.
 */
@DisplayName("W3CDom Conversion")
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

    @DisplayName("Should not expand XML entities to prevent Billion Laughs attack")
    @ParameterizedTest(name = "with {0}")
    @MethodSource("parserProvider")
    void billionLaughsAttackIsPrevented(Parser parser) {
        // Arrange
        // This XML contains a "Billion Laughs" entity expansion attack.
        String billionLaughsXml =
            "<?xml version=\"1.0\"?>\n" +
            "<!DOCTYPE lolz [\n" +
            " <!ENTITY lol \"lol\">\n" +
            " <!ENTITY lol1 \"&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;\">\n" +
            "]>\n" +
            "<html><body><p>&lol1;</p></body></html>";

        // Act
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(billionLaughsXml, parser);
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

        // Assert
        // 1. Verify the DOM text content is not expanded.
        NodeList pElements = w3cDoc.getElementsByTagName("p");
        assertEquals(1, pElements.getLength());
        assertEquals("&lol1;", pElements.item(0).getTextContent());

        // 2. Verify the serialized string output does not contain the expanded text
        // and that the entity reference itself is correctly escaped.
        String actualXml = W3CDom.asString(w3cDoc, W3CDom.OutputXml());
        assertFalse(actualXml.contains("lololol"));
        assertTrue(actualXml.contains("&amp;lol1;"));
    }

    @Test
    @DisplayName("Converts a simple Jsoup Document to a W3C Document correctly")
    void convertsSimpleHtmlToW3cDom() {
        // Arrange
        String html = "<html><head><title>W3c</title></head><body><p class='one' id=12>Text</p><!-- comment --></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);

        // Act
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

        // Assert
        // Check root element and namespace
        assertEquals("html", w3cDoc.getDocumentElement().getTagName());
        assertEquals("http://www.w3.org/1999/xhtml", w3cDoc.getDocumentElement().getNamespaceURI());

        // Check element with attributes and text
        NodeList pElements = w3cDoc.getElementsByTagName("p");
        assertEquals(1, pElements.getLength());
        Node pNode = pElements.item(0);
        assertEquals(Node.ELEMENT_NODE, pNode.getNodeType());
        org.w3c.dom.Element pElement = (org.w3c.dom.Element) pNode;
        assertEquals("one", pElement.getAttribute("class"));
        assertEquals("12", pElement.getAttribute("id"));
        assertEquals("Text", pElement.getTextContent());

        // Check comment node
        Node comment = pNode.getNextSibling();
        assertEquals(Node.COMMENT_NODE, comment.getNodeType());
        assertEquals(" comment ", comment.getNodeValue()); // Jsoup adds spaces around comment data
    }

    @Test
    @DisplayName("Serializes a converted W3C Document to a compact XML string")
    void serializesToCompactXmlString() {
        // Arrange
        String html = "<html><head><title>W3c</title></head><body><p class='one' id=12>Text</p><!-- comment --><invalid>What<script>alert('!')</script></invalid></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

        String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<html xmlns=\"http://www.w3.org/1999/xhtml\">" +
            "<head><title>W3c</title></head>" +
            "<body>" +
            "<p class=\"one\" id=\"12\">Text</p>" +
            "<!-- comment -->" +
            "<invalid>What<script>alert('!')</script></invalid>" +
            "</body>" +
            "</html>";

        // Act
        String actualXml = W3CDom.asString(w3cDoc, W3CDom.OutputXml());

        // Assert
        // Compare without newlines, as different platforms may use different line endings.
        assertEquals(expectedXml, TextUtil.stripNewlines(actualXml));
    }

    @Test
    @DisplayName("Serializes a converted W3C Document with custom output properties")
    void serializesWithCustomProperties() {
        // Arrange
        String html = "<html><head><title>W3c</title></head><body><p class='one' id=12>Text</p></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

        String compactXml = W3CDom.asString(w3cDoc, W3CDom.OutputXml());

        // Act
        Map<String, String> properties = W3CDom.OutputXml();
        properties.put(OutputKeys.INDENT, "yes");
        String indentedXml = W3CDom.asString(w3cDoc, properties);

        // Assert
        assertTrue(indentedXml.length() > compactXml.length(), "Indented XML should be longer than compact XML");
        // Verify content is identical, ignoring platform-specific indentation and newlines
        assertEquals(TextUtil.stripNewlines(compactXml), TextUtil.stripNewlines(indentedXml));
    }

    @Test
    @DisplayName("Round-trip conversion (Jsoup -> W3C -> XML -> W3C) preserves content")
    void roundTripConversionPreservesContent() {
        // Arrange
        String html = "<html><head><title>W3c</title></head><body><p>Text</p></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);

        // Act
        // 1. Jsoup -> W3C -> XML String
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        String xmlOutput = W3CDom.asString(w3cDoc, W3CDom.OutputXml());

        // 2. XML String -> W3C Document
        Document roundTrippedDoc = parseXml(xmlOutput, true);

        // Assert
        NodeList pElements = roundTrippedDoc.getElementsByTagName("p");
        assertEquals(1, pElements.getLength());
        assertEquals("Text", pElements.item(0).getTextContent());
    }

    /**
     * Helper to parse an XML string into a W3C Document for verification purposes.
     * Throws an IllegalStateException on parsing errors to simplify test code.
     */
    private Document parseXml(String xml, boolean nameSpaceAware) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(nameSpaceAware);
            DocumentBuilder builder = factory.newDocumentBuilder();
            // Suppress resolution of external entities, e.g., from <!DOCTYPE>
            builder.setEntityResolver((publicId, systemId) -> {
                if (systemId.contains("about:legacy-compat")) { // From <!doctype html>
                    return new InputSource(new StringReader(""));
                }
                return null;
            });
            Document dom = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
            dom.normalizeDocument();
            return dom;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to parse XML for test verification", e);
        }
    }
}