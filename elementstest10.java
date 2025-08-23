package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the {@link Elements#hasText()} method.
 */
@DisplayName("Elements#hasText")
class ElementsHasTextTest {

    @Nested
    @DisplayName("when the collection is not empty")
    class WhenCollectionIsNotEmpty {

        @Test
        @DisplayName("should return true if at least one element contains text")
        void shouldReturnTrueIfAnyElementHasText() {
            // Arrange
            String html = "<div><p>Hello</p></div>" + // This div contains text
                          "<div><p></p></div>";      // This div is empty
            Document doc = Jsoup.parse(html);
            Elements elements = doc.select("div");

            // Act
            boolean result = elements.hasText();

            // Assert
            assertTrue(result, "Expected hasText() to be true because the first element contains text.");
        }

        @Test
        @DisplayName("should return false if no elements contain text")
        void shouldReturnFalseIfNoElementsHaveText() {
            // Arrange
            String html = "<div><p>Hello</p></div>" +
                          "<div><p></p></div>"; // This div is selected and has no text
            Document doc = Jsoup.parse(html);
            Elements elementsWithoutText = doc.select("div + div"); // Selects only the second, empty div

            // Act
            boolean result = elementsWithoutText.hasText();

            // Assert
            assertFalse(result, "Expected hasText() to be false because the selected element has no text.");
        }
    }

    @Nested
    @DisplayName("when the collection is empty")
    class WhenCollectionIsEmpty {

        @Test
        @DisplayName("should return false")
        void shouldReturnFalseForEmptyCollection() {
            // Arrange
            String html = "<div><p>Hello</p></div>";
            Document doc = Jsoup.parse(html);
            Elements emptyCollection = doc.select("span"); // This selector matches no elements

            // Act
            boolean result = emptyCollection.hasText();

            // Assert
            assertTrue(emptyCollection.isEmpty(), "Sanity check to ensure the collection is empty.");
            assertFalse(result, "Expected hasText() to be false for an empty collection.");
        }
    }
}