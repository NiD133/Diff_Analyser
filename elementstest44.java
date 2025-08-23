package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This test suite focuses on the behavior of the Elements class.
 */
public class ElementsTest {

    /**
     * Tests that the deselect(Object) method successfully removes an element
     * from the Elements list and returns true.
     *
     * The `deselect` method is designed to remove an element from the list
     * without affecting the underlying DOM, unlike the `remove` method.
     */
    @Test(timeout = 4000)
    public void deselectRemovesElementFromListAndReturnsTrue() {
        // Arrange: Create a document and get its elements.
        // A new Document object automatically contains a root structure (<html>, <head>, <body>).
        // The getAllElements() method includes the Document node itself in the returned list.
        Document document = new Document("");
        Elements allElements = document.getAllElements();
        int initialSize = allElements.size();

        // Pre-condition check: ensure the document element is in the list before removal.
        assertTrue("The list should initially contain the document element.", allElements.contains(document));

        // Act: Deselect (remove) the document element from the list.
        boolean wasRemoved = allElements.deselect(document);

        // Assert: Verify the element was removed and the list is in the correct state.
        assertTrue("deselect() should return true when an element is successfully removed.", wasRemoved);
        assertEquals("The size of the list should decrease by one.", initialSize - 1, allElements.size());
        assertFalse("The list should no longer contain the document element after deselecting it.", allElements.contains(document));
    }
}