package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests for the {@link Elements} class.
 */
public class ElementsTest {

    @Test
    @DisplayName("expectFirst should return the first descendant element that matches the selector")
    void expectFirst_whenMatchExists_returnsFirstMatchingElement() {
        // Arrange
        String html = "<p>One</p><p>Two <span>Jsoup</span></p><p><span>Three</span></p>";
        Document doc = Jsoup.parse(html);
        Elements paragraphs = doc.select("p"); // A collection of three <p> elements

        // Act
        // The method searches through the collection of paragraphs for the first descendant <span>.
        Element foundSpan = paragraphs.expectFirst("span");

        // Assert
        assertNotNull(foundSpan, "A matching element should be found.");
        assertEquals("Jsoup", foundSpan.text(), "The text of the first matching span should be 'Jsoup'.");
        assertEquals("span", foundSpan.tagName(), "The element should be a <span>.");
    }
}