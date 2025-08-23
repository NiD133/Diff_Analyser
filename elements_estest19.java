package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the {@link Elements#remove(int)} method.
 */
public class ElementsRemoveTest {

    /**
     * Verifies that calling remove(index) on an Elements collection
     * correctly removes the element from both the list and the underlying DOM tree.
     */
    @Test
    public void removeByIndexRemovesElementFromListAndDom() {
        // Arrange: A new Document is initialized with a default HTML structure
        // (<html><head></head><body></body></html>).
        Document doc = new Document("");
        Elements allElements = doc.getAllElements();
        
        // Initially, getAllElements() returns [<html>, <head>, <body>].
        // We will test removing the first element, the <html> tag.
        int initialSize = allElements.size();
        Element elementToRemove = allElements.get(0);
        
        // Sanity checks on the initial state.
        assertEquals(3, initialSize);
        assertEquals("html", elementToRemove.tagName());

        // Act: Remove the element at index 0.
        Element removedElement = allElements.remove(0);

        // Assert
        // 1. The method should return the element that was removed.
        assertSame("The returned element should be the same instance as the one at the specified index.",
            elementToRemove, removedElement);

        // 2. The Elements list should now be smaller.
        assertEquals("The list size should be reduced by one.",
            initialSize - 1, allElements.size());

        // 3. The element should be detached from the DOM (its parent should be null).
        assertNull("The removed element should be detached from the DOM.",
            removedElement.parent());
    }
}