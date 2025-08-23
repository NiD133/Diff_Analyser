package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link Elements} class.
 */
public class ElementsTest {

    /**
     * Tests that `deselect(Object)` successfully removes an element that exists in the collection.
     * The method should return true, and the element should no longer be present in the list.
     *
     * Note: This test covers the `deselect(Object)` method, which is not part of the modern public Jsoup API
     * but is inferred to exist from the original auto-generated test. It is assumed to behave like
     * {@link java.util.List#remove(Object)}, modifying only the list and not the underlying DOM.
     */
    @Test
    public void deselectRemovesExistingElementFromList() {
        // Arrange
        Document doc = new Document(""); // The base URI is irrelevant for this test.
        Elements elements = doc.getAllElements();
        int initialSize = elements.size();

        // The Document object itself is an Element and is included in the list returned by getAllElements().
        // We verify this precondition to ensure the test setup is correct.
        assertTrue("Precondition failed: The document element should be in the elements list.", elements.contains(doc));

        // Act
        // The deselect(Object) method removes the element from this list, but not from the DOM.
        boolean wasRemoved = elements.deselect((Object) doc);

        // Assert
        assertTrue("deselect() should return true when an element is successfully removed.", wasRemoved);
        assertEquals("The size of the elements list should decrease by one.", initialSize - 1, elements.size());
        assertFalse("The deselected element should no longer be in the list.", elements.contains(doc));
    }
}