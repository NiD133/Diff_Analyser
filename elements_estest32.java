package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test suite for the {@link Elements} class.
 */
public class ElementsTest {

    /**
     * Tests that calling {@link Elements#next(String)} with a null query
     * correctly finds the next direct sibling for each element in the collection.
     * This behavior should be equivalent to calling the no-argument {@link Elements#next()}.
     */
    @Test
    public void nextWithNullQueryFindsNextSiblingElements() {
        // Arrange
        // Parsing a body fragment creates a standard document: <html><head></head><body>...</body></html>
        Document doc = Parser.parseBodyFragment("some text", "");
        // This results in a list of all elements: [<html>, <head>, <body>]
        Elements allElements = doc.getAllElements();

        // Act
        // The next(null) method should find the next sibling for each element in the list.
        // - <html> has no next sibling.
        // - <head>'s next sibling is <body>.
        // - <body> has no next sibling.
        // The result should be a new Elements object containing only the <body> element.
        Elements nextSiblings = allElements.next((String) null);

        // Assert
        assertNotNull("The result should not be null.", nextSiblings);
        assertEquals("Should find exactly one next sibling element in total.", 1, nextSiblings.size());

        Element foundElement = nextSiblings.first();
        assertNotNull("The found element should not be null.", foundElement);
        assertEquals("The found sibling should be the <body> element.", "body", foundElement.tagName());
    }
}