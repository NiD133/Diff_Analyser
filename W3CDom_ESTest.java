package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.imageio.metadata.IIOMetadataNode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * A comprehensive test suite for the {@link W3CDom} class, focusing on clarity,
 * correctness, and testing the public API contract.
 */
public class W3CDomTest {

    // --- Conversion Tests (fromJsoup, convert) ---

    @Test
    public void convertsSimpleJsoupDocumentToW3cDom() {
        // Arrange
        String html = "<html><head><title>Hello</title></head><body><p>There</p></body></html>";
        Document jsoupDoc = Jsoup.parse(html);
        W3CDom w3cDom = new W3CDom();

        // Act
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Assert
        assertNotNull(w3cDoc);
        assertEquals("html", w3cDoc.getDocumentElement().getTagName());
        assertEquals("Hello", w3cDoc.getElementsByTagName("title").item(0).getTextContent());
        assertEquals("p", w3cDoc.getElementsByTagName("p").item(0).getTagName());
        assertEquals("There", w3cDoc.getElementsByTagName("p").item(0).getTextContent());
    }

    @Test
    public void canBeConvertedToAndFromString() {
        // Arrange
        String html = "<html><head><title>W3C</title></head><body><p class='foo'>Bar</p></body></html>";
        Document jsoupDoc = Jsoup.parse(html);
        W3CDom w3cDom = new W3CDom();

        // Act
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        String out = w3cDom.asString(w3cDoc);

        // Assert
        // The exact output can be brittle, so we check for key parts.
        // By default, it includes an XML declaration and XHTML namespace.
        assertTrue(out.contains("<?xml"));
        assertTrue(out.contains("xmlns=\"http://www.w3.org/1999/xhtml\""));
        assertTrue(out.contains("<title>W3C</title>"));
        assertTrue(out.contains("<p class=\"foo\">Bar</p>"));
    }

    @Test(expected = DOMException.class)
    public void conversionFailsForInvalidTagNames() {
        // Arrange
        Document jsoupDoc = Jsoup.parse("<body><invalid<tag>text</invalid<tag></body>");
        W3CDom w3cDom = new W3CDom();

        // Act
        w3cDom.fromJsoup(jsoupDoc);
    }

    @Test(expected = StackOverflowError.class)
    public void conversionFailsForCyclicStructure() {
        // Arrange
        Document jsoupDoc = Jsoup.parse("<a></a>");
        Element a = jsoupDoc.selectFirst("a");
        a.appendChild(a); // Create a cycle

        // Act
        W3CDom.convert(jsoupDoc);
    }

    @Test(expected = IllegalArgumentException.class)
    public void conversionFailsForEmptyJsoupDocument() {
        // Arrange
        // An empty document with no root element is invalid for conversion.
        Document jsoupDoc = new Document("");

        // Act
        W3CDom.convert(jsoupDoc);
    }

    @Test
    public void handlesDocumentWithDoctype() {
        // Arrange
        String html = "<!DOCTYPE html><html><head><title>Some Title</title></head><body></body></html>";
        Document jsoupDoc = Jsoup.parse(html);
        W3CDom w3cDom = new W3CDom();

        // Act
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Assert
        assertNotNull(w3cDoc.getDoctype());
        assertEquals("html", w3cDoc.getDoctype().getName());
    }

    @Test
    public void handlesNamespacesCorrectlyWhenAware() {
        // Arrange
        String xml = "<div xmlns:foo='urn:bar'><foo:bar>baz</foo:bar></div>";
        Document jsoupDoc = Jsoup.parse(xml, "", Parser.xmlParser());
        W3CDom w3cDom = new W3CDom();
        assertTrue(w3cDom.namespaceAware()); // Default is true

        // Act
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Assert
        // With namespace awareness, the element has the correct local name and namespace URI.
        NodeList elements = w3cDoc.getElementsByTagNameNS("urn:bar", "bar");
        assertEquals(1, elements.getLength());
        Node element = elements.item(0);
        assertEquals("foo:bar", element.getNodeName());
        assertEquals("urn:bar", element.getNamespaceURI());
        assertEquals("bar", element.getLocalName());
    }

    @Test
    public void canDisableNamespaceAwareness() {
        // Arrange
        String xml = "<div xmlns:foo='bar'><foo:bar>baz</foo:bar></div>";
        Document jsoupDoc = Jsoup.parse(xml, "", Parser.xmlParser());
        W3CDom w3cDom = new W3CDom();
        w3cDom.namespaceAware(false);
        assertFalse(w3cDom.namespaceAware());

        // Act
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Assert
        // Without namespace awareness, "foo:bar" is treated as a literal tag name.
        NodeList elements = w3cDoc.getElementsByTagName("foo:bar");
        assertEquals(1, elements.getLength());
        assertNull(elements.item(0).getNamespaceURI());
    }

    // --- XPath Selection Tests ---

