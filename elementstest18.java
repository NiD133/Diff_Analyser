package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link Elements} class.
 */
public class ElementsTest {

    /**
     * Verifies that calling removeAll with a collection containing the same elements
     * successfully removes all elements and returns true.
     */
    @Test
    public void testRemoveAllWithIdenticalCollectionShouldEmptyTheList() {
        // Arrange: Create a shell document and get its elements.
        // A shell document contains <html>, <head>, and <body> tags.
        Document doc = Document.createShell("");
        Elements originalElements = doc.getAllElements();
        int initialSize = originalElements.size();

        // Create a new Elements collection containing the exact same elements.
        Elements elementsToRemove = new Elements((List<Element>) originalElements);

        // Act: Call the removeAll method.
        boolean wasModified = originalElements.removeAll(elementsToRemove);

        // Assert: Verify that the original collection is now empty and the method returned true.
        assertTrue("The initial collection should not be empty.", initialSize > 0);
        assertTrue("removeAll should return true, indicating the collection was modified.", wasModified);
        assertEquals("The collection should be empty after removing all its elements.", 0, originalElements.size());
    }
}