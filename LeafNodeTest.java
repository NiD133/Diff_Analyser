package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeFilter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LeafNodeTest {

    @Test
    public void testAttributeHandlingInLeafNodes() {
        // Test to ensure attributes are not prematurely set on nodes

        // Parse a document with a base URI
        String htmlContent = "<p>One <!-- Two --> Three<![CDATA[Four]]></p>";
        Document documentWithBaseUri = Jsoup.parse(htmlContent, "https://example.com/");
        
        // The document should have one attribute - the base URI
        assertTrue(hasAnyAttributes(documentWithBaseUri));

        // Parse a document without a base URI
        Document documentWithoutBaseUri = Jsoup.parse("<div>None</div>");
        
        // This document should not have any attributes
        assertFalse(hasAnyAttributes(documentWithoutBaseUri));

        // Check the first child of the document (the <html> element)
        Element htmlElement = documentWithBaseUri.child(0);
        assertFalse(hasAnyAttributes(htmlElement));

        // Generate the outer HTML of the document
        String outerHtml = documentWithBaseUri.outerHtml();
        
        // Ensure no attributes were added to the <html> element
        assertFalse(hasAnyAttributes(htmlElement));

        // Select the <p> element from the document
        Elements paragraphElements = documentWithBaseUri.select("p");
        Element paragraphElement = paragraphElements.first();
        
        // Ensure there is only one <p> element
        assertEquals(1, paragraphElements.size());
        assertFalse(hasAnyAttributes(htmlElement));

        // Select elements with a non-existent class
        Elements nonExistentClassElements = documentWithBaseUri.select("p.none");
        assertFalse(hasAnyAttributes(htmlElement));

        // Check the ID of the <p> element, which should be empty
        String paragraphId = paragraphElement.id();
        assertEquals("", paragraphId);
        
        // Ensure the <p> element does not have a class "Foobs"
        assertFalse(paragraphElement.hasClass("Foobs"));
        assertFalse(hasAnyAttributes(htmlElement));

        // Add a class to the <p> element
        paragraphElement.addClass("Foobs");
        
        // Verify the class was added
        assertTrue(paragraphElement.hasClass("Foobs"));
        assertTrue(hasAnyAttributes(htmlElement));
        assertTrue(hasAnyAttributes(paragraphElement));

        // Check the attributes of the <p> element
        Attributes paragraphAttributes = paragraphElement.attributes();
        assertTrue(paragraphAttributes.hasKey("class"));

        // Clear attributes from the <p> element
        paragraphElement.clearAttributes();
        
        // Ensure attributes are cleared
        assertFalse(hasAnyAttributes(paragraphElement));
        assertFalse(hasAnyAttributes(htmlElement));
        assertFalse(paragraphAttributes.hasKey("class"));
    }

    /**
     * Helper method to determine if a node has any attributes.
     *
     * @param node the node to check
     * @return true if the node has any attributes, false otherwise
     */
    private boolean hasAnyAttributes(Node node) {
        final boolean[] foundAttributes = new boolean[1];
        node.filter(new NodeFilter() {
            @Override
            public FilterResult head(Node node, int depth) {
                if (node.hasAttributes()) {
                    foundAttributes[0] = true;
                    return FilterResult.STOP;
                } else {
                    return FilterResult.CONTINUE;
                }
            }

            @Override
            public FilterResult tail(Node node, int depth) {
                return FilterResult.CONTINUE;
            }
        });
        return foundAttributes[0];
    }
}