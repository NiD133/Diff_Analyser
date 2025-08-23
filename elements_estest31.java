package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class Elements_ESTestTest31 extends Elements_ESTest_scaffolding {

    /**
     * Tests that nextAll() on a list of elements collects all unique following
     * siblings from each element in the list.
     */
    @Test
    public void nextAllCollectsFollowingSiblingsFromAllElementsInList() {
        // Arrange
        // A shell document has the structure: <html><head></head><body></body></html>
        Document doc = Document.createShell("");
        
        // doc.getAllElements() will return a list of [<html>, <head>, <body>]
        Elements sourceElements = doc.getAllElements();

        // Act
        // nextAll() should find the following siblings for each element in sourceElements.
        // - <html> has no parent, thus no siblings.
        // - <head>'s only following sibling is <body>.
        // - <body> has no following siblings.
        // The result should be a unique list containing just the <body> element.
        Elements followingSiblings = sourceElements.nextAll();

        // Assert
        assertEquals("Should find one unique following sibling", 1, followingSiblings.size());
        
        Element resultElement = followingSiblings.first();
        assertNotNull("The resulting element should not be null", resultElement);
        assertEquals("The single sibling should be the <body> tag", "body", resultElement.tagName());
    }
}