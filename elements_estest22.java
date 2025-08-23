package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link Elements#prevAll()}.
 */
public class ElementsPrevAllTest {

    @Test
    public void prevAllOnMultipleElementsCollectsUniquePreviousSiblings() {
        // Arrange
        // Jsoup automatically creates a standard document structure: <html><head></head><body>...</body></html>
        // The list of all elements will be [<html>, <head>, <body>].
        // Of these, only the <body> element has a previous sibling, which is <head>.
        Document doc = Jsoup.parse("<body></body>");
        Elements allElements = doc.getAllElements();

        // Act
        // The prevAll() method is called on the entire set of elements. It finds all previous
        // siblings for each element in the set and returns a new Elements object containing them.
        Elements previousSiblings = allElements.prevAll();

        // Assert
        // The resulting set should contain only the single unique previous sibling found (<head>).
        assertEquals(1, previousSiblings.size());
        assertEquals("head", previousSiblings.first().tagName());
    }
}