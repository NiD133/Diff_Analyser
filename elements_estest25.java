package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link Elements#parents()} method.
 */
public class ElementsParentsTest {

    /**
     * Tests that the parents() method correctly finds the unique parent element
     * from a collection of elements that share the same parent.
     */
    @Test
    public void parentsFindsUniqueParentOfMultipleElements() {
        // Arrange
        // A shell document creates a basic HTML structure: <html><head></head><body></body></html>
        Document doc = Document.createShell("");
        
        // This collection will contain the <html>, <head>, and <body> elements.
        Elements elements = doc.getAllElements();

        // Act
        // The parents() method should find all unique parents of the elements in the collection.
        // - The parent of <head> is <html>.
        // - The parent of <body> is <html>.
        // - <html> has no element parent (its parent is the document node).
        // The result should be a unique set containing just the <html> element.
        Elements parents = elements.parents();

        // Assert
        // Verify that only one unique parent was found.
        assertEquals("Should find one unique parent.", 1, parents.size());
        // Verify that the unique parent is the <html> element.
        assertEquals("The unique parent should be the 'html' element.", "html", parents.first().tagName());
    }
}