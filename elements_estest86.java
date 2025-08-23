package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.junit.Test;

import static org.junit.Assert.assertThrows;

/**
 * Test suite for the {@link Elements} class.
 */
public class ElementsTest {

    /**
     * Tests that calling the before() method on an Elements collection that was used to create
     * a recursive DOM structure results in a StackOverflowError.
     * <p>
     * This scenario can occur when a collection of elements, including ancestors and descendants,
     * is used to modify the DOM in a way that creates a cyclical or infinitely deep path for
     * tree traversal algorithms.
     * </p>
     */
    @Test
    public void beforeOnElementsInRecursiveDomStructureThrowsStackOverflowError() {
        // Arrange: Create a DOM structure where elements are reparented to be siblings
        // of their original parent, leading to a problematic structure for modifications.
        Document doc = new Document("http://example.com"); // Creates <html><head></head><body></body></html>
        Elements allElements = doc.getAllElements(); // Contains [<html>, <head>, <body>]

        // This is the key step: we take all elements and append them back to the document root.
        // This operation moves <head> and <body> to become siblings of <html>, instead of its children.
        // The 'allElements' list now refers to elements within this modified, recursive-like structure.
        doc.appendChildren(allElements);

        // Act & Assert: Calling before() on this collection attempts to parse and insert HTML
        // relative to each element. The unusual ancestry causes an infinite recursion during
        // the parsing context resolution, leading to a StackOverflowError.
        assertThrows(StackOverflowError.class, () ->
            allElements.before("<span>New</span>")
        );
    }
}