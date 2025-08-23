package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link Elements} class, focusing on filtering methods.
 */
public class ElementsTest {

    /**
     * Verifies that the .not() method returns an unfiltered list of elements
     * when provided with a selector that matches nothing.
     */
    @Test
    public void notWithNonMatchingSelectorReturnsAllOriginalElements() {
        // Arrange
        // A shell document contains 4 elements: the document root, <html>, <head>, and <body>.
        Document doc = Document.createShell("");
        Elements allElements = doc.getAllElements();
        final int expectedInitialSize = 4;
        assertEquals("A shell document should contain 4 elements", expectedInitialSize, allElements.size());

        // This selector is syntactically valid but will not match any elements in the blank
        // shell document, as none contain the literal text "%s".
        String nonMatchingSelector = ":containsWholeOwnText(%s)";

        // Act
        // The .not() method should remove elements that match the selector.
        Elements filteredElements = allElements.not(nonMatchingSelector);

        // Assert
        // Since no elements matched the selector, none should have been removed.
        // The resulting collection should be the same size as the original.
        assertEquals("The size of the collection should be unchanged", expectedInitialSize, filteredElements.size());
    }
}