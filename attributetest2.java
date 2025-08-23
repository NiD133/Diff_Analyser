package org.jsoup.nodes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link Attribute} class, focusing on its HTML representation.
 */
@DisplayName("Attribute HTML Generation")
class AttributeTest {

    @Test
    @DisplayName("html() should correctly escape special HTML characters in the attribute's value")
    void html_whenValueContainsSpecialCharacters_shouldEscapeThem() {
        // Arrange
        String valueWithSpecialChars = "<value>";
        Attribute attribute = new Attribute("key", valueWithSpecialChars);
        String expectedHtml = "key=\"&lt;value&gt;\"";

        // Act
        String actualHtml = attribute.html();

        // Assert
        assertEquals(expectedHtml, actualHtml, "Attribute value should be HTML-escaped.");
    }
}