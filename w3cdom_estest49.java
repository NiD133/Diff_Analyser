package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.w3c.dom.NodeList;

import java.util.List;

import static org.junit.Assert.*;

public class W3CDomTest {

    /**
     * Tests that the sourceNodes() method can correctly retrieve the original jsoup Element nodes
     * from a W3C NodeList that was created from a jsoup Document.
     */
    @Test
    public void sourceNodesRetrievesOriginalJsoupElementsFromW3cNodeList() {
        // Arrange
        W3CDom w3cDom = new W3CDom();
        String html = "<html><body><p>Hello</p> and some text</body></html>";
        Document jsoupDoc = Jsoup.parse(html);
        Element originalJsoupParagraph = jsoupDoc.selectFirst("p");

        // Act
        // 1. Convert the jsoup document to a W3C DOM document.
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // 2. Get a NodeList of the <body>'s children from the W3C document.
        // This list will contain the W3C <p> element and a W3C text node.
        NodeList w3cBodyChildren = w3cDoc.getElementsByTagName("body").item(0).getChildNodes();

        // 3. Retrieve the original jsoup *Elements* from the W3C NodeList.
        // This should filter out the text node and only return the jsoup Element.
        List<Element> sourceElements = w3cDom.sourceNodes(w3cBodyChildren, Element.class);

        // Assert
        // Verify that only the single <p> element was retrieved.
        assertEquals("Should retrieve only the Element nodes", 1, sourceElements.size());

        // Verify that the retrieved element is the exact same instance as the original jsoup <p> element.
        Element retrievedElement = sourceElements.get(0);
        assertSame("Retrieved element should be the original jsoup node instance",
            originalJsoupParagraph, retrievedElement);
        assertEquals("p", retrievedElement.tagName());

        // The original test also checked this; it's a good check for the default state.
        assertTrue("W3CDom should be namespace-aware by default", w3cDom.namespaceAware());
    }
}