package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link W3CDom} conversion, focusing on security aspects and source node mapping.
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
    void fromJsoup_shouldNotExpandXmlEntities_preventingBillionLaughs(Parser parser) {
        // ARRANGE
        // This XML contains a "Billion Laughs" entity expansion attack.
        String billionLaughsXml = "<?xml version=\"1.0\"?>\n" +
            "<!DOCTYPE lolz [\n" +
            " <!ENTITY lol \"lol\">\n" +
            " <!ENTITY lol1 \"&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;\">\n" +
            "]>\n" +
            "<html><body><p>&lol1;</p></body></html>";
        W3CDom w3cDom = new W3CDom();

        // ACT
        // Jsoup does not parse DTD entities, so they are not expanded during conversion.
        // This test confirms that behavior to prevent XXE or DoS vulnerabilities.
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(billionLaughsXml, parser);
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // ASSERT
        // 1. Check the W3C DOM text content to ensure it's not expanded.
        NodeList pElements = w3cDoc.getElementsByTagName("p");
        assertEquals(1, pElements.getLength());
        Node pElement = pElements.item(0);
        assertEquals("&lol1;", pElement.getTextContent(), "Entity should not be expanded in the W3C DOM.");

        // 2. Check the serialized string output to ensure it's correctly escaped.
        String outXml = W3CDom.asString(w3cDoc, W3CDom.OutputXml());
        assertFalse(outXml.contains("lololol"), "Expanded entity content should not be in the output string.");
        assertTrue(outXml.contains("&amp;lol1;"), "Entity reference should be correctly escaped in the output string.");
    }

    @Test
    public void fromJsoupElement_shouldPreserveSourceNodeMapping() {
        // ARRANGE
        String html = "<body><div><p>One</div><div><p>Two";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);

        Element jsoupDiv = jsoupDoc.selectFirst("div");
        assertNotNull(jsoupDiv, "Jsoup should find the first div element.");

        // The jsoup parser will create a <p> tag inside the div, containing the text "One".
        TextNode jsoupText = (TextNode) jsoupDiv.childNode(0).childNode(0);
        assertEquals("One", jsoupText.getWholeText());

        W3CDom w3cDom = new W3CDom();

        // ACT
        // Convert the Jsoup Document, with the 'div' element as the context.
        Document w3cDoc = w3cDom.fromJsoup(jsoupDiv);

        // ASSERT
        // The W3CDom#contextNode method should return the W3C node corresponding to the original Jsoup context element.
        Node w3cDiv = w3cDom.contextNode(w3cDoc);
        assertEquals("div", w3cDiv.getLocalName());
        assertEquals(jsoupDiv, w3cDiv.getUserData(W3CDom.SourceProperty),
            "W3C div should map back to the original Jsoup div.");

        // Verify the child text node is also converted and mapped correctly.
        Node w3cParagraph = w3cDiv.getFirstChild(); // This is the <p> element
        Node w3cText = w3cParagraph.getFirstChild(); // This is the text node "One"

        assertEquals(Node.TEXT_NODE, w3cText.getNodeType());
        assertEquals("One", w3cText.getTextContent());
        assertEquals(jsoupText, w3cText.getUserData(W3CDom.SourceProperty),
            "W3C text node should map back to the original Jsoup text node.");
    }
}