    @Test
    public void selectXpathFindsNodes() {
        // Arrange
        String html = "<div><p>One</p><p>Two</p></div>";
        Document jsoupDoc = Jsoup.parse(html);
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Act
        NodeList result = w3cDom.selectXpath("//p", w3cDoc);

        // Assert
        assertEquals(2, result.getLength());
        assertEquals("One", result.item(0).getTextContent());
        assertEquals("Two", result.item(1).getTextContent());
    }

    @Test
    public void selectXpathReturnsEmptyListForNoMatches() {
        // Arrange
        String html = "<div><p>One</p></div>";
        Document jsoupDoc = Jsoup.parse(html);
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Act
        NodeList result = w3cDom.selectXpath("//span", w3cDoc);

        // Assert
        assertEquals(0, result.getLength());
    }

    @Test(expected = IllegalStateException.class)
    public void selectXpathThrowsExceptionForInvalidQuery() {
        // Arrange
        Document jsoupDoc = Jsoup.parse("<p></p>");
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Act
        w3cDom.selectXpath("---", w3cDoc);
    }

    @Test(expected = IllegalStateException.class)
    public void selectXpathThrowsExceptionForNonNodeSetResult() {
        // Arrange
        Document jsoupDoc = Jsoup.parse("<p></p>");
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Act
        // This XPath expression returns a number, not a node-set.
        w3cDom.selectXpath("count(//p)", w3cDoc);
    }

    // --- sourceNodes and contextNode Tests ---

    @Test
    public void sourceNodesRetrievesOriginalJsoupNodes() {
        // Arrange
        String html = "<div><p>One</p><span>Two</span></div>";
        Document jsoupDoc = Jsoup.parse(html);
        List<org.jsoup.nodes.Node> originalNodes = jsoupDoc.select("p, span").stream().collect(Collectors.toList());

        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        NodeList w3cNodes = w3cDom.selectXpath("//p | //span", w3cDoc);

        // Act
        List<org.jsoup.nodes.Node> sourceNodes = w3cDom.sourceNodes(w3cNodes, org.jsoup.nodes.Node.class);

        // Assert
        assertEquals(2, sourceNodes.size());
        assertSame(originalNodes.get(0), sourceNodes.get(0)); // <p>
        assertSame(originalNodes.get(1), sourceNodes.get(1)); // <span>
        assertTrue(sourceNodes.get(0) instanceof Element);
    }

    @Test
    public void sourceNodesIgnoresNodesNotCreatedByW3CDom() {
        // Arrange
        // Create a NodeList that doesn't have the jsoupSource user data.
        // IIOMetadataNode is a convenient, standard way to get such a Node.
        IIOMetadataNode foreignNode = new IIOMetadataNode("dummy");
        NodeList nodeList = foreignNode.getChildNodes(); // An empty NodeList from a non-W3CDom source
        W3CDom w3cDom = new W3CDom();

        // Act
        List<org.jsoup.nodes.Node> sourceNodes = w3cDom.sourceNodes(nodeList, org.jsoup.nodes.Node.class);

        // Assert
        // The node is ignored because it wasn't created by the Jsoup-W3C conversion.
        assertTrue(sourceNodes.isEmpty());
    }

    // --- Helper Method Tests ---

    @Test
    public void propertiesFromMapConvertsMapCorrectly() {
        // Arrange
        Map<String, String> map = new HashMap<>();
        map.put("method", "xml");
        map.put("indent", "yes");

        // Act
        Properties props = W3CDom.propertiesFromMap(map);

        // Assert
        assertEquals(2, props.size());
        assertEquals("xml", props.getProperty("method"));
        assertEquals("yes", props.getProperty("indent"));
    }

    // --- Null and Invalid Argument Tests ---

    @Test(expected = IllegalArgumentException.class)
    public void fromJsoupThrowsExceptionForNullDocument() {
        new W3CDom().fromJsoup((Document) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromJsoupThrowsExceptionForNullElement() {
        new W3CDom().fromJsoup((Element) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void staticConvertThrowsExceptionForNullDocument() {
        W3CDom.convert(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void selectXpathThrowsExceptionForNullContext() {
        new W3CDom().selectXpath("//*", (Node) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void sourceNodesThrowsExceptionForNullNodeList() {
        new W3CDom().sourceNodes(null, org.jsoup.nodes.Node.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void sourceNodesThrowsExceptionForNullClassType() {
        // Arrange
        Document jsoupDoc = Jsoup.parse("<p></p>");
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        NodeList nodes = w3cDoc.getElementsByTagName("p");

        // Act
        w3cDom.sourceNodes(nodes, null);
    }

    @Test(expected = NullPointerException.class)
    public void propertiesFromMapThrowsExceptionForNullMap() {
        W3CDom.propertiesFromMap(null);
    }

    @Test(expected = NullPointerException.class)
    public void asStringThrowsExceptionForNullDocument() {
        new W3CDom().asString(null);
    }
}