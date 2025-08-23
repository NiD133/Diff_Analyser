package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Contains tests for the {@link Elements} class.
 */
public class ElementsTest {

    @Test
    public void firstShouldReturnTheFirstElementFromACollection() {
        // Arrange: Jsoup wraps a body fragment in a full HTML document.
        // The direct child of the Document object is always the <html> element.
        Document doc = Jsoup.parseBodyFragment("<div>Some content</div>");
        Elements children = doc.children(); // This collection will contain just the <html> element.

        // Act: Get the first element from the collection.
        Element firstElement = children.first();

        // Assert: Verify that the first element is the expected <html> tag.
        assertNotNull("The first element should not be null for a non-empty list.", firstElement);
        assertEquals("The first child of a parsed document should be the <html> element.", "html", firstElement.tagName());
    }
}