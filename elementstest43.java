package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test suite for the {@link Elements} class, focusing on DOM manipulation methods.
 */
public class ElementsTest {

    /**
     * Verifies that Elements.clear() not only empties the list but also
     * removes the corresponding elements from the underlying document (DOM).
     */
    @Test
    void clearEmptiesListAndRemovesElementsFromDom() {
        // Arrange: Create a document and select a group of elements.
        Document doc = Jsoup.parse("<body><p>One</p><p>Two</p><div>Three</div></body>");
        Elements pElements = doc.select("p");

        // Sanity check the initial state.
        assertEquals(2, pElements.size(), "Initially, two <p> elements should be selected.");

        // Act: Clear the Elements collection.
        pElements.clear();

        // Assert: The collection should be empty, and the elements should be gone from the document.
        assertTrue(pElements.isEmpty(), "The Elements collection itself should be empty after clear().");
        
        // Verify the side-effect: the elements are also removed from the DOM.
        Elements remainingPElementsInDoc = doc.select("p");
        assertTrue(remainingPElementsInDoc.isEmpty(), "The <p> elements should also be removed from the Document.");
        
        // A final check on the document's structure confirms the change.
        assertEquals("<div>Three</div>", doc.body().html());
    }
}