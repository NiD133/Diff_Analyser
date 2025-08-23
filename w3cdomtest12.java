package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for {@link W3CDom}.
 * Focuses on security aspects and configuration options like namespace awareness.
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

    @DisplayName("Should not expand XML entities to prevent Billion Laughs attack")
    @ParameterizedTest(name = "with {0}")
    @MethodSource("parserProvider")
    void billionLaughsAttackDoesNotExpandEntities(Parser parser) {
        // Arrange
        // This XML contains a "Billion Laughs" entity attack. Jsoup does not parse entities in the DTD,
        // so it is not vulnerable. This test confirms that behavior is preserved when converting to a W3C DOM.
        String billionLaughsXml =
            "<?xml version=\"1.0\"?>" +
            "<!DOCTYPE lolz [" +
            " <!ENTITY lol \"lol\">" +
            " <!ENTITY lol1 \"&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;\">" +
            "]>" +
            "<html><body><p>&lol1;</p></body></html>";

        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(billionLaughsXml, parser);
        W3CDom w3cDom = new W3CDom();

        // Act
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        NodeList pElements = w3cDoc.getElementsByTagName("p");
        Node pElement = pElements.item(0);
        String textContent = pElement.getTextContent();
        String xmlOutput = W3CDom.asString(w3cDoc, W3CDom.OutputXml());

        // Assert
        assertNotNull(w3cDoc, "The converted W3C document should not be null.");
        assertEquals(1, pElements.getLength(), "Should find one 'p' element.");

        // Crucially, the entity should not be expanded in the DOM text content.
        assertEquals("&lol1;", textContent, "The entity should not be expanded in the text content.");

        // Also, check the serialized output for good measure.
        assertFalse(xmlOutput.contains("lololol"), "Serialized output should not contain the expanded entity.");
        assertTrue(xmlOutput.contains("&amp;lol1;"), "Serialized output should contain the escaped entity reference.");
    }

    @DisplayName("Disabling namespace awareness allows selecting elements without namespace prefixes")
    @Test
    void canDisableNamespaceAwarenessForSimpleXPath() {
        // Arrange
        W3CDom w3cDom = new W3CDom();
        assertTrue(w3cDom.namespaceAware(), "Namespace awareness should be enabled by default.");

        String htmlWithNamespace = "<html xmlns='http://www.w3.org/1999/xhtml'><body id='One'><div>hello</div></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(htmlWithNamespace);

        // Act: Disable namespace awareness and convert the document
        w3cDom.namespaceAware(false);
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Assert
        assertFalse(w3cDom.namespaceAware(), "Namespace awareness should now be disabled.");

        // With namespace awareness disabled, a simple XPath query should find the element.
        NodeList bodyNodes = assertDoesNotThrow(
            () -> executeXPath(w3cDoc, "//body"),
            "XPath execution should not throw an exception."
        );

        assertEquals(1, bodyNodes.getLength(), "Should find exactly one 'body' element.");
        Node bodyElement = bodyNodes.item(0);
        assertEquals("body", bodyElement.getLocalName(), "The local name of the found element should be 'body'.");
    }

    /**
     * Executes an XPath query on a W3C Document and returns the resulting nodes.
     * @param w3cDoc The W3C Document to query.
     * @param query The XPath query string.
     * @return A NodeList containing the matched nodes.
     * @throws XPathExpressionException if the query is invalid.
     */
    private NodeList executeXPath(Document w3cDoc, String query) throws XPathExpressionException {
        XPathExpression xpath = XPathFactory.newInstance().newXPath().compile(query);
        // Use NODESET to get a list of nodes, not just the first one.
        return (NodeList) xpath.evaluate(w3cDoc, XPathConstants.NODESET);
    }
}