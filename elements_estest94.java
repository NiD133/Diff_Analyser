package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test class focuses on verifying the behavior of the {@link Elements} collection.
 */
public class Elements_ESTestTest94 extends Elements_ESTest_scaffolding {

    /**
     * Verifies that calling remove(Object) on an Elements collection successfully
     * removes the specified element and returns true.
     */
    @Test
    public void removeObjectShouldSucceedForExistingElement() {
        // Arrange: Create a document and get all its elements.
        // A new document contains the root, <html>, <head>, and <body> elements.
        Document doc = new Document("");
        Elements elements = doc.getAllElements();
        int initialSize = elements.size();

        // Pre-condition check: Ensure the element to be removed is present.
        assertTrue("Pre-condition failed: The document element should be in the list.", elements.contains(doc));

        // Act: Remove the document object itself from the Elements collection.
        // The remove(Object) method should remove the element from the list and also from the DOM.
        boolean wasRemoved = elements.remove(doc);

        // Assert: Verify the removal was successful and the list is in the correct state.
        assertTrue("The remove operation should return true to indicate success.", wasRemoved);
        assertEquals("The list size should be reduced by one.", initialSize - 1, elements.size());
        assertFalse("The removed element should no longer be in the list.", elements.contains(doc));
    }
}