package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests for the {@link Elements} class, focusing on selection mechanisms.
 */
public class ElementsTest {

    /**
     * Tests that getElementsByClass can correctly find an element
     * when its class name contains a hyphen.
     */
    @Test
    void getElementsByClassShouldFindElementWithHyphenatedClassName() {
        // Arrange
        String html = "<p class='tab-nav'>Check</p>";
        Document document = Jsoup.parse(html);

        // Act
        Elements elements = document.getElementsByClass("tab-nav");

        // Assert
        assertNotNull(elements, "The returned Elements collection should not be null.");
        assertEquals(1, elements.size(), "Should find exactly one element.");
        assertEquals("Check", elements.text(), "The text content of the found element should match.");
    }
}