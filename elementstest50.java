package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test suite for the {@link Elements} class.
 */
// The original class name "ElementsTestTest50" was renamed for clarity and convention.
public class ElementsTest {

    @Test
    @DisplayName("selectFirst should return the first matching element from a list")
    // The method name is now more descriptive of the test's specific scenario.
    void selectFirst_whenMultipleMatchesExist_returnsFirstOne() {
        // --- Arrange ---
        // A well-defined HTML structure makes the test's purpose clear.
        // The goal is to ensure selectFirst() correctly picks the first 'span' encountered
        // and ignores subsequent ones.
        String html = "<p>One</p>" +
                      "<p>Two <span>Jsoup</span></p>" + // This contains the first 'span'
                      "<p><span>Three</span></p>";      // This second 'span' should be ignored

        Document doc = Jsoup.parse(html);
        // Create an Elements collection to test against. Using doc.select("p") is more
        // explicit about the contents of the collection than the original doc.children().
        Elements paragraphs = doc.select("p");

        // --- Act ---
        // Find the first 'span' element within the collection of paragraphs.
        Element firstSpan = paragraphs.selectFirst("span");

        // --- Assert ---
        // Assertions include messages to explain what is being verified and why.
        assertNotNull(firstSpan, "A matching element should be found.");
        assertEquals("Jsoup", firstSpan.text(),
            "The text should belong to the first span, confirming the correct element was selected.");
    }
}