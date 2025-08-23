package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document.OutputSettings.Syntax;
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

@DisplayName("W3CDom Functionality Tests")
public class W3CDomTest {

    private static Stream<Arguments> parserProvider() {
        return Stream.of(
            Arguments.of(Parser.htmlParser()),
            Arguments.of(Parser.xmlParser())
        );
    }

    @DisplayName("Should not expand XML entities to prevent Billion Laughs / XXE attacks")
    @ParameterizedTest(name = "with {0}")
    @MethodSource("parserProvider")
    void billionLaughsAttackDoesNotExpandEntities(Parser parser) {
        // Arrange
        // This test ensures jsoup does not expand entities within the doctype,
        // which is a safeguard against "billion laughs" and XXE attacks.
        // The W3C converter should preserve this safe behavior.
        String billionLaughsXml = """
            <?xml version="1.0"?>
            <!DOCTYPE lolz [
             <!ENTITY lol "lol">
             <!ENTITY lol1 "&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;">
            ]>
            <html><body><p>&lol1;</p></body></html>""";

        // Act
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(billionLaughsXml, parser);
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Assert
        NodeList pElements = w3cDoc.getElementsByTagName("p");
        assertEquals(1, pElements.getLength(), "Should find one 'p' element");

        Node pElement = pElements.item(0);
        assertEquals("&lol1;", pElement.getTextContent(), "Entity in text content should not be expanded");

        // Also verify the serialized output
        String outputXml = W3CDom.asString(w3cDoc, W3CDom.OutputXml());
        assertFalse(outputXml.contains("lololol"), "Output string should not contain the expanded entity");
        assertTrue(outputXml.contains("&amp;lol1;"), "Output string should contain the escaped, unexpanded entity");
    }

    @Test
    @DisplayName("fromJsoup should allow simple XPath selection when namespace awareness is disabled")
    void fromJsoup_withNamespaceAwareFalse_allowsSimpleXpath() throws XPathExpressionException {
        // Arrange
        String html = "<html><body><div>hello</div></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);

        W3CDom w3c = new W3CDom();
        w3c.namespaceAware(false); // Disable namespace awareness for the conversion

        // Act
        Document w3cDoc = w3c.fromJsoup(jsoupDoc);
        NodeList bodyNodes = xpath(w3cDoc, "//body"); // Select the body element

        // Assert
        assertEquals(1, bodyNodes.getLength(), "Should find exactly one body element");
        Node bodyElement = bodyNodes.item(0);

        // The original test asserted "div", which was incorrect for an XPath of "//body".
        // The local name of the selected <body> element should be "body".
        assertEquals("body", bodyElement.getLocalName(), "Local name of the element should be 'body'");

        // Verify the child node as well, which might have been the original intent.
        assertNotNull(bodyElement.getFirstChild(), "Body element should have a child node");
        assertEquals("div", bodyElement.getFirstChild().getLocalName(), "The first child of body should be 'div'");
    }

    /**
     * Helper to evaluate an XPath query against a W3C Document.
     *
     * @param w3cDoc The W3C Document to query.
     * @param query  The XPath query string.
     * @return A NodeList containing the matching nodes.
     * @throws XPathExpressionException if the XPath expression is invalid.
     */
    private NodeList xpath(Document w3cDoc, String query) throws XPathExpressionException {
        XPathExpression xpath = XPathFactory.newInstance().newXPath().compile(query);
        // The original implementation incorrectly used XPathConstants.NODE, which returns a single Node.
        // Using NODESET correctly returns a list of all matching nodes.
        return (NodeList) xpath.evaluate(w3cDoc, XPathConstants.NODESET);
    }
}