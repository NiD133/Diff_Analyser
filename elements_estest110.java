package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class Elements_ESTestTest110 extends Elements_ESTest_scaffolding {

    /**
     * Tests that calling {@link Elements#prevAll(String)} with a null query string
     * behaves identically to calling {@link Elements#prevAll()} with no query,
     * returning all preceding siblings without any filtering.
     */
    @Test(timeout = 4000)
    public void prevAllWithNullQueryShouldReturnAllPreviousSiblings() {
        // Arrange
        // The Parser creates a standard document structure: <html><head></head><body>...</body></html>
        Document doc = Parser.parse("Some text", "");
        Elements allElements = doc.getAllElements(); // This will contain [<html>, <head>, <body>]

        // Act
        // A null query should be handled gracefully and act as a wildcard.
        Elements previousSiblings = allElements.prevAll((String) null);

        // Assert
        // The `allElements` collection contains <html>, <head>, and <body>.
        // - <html> has no previous siblings.
        // - <head> has no previous siblings.
        // - <body> has one previous sibling: <head>.
        // Therefore, the combined result should contain only the <head> element.
        assertEquals("Should find one previous sibling in total", 1, previousSiblings.size());

        Element headElement = previousSiblings.first();
        assertNotNull("The resulting element should not be null", headElement);
        assertEquals("The previous sibling of <body> should be <head>", "head", headElement.tagName());
    }
}