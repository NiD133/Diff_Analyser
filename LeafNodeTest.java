package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeFilter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LeafNodeTest {

    @Test
    public void shouldLazilyInitializeAttributes() {
        // Test that LeafNode implementations don't eagerly create attribute maps
        // until attributes are actually needed, to optimize memory usage
        
        // Parse HTML with various node types (text, comment, CDATA)
        String htmlWithMixedNodes = "<p>One <!-- Two --> Three<![CDATA[Four]]></p>";
        Document docWithBaseUri = Jsoup.parse(htmlWithMixedNodes, "https://example.com/");
        Document docWithoutBaseUri = Jsoup.parse("<div>None</div>");
        
        // Document with base URI should have attributes (base URI is stored as attribute)
        assertTrue(hasAnyNodeWithAttributes(docWithBaseUri), 
            "Document with base URI should have at least one node with attributes");
        
        // Document without base URI should have no attributes initially
        assertFalse(hasAnyNodeWithAttributes(docWithoutBaseUri), 
            "Document without base URI should have no nodes with attributes");
        
        Element htmlElement = docWithBaseUri.child(0);
        
        // Verify that common read operations don't trigger attribute creation
        assertAttributesNotCreatedAfterReadOperations(htmlElement);
        
        // Verify that write operations do trigger attribute creation
        assertAttributesCreatedAfterWriteOperations(docWithBaseUri, htmlElement);
    }

    private void assertAttributesNotCreatedAfterReadOperations(Element htmlElement) {
        // Reading HTML should not create attributes
        String htmlOutput = htmlElement.ownerDocument().outerHtml();
        assertFalse(hasAnyNodeWithAttributes(htmlElement), 
            "Reading HTML output should not create attributes");

        // Selecting elements should not create attributes
        Elements paragraphs = htmlElement.ownerDocument().select("p");
        Element paragraph = paragraphs.first();
        assertEquals(1, paragraphs.size());
        assertFalse(hasAnyNodeWithAttributes(htmlElement), 
            "Selecting elements should not create attributes");

        // CSS selector queries should not create attributes
        Elements emptySelection = htmlElement.ownerDocument().select("p.none");
        assertFalse(hasAnyNodeWithAttributes(htmlElement), 
            "CSS selector queries should not create attributes");

        // Reading attribute values should not create attributes on other elements
        String elementId = paragraph.id();
        assertEquals("", elementId);
        assertFalse(paragraph.hasClass("TestClass"));
        assertFalse(hasAnyNodeWithAttributes(htmlElement), 
            "Reading attribute values should not create attributes on other elements");
    }

    private void assertAttributesCreatedAfterWriteOperations(Document document, Element htmlElement) {
        Elements paragraphs = document.select("p");
        Element paragraph = paragraphs.first();
        
        // Adding a CSS class should create attributes
        paragraph.addClass("TestClass");
        assertTrue(paragraph.hasClass("TestClass"), 
            "Element should have the added CSS class");
        assertTrue(hasAnyNodeWithAttributes(htmlElement), 
            "Adding CSS class should create attributes on HTML element");
        assertTrue(hasAnyNodeWithAttributes(paragraph), 
            "Adding CSS class should create attributes on target element");

        // Verify attributes are properly managed
        Attributes paragraphAttributes = paragraph.attributes();
        assertTrue(paragraphAttributes.hasKey("class"), 
            "Attributes should contain the class key");
        
        // Clearing attributes should remove them completely
        paragraph.clearAttributes();
        assertFalse(hasAnyNodeWithAttributes(paragraph), 
            "Clearing attributes should remove all attributes from element");
        assertFalse(hasAnyNodeWithAttributes(htmlElement), 
            "Clearing attributes should remove attributes from HTML element");
        assertFalse(paragraphAttributes.hasKey("class"), 
            "Cleared attributes should no longer contain class key");
    }

    /**
     * Recursively checks if any node in the tree has attributes.
     * Uses a visitor pattern to traverse all nodes efficiently.
     * 
     * @param rootNode the root node to start checking from
     * @return true if any node in the tree has attributes, false otherwise
     */
    private boolean hasAnyNodeWithAttributes(Node rootNode) {
        final boolean[] attributesFound = new boolean[1];
        
        rootNode.filter(new NodeFilter() {
            @Override
            public FilterResult head(Node node, int depth) {
                if (node.hasAttributes()) {
                    attributesFound[0] = true;
                    return FilterResult.STOP; // Stop traversal once we find attributes
                }
                return FilterResult.CONTINUE; // Keep looking
            }

            @Override
            public FilterResult tail(Node node, int depth) {
                return FilterResult.CONTINUE; // No action needed on exit
            }
        });
        
        return attributesFound[0];
    }
}