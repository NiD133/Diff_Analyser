package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the {@link Elements#prev(String)} method.
 */
public class ElementsPrevWithSelectorTest {

    @Test
    public void prevWithSelectorReturnsEmptyWhenNoPreviousSiblingsMatch() {
        // Arrange
        // Create a document with a standard structure. The <body> tag's previous sibling is <head>.
        // The <html> and <head> tags have no previous siblings.
        Document doc = Jsoup.parse("<html><head></head><body></body></html>");
        Elements allElements = doc.getAllElements(); // Contains [html, head, body]

        // Act
        // Attempt to find the previous sibling of each element that matches a non-existent CSS class.
        Elements foundElements = allElements.prev(".non-existent-class");

        // Assert
        // The <head> element (the only previous sibling found) does not match the selector.
        // Therefore, the resulting collection should be empty.
        assertTrue("The result should be empty as no element has a previous sibling matching the selector.", foundElements.isEmpty());
    }
}