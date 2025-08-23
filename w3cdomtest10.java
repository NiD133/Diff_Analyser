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
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for W3CDom focusing on security (entity expansion) and XML namespace handling.
 */
public class W3CDomSecurityAndNamespaceTest {

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
     * Verifies that the W3C converter does not expand XML entities, protecting against "Billion Laughs" style attacks.
     * Jsoup itself does not parse entities in the doctype, so this test confirms that behavior is maintained
     * through the W3C conversion process.
     *
     * @param parser The Jsoup parser to use (HTML or XML).
     */
    @ParameterizedTest
    @MethodSource("parserProvider")
    @DisplayName("Billion Laughs attack should not cause entity expansion")
    void billionLaughsAttackDoesNotCauseEntityExpansion(Parser parser) {
        // Arrange
        String billionLaughsXml =
            "<?xml version=\"1.0\"?>\n" +
            "<!DOCTYPE lolz [\n" +
            " <!ENTITY lol \"lol\">\n" +
            " <!ENTITY lol1 \"&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;\">\n" +
            "]>\n" +
            "<html><body><p>&lol1;</p></body></html>";

        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(billionLaughsXml, parser);

        // Act
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        String resultString = W3CDom.asString(w3cDoc, W3CDom.OutputXml());
        NodeList pElements = w3cDoc.getElementsByTagName("p");

        // Assert
        assertNotNull(w3cDoc);
        assertEquals(1, pElements.getLength());
        // Check that the entity is not expanded in the DOM text content
        assertEquals("&lol1;", pElements.item(0).getTextContent());
        // Check that the serialized string contains the escaped entity, not the expanded text
        assertFalse(resultString.contains("lololol"));
        assertTrue(resultString.contains("&amp;lol1;"));
    }

    /**
     * Tests that an XPath query using `local-name()` can find an element in a document
     * that has a default HTML namespace.
     */
    @Test
    @DisplayName("XPath with local-name() should find element in default HTML namespace")
    void xpathWithLocalNameFindsElementInDefaultNamespace() throws XPathExpressionException {
        // Arrange
        String html = "<html><body><div>hello</div></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

        // Act
        NodeList nodeList = xpath(w3cDoc, "//*[local-name()='body']");

        // Assert
        assertEquals(1, nodeList.getLength());
        Node body = nodeList.item(0);
        assertEquals("body", body.getLocalName());
        assertEquals("http://www.w3.org/1999/xhtml", body.getNamespaceURI());
    }

    /**
     * Verifies that a simple XPath query (e.g., "//body") fails to find an element
     * when the document has an explicit namespace and the query is not namespace-aware.
     */
    @Test
    @DisplayName("XPath without namespace should fail on an explicitly namespaced document")
    void xpathWithoutNamespaceFailsOnExplicitlyNamespacedDocument() throws XPathExpressionException {
        // Arrange
        String html = "<html xmlns='http://www.w3.org/1999/xhtml'><body id='One'><div>hello</div></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

        // Act
        NodeList nodeList = xpath(w3cDoc, "//body");

        // Assert
        assertEquals(0, nodeList.getLength());
    }

    /**
     * Confirms that an XPath query using `local-name()` successfully finds an element
     * in a document with an explicit namespace declaration.
     */
    @Test
    @DisplayName("XPath with local-name() should find element in an explicit namespace")
    void xpathWithLocalNameFindsElementInExplicitNamespace() throws XPathExpressionException {
        // Arrange
        String html = "<html xmlns='http://www.w3.org/1999/xhtml'><body id='One'><div>hello</div></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

        // Act
        NodeList nodeList = xpath(w3cDoc, "//*[local-name()='body']");

        // Assert
        assertEquals(1, nodeList.getLength());
        Node body = nodeList.item(0);
        assertEquals("body", body.getLocalName());
        assertEquals("http://www.w3.org/1999/xhtml", body.getNamespaceURI());
        assertNull(body.getPrefix());
    }

    /**
     * Tests that after serializing a namespaced document and re-parsing it as
     * namespace-unaware, a simple XPath query can find elements.
     */
    @Test
    @DisplayName("XPath should work on a serialized document parsed as namespace-unaware")
    void xpathWorksOnSerializedDocParsedAsNamespaceUnaware() throws XPathExpressionException {
        // Arrange
        String html = "<html xmlns='http://www.w3.org/1999/xhtml'><body><div>hello</div></body></html>";
        Document w3cDocWithNs = new W3CDom().fromJsoup(Jsoup.parse(html));
        String xmlString = W3CDom.asString(w3cDocWithNs);
        Document w3cDocWithoutNs = parseXml(xmlString, false); // Parse as namespace-unaware

        // Act
        NodeList nodeList = xpath(w3cDocWithoutNs, "//body");

        // Assert
        assertEquals(1, nodeList.getLength());
        Node body = nodeList.item(0);
        assertEquals("body", body.getNodeName());
        assertNull(body.getNamespaceURI());
        assertNull(body.getPrefix());
    }

    /**
     * Verifies that if a serialized document (where xmlns is just an attribute) is
     * re-parsed with a namespace-aware parser, a simple XPath query will not find
     * the namespaced element.
     */
    @Test
    @DisplayName("XPath should fail on a serialized document re-parsed as namespace-aware")
    void xpathFailsOnSerializedDocParsedAsNamespaceAware() throws XPathExpressionException {
        // Arrange
        String html = "<html xmlns='http://www.w3.org/1999/xhtml'><body><div>hello</div></body></html>";
        Document w3cDocWithNs = new W3CDom().fromJsoup(Jsoup.parse(html));
        String xmlString = W3CDom.asString(w3cDocWithNs);
        Document w3cDocAware = parseXml(xmlString, true); // Re-parse as namespace-aware

        // Act
        NodeList nodeList = xpath(w3cDocAware, "//body");

        // Assert
        assertEquals(0, nodeList.getLength());
    }

    // --- HELPER METHODS ---

    /**
     * Executes an XPath query on a W3C Document and returns the resulting nodes.
     *
     * @param w3cDoc The W3C Document to query.
     * @param query  The XPath query string.
     * @return A NodeList containing the matched nodes.
     */
    private NodeList xpath(Document w3cDoc, String query) throws XPathExpressionException {
        XPathExpression xpath = XPathFactory.newInstance().newXPath().compile(query);
        // Use NODESET to get a list of matching nodes.
        return (NodeList) xpath.evaluate(w3cDoc, XPathConstants.NODESET);
    }

    /**
     * Parses an XML string into a W3C Document, with an option to control namespace awareness.
     *
     * @param xml            The XML string to parse.
     * @param namespaceAware True to enable namespace awareness, false otherwise.
     * @return The parsed W3C Document.
     */
    private static Document parseXml(String xml, boolean namespaceAware) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(namespaceAware);
            DocumentBuilder builder = factory.newDocumentBuilder();
            // Suppress DTD validation for legacy compatibility doctypes
            builder.setEntityResolver((publicId, systemId) -> {
                if (systemId != null && systemId.contains("about:legacy-compat")) {
                    return new InputSource(new StringReader(""));
                } else {
                    return null;
                }
            });
            Document dom = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
            dom.normalizeDocument();
            return dom;
        } catch (Exception e) {
            throw new IllegalStateException("Error parsing XML", e);
        }
    }
}