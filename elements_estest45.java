package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test suite for the {@link Elements#deselect(int)} method.
 */
public class ElementsDeselectTest {

    @Test
    public void deselectRemovesElementFromListButNotFromDomAndReturnsIt() {
        // Arrange
        // A shell document's structure is: <html><head><title>...</title></head><body></body></html>
        // This results in an Elements list of: [html, head, title, body]
        Document doc = Document.createShell("");
        Elements elements = doc.getAllElements();
        
        int initialSize = elements.size(); // Should be 4
        int bodyElementIndex = 3; // The <body> element is at index 3

        // Sanity check to ensure our understanding of the structure is correct
        assertEquals("body", elements.get(bodyElementIndex).tagName());

        // Act
        Element deselectedElement = elements.deselect(bodyElementIndex);

        // Assert
        // 1. Verify the correct element was returned
        assertNotNull(deselectedElement);
        assertEquals("body", deselectedElement.tagName());

        // 2. Verify the element was removed from the Elements list
        assertEquals("The list size should decrease by one.", initialSize - 1, elements.size());
        assertFalse("The list should no longer contain the deselected element.", elements.contains(deselectedElement));

        // 3. Verify the element remains in the original Document (the DOM)
        assertNotNull("The body element should still exist on the document.", doc.body());
        assertSame("The returned element should be the same instance as the one in the DOM.",
                     doc.body(), deselectedElement);
    }
}