package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Contains tests for the {@link Elements#parents()} method.
 */
public class ElementsParentsTest {

    /**
     * Verifies that the parents() method correctly finds and returns the unique
     * parent elements from a collection of elements.
     */
    @Test
    public void parents_whenCalledOnMultipleElements_shouldReturnUniqueParents() {
        // Arrange: Create a basic HTML document with a standard structure (<html>, <head>, <body>).
        // Document.createShell() provides a document with this structure. [3, 5]
        Document doc = Document.createShell("");
        Elements allElements = doc.getAllElements(); // Contains [<html>, <head>, <body>]

        // Act: Call the parents() method to find all unique parent elements.
        // For <head> and <body>, the parent is <html>. The <html> element has no element parent.
        // The method should return a collection containing only the <html> element. [9]
        Elements parentElements = allElements.parents();

        // Assert: Verify that the result is correct.
        // The collection should not be empty and should contain exactly one element.
        assertFalse("The parents collection should not be empty.", parentElements.isEmpty());
        assertEquals("The parents collection should contain exactly one unique parent.", 1, parentElements.size());

        // For more specific verification, check that the single parent found is indeed the <html> tag.
        assertNotNull("The first parent element should not be null.", parentElements.first());
        assertEquals("The parent element should be the 'html' tag.", "html", parentElements.first().tagName()); [2]
    }
}