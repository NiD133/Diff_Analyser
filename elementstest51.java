package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Test suite for the {@link Elements} class.
 */
public class ElementsTest {

    /**
     * Verifies that {@link Elements#selectFirst(String)} returns null
     * when the CSS selector does not match any element in the collection.
     */
    @Test
    void selectFirstShouldReturnNullWhenNoElementMatches() {
        // Arrange: Create a document with <p> tags but no <span> tags.
        String html = "<p>One</p><p>Two</p><p>Three</p>";
        Document doc = Jsoup.parse(html);
        Elements elements = doc.children();

        // Act: Attempt to select the first <span> element, which does not exist.
        Element foundElement = elements.selectFirst("span");

        // Assert: The method should return null to indicate no match was found.
        assertNull(foundElement, "selectFirst should return null for a non-matching selector.");
    }
}