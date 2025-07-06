package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LeafNodeTest {

    @Test
    public void shouldNotInitializeAttributesPrematurely() {
        // Test that LeafNodes (and other Nodes) don't create attribute maps unnecessarily.
        // This is an optimization to save memory, as many nodes don't have attributes.

        String html = "<p>One <!-- Two --> Three<![CDATA[Four]]></p>";
        Document doc = Jsoup.parse(html);

        // Document itself should have a base URI attribute.
        assertTrue(hasAnyAttributes(doc), "Document should have the base URI attribute.");

        // Initially, the <html> element should NOT have any attributes.
        Element htmlElement = doc.child(0); // The first child is the <html> element
        assertFalse(hasAnyAttributes(htmlElement), "<html> element should not initially have attributes.");

        // Serializing the document should not add attributes to the <html> element.
        String outerHtml = doc.outerHtml();
        assertFalse(hasAnyAttributes(htmlElement), "Serializing the document should not add attributes.");

        // Selecting <p> elements should not add attributes to the <html> element.
        Elements pElements = doc.select("p");
        assertEquals(1, pElements.size(), "Should find one <p> element.");
        Element p = pElements.first();
        assertFalse(hasAnyAttributes(htmlElement), "<p> selection should not add attributes to <html>.");

        // Selecting a non-existent element should not add attributes to the <html> element.
        Elements noElements = doc.select("p.none");
        assertFalse(hasAnyAttributes(htmlElement), "Non-existent selection should not add attributes to <html>.");

        // Accessing the ID of the <p> element (which is empty) should not add attributes.
        String id = p.id();
        assertEquals("", id, "<p> element should not have an ID.");
        assertFalse(p.hasClass("Foobs"), "<p> element should not have class 'Foobs'.");
        assertFalse(hasAnyAttributes(htmlElement), "Accessing empty ID should not add attributes to <html>.");

        // Adding a class to the <p> element should create the attribute map.
        p.addClass("Foobs");
        assertTrue(p.hasClass("Foobs"), "<p> element should now have class 'Foobs'.");
        assertTrue(hasAnyAttributes(htmlElement), "Adding class to <p> should add attributes to <html> element.");
        assertTrue(hasAnyAttributes(p), "<p> element should now have attributes.");

        // Verify that the attribute map contains the class attribute.
        Attributes attributes = p.attributes();
        assertTrue(attributes.hasKey("class"), "Attributes should contain the 'class' key.");

        // Clearing the attributes should remove the attribute map.
        p.clearAttributes();
        assertFalse(hasAnyAttributes(p), "<p> element should no longer have attributes after clearing.");
        assertFalse(hasAnyAttributes(htmlElement), "Clearing <p>'s attributes should remove them from <html> element.");
        assertFalse(attributes.hasKey("class"), "Attributes should no longer contain the 'class' key after clearing.");
    }

    /**
     * Checks if a node or any of its descendants have any attributes.  This method is used to verify that
     * attributes are only created when needed, and not prematurely.
     *
     * @param node The node to check.
     * @return True if the node or any of its descendants has attributes, false otherwise.
     */
    private boolean hasAnyAttributes(Node node) {
        // Use a NodeFilter to traverse the node tree and check for attributes.
        final boolean[] found = new boolean[1]; // Use an array to effectively pass a boolean by reference.

        node.filter(new NodeFilter() {
            @Override
            public FilterResult head(Node node, int depth) {
                if (node.hasAttributes()) {
                    found[0] = true;
                    return FilterResult.STOP; // Stop traversal once an attribute is found for efficiency.
                } else {
                    return FilterResult.CONTINUE; // Continue traversal if no attribute is found at this node.
                }
            }

            @Override
            public FilterResult tail(Node node, int depth) {
                return FilterResult.CONTINUE; // Not needed for this check, but required by the interface.
            }
        });

        return found[0];
    }
}