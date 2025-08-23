package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link Elements#deselect(int)} method.
 */
public class Elements_ESTestTest46 {

    @Test(timeout = 4000)
    public void deselectRemovesElementFromListAndReturnsIt() {
        // Arrange
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        Elements allElements = doc.getAllElements();

        // For a new Document, getAllElements() returns the Document itself, <html>, <head>, and <body>.
        int initialSize = allElements.size();
        assertTrue("The initial list of elements should not be empty.", initialSize > 0);

        // The first element in this list is the Document object itself.
        Element elementToDeselect = allElements.get(0);

        // Act
        Element deselectedElement = allElements.deselect(0);

        // Assert
        // 1. Verify the correct element was returned.
        assertSame("The returned element should be the exact same instance as the one at the specified index.",
            elementToDeselect, deselectedElement);

        // 2. Verify the state of the returned element is preserved.
        assertTrue("The deselected element should be an instance of Document.", deselectedElement instanceof Document);
        assertEquals("The location of the deselected document should be unchanged.",
            baseUri, ((Document) deselectedElement).location());

        // 3. Verify the list was modified as expected.
        assertEquals("The size of the Elements list should decrease by one.",
            initialSize - 1, allElements.size());
        assertFalse("The list should no longer contain the deselected element.",
            allElements.contains(deselectedElement));
    }
}