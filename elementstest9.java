package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link Elements#text()} method.
 */
public class ElementsTest {

    @Test
    @DisplayName("text() should return the combined text of all elements, separated by spaces")
    void textReturnsCombinedTextOfAllElements() {
        // Arrange
        String html = "<div><p>Hello</p><p>there</p><p>world</p></div>";
        Document document = Jsoup.parse(html);
        Elements elements = document.select("div > p");
        String expectedText = "Hello there world";

        // Act
        String actualText = elements.text();

        // Assert
        assertEquals(expectedText, actualText);
    }
}