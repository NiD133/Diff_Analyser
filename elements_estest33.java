package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test suite for {@link Elements}.
 */
public class ElementsTest {

    @Test
    public void nextShouldReturnTheImmediateNextSiblingForEachElementInTheSet() {
        // Arrange
        // Jsoup.parse creates a full HTML document: <html><head></head><body>Some text</body></html>
        Document doc = Jsoup.parse("Some text");

        // Select the html, head, and body elements to test the .next() method on a collection.
        Elements elements = doc.select("html, head, body");
        assertEquals(3, elements.size()); // Sanity check: we have our 3 elements

        // Act
        // The .next() method finds the next sibling for each element in the collection.
        // - <html> has no parent, thus no siblings.
        // - <head>'s next sibling is <body>.
        // - <body> has no next sibling.
        // The resulting collection should contain only the unique siblings found, which is just the <body> element.
        Elements nextSiblings = elements.next();

        // Assert
        assertNotNull(nextSiblings);
        assertEquals(1, nextSiblings.size());
        assertEquals("body", nextSiblings.first().tagName());
    }
}