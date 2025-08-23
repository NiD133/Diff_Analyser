package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.FormElement;
import org.junit.Test;
import org.w3c.dom.NodeList;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the W3CDom helper class, focusing on converting Jsoup nodes
 * back from their W3C DOM representation.
 */
public class W3CDomTest {

    /**
     * Verifies that a new W3CDom instance is configured to be namespace-aware by default,
     * which is its standard behavior.
     */
    @Test
    public void newInstanceShouldBeNamespaceAwareByDefault() {
        // Arrange
        W3CDom w3cDom = new W3CDom();

        // Act & Assert
        assertTrue("A new W3CDom instance should be namespace-aware by default.", w3cDom.namespaceAware());
    }

    /**
     * Tests that sourceNodes returns an empty list when asked to find nodes of a type
     * that do not exist in the provided NodeList, ensuring it handles "not found" cases gracefully.
     */
    @Test
    public void sourceNodesShouldReturnEmptyListForNonMatchingNodeType() {
        // Arrange
        W3CDom w3cDom = new W3CDom();
        // A basic HTML document without any <form> elements.
        Document jsoupDoc = Document.createShell("http://example.com");

        // Act
        // 1. Convert the Jsoup document to a W3C DOM document.
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        // 2. Get a list of all W3C element nodes from the document.
        NodeList allW3cNodes = w3cDoc.getElementsByTagName("*");
        // 3. Attempt to retrieve the original Jsoup nodes, but filter for a type that doesn't exist.
        List<FormElement> formElements = w3cDom.sourceNodes(allW3cNodes, FormElement.class);

        // Assert
        // The method should return a non-null, empty list.
        assertNotNull("The returned list should not be null, even if no nodes match.", formElements);
        assertEquals("The list should be empty as no FormElements exist in the source document.", 0, formElements.size());
    }
}