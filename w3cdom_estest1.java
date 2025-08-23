package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link W3CDom} to verify the conversion of jsoup documents
 * with namespaces into W3C DOM documents.
 */
public class W3CDomTest {

    private static final String SVG_NAMESPACE_URI = "http://www.w3.org/2000/svg";

    /**
     * Verifies that when a jsoup Document containing namespaced elements (like SVG)
     * is converted to a W3C Document, the namespaces are correctly preserved.
     */
    @Test
    public void fromJsoupConvertsNamespacedElementsCorrectly() {
        // Arrange: Create a jsoup document with an embedded SVG element, which uses namespaces.
        String htmlWithSvg = "<html><body><svg xmlns='" + SVG_NAMESPACE_URI + "' width='100' height='100'>" +
                             "<circle cx='50' cy='50' r='40' fill='red' />" +
                             "</svg></body></html>";
        Document jsoupDoc = Parser.parse(htmlWithSvg, "");
        W3CDom w3cDomConverter = new W3CDom();

        // Act: Convert the jsoup document to a W3C DOM document.
        org.w3c.dom.Document w3cDoc = w3cDomConverter.fromJsoup(jsoupDoc);

        // Assert: Verify that the SVG and circle elements exist in the W3C DOM
        // and have the correct namespace URI.
        NodeList svgElements = w3cDoc.getElementsByTagNameNS(SVG_NAMESPACE_URI, "svg");
        assertEquals("Should find one SVG element", 1, svgElements.getLength());
        
        Node svgNode = svgElements.item(0);
        assertNotNull("SVG node should not be null", svgNode);
        assertEquals("SVG node should have the correct namespace URI", SVG_NAMESPACE_URI, svgNode.getNamespaceURI());
        assertEquals("SVG node should have the correct local name", "svg", svgNode.getLocalName());

        NodeList circleElements = ((Element) svgNode).getElementsByTagNameNS(SVG_NAMESPACE_URI, "circle");
        assertEquals("Should find one circle element within the SVG element", 1, circleElements.getLength());

        Node circleNode = circleElements.item(0);
        assertNotNull("Circle node should not be null", circleNode);
        assertEquals("Circle node should have the correct namespace URI", SVG_NAMESPACE_URI, circleNode.getNamespaceURI());
        assertEquals("Circle node should have the correct local name", "circle", circleNode.getLocalName());
        
        // Also verify an attribute on the namespaced element
        Element circleElement = (Element) circleNode;
        assertEquals("Circle element should have correct 'cx' attribute", "50", circleElement.getAttribute("cx"));
    }
}