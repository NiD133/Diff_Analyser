package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the {@link Elements#removeIf(java.util.function.Predicate)} method.
 */
public class ElementsTestTest47 {

    @Test
    @DisplayName("removeIf should remove matching elements, modify the DOM, and return true")
    void removeIf_whenElementMatches_removesElementAndReturnsTrue() {
        // Arrange: Create a document with four paragraphs.
        Document doc = Jsoup.parse("<body><p>One</p><p>Two</p><p>Three</p><p>Four</p></body>");
        Elements paragraphs = doc.select("p");
        assertEquals(4, paragraphs.size(), "Pre-condition: 4 paragraphs should exist.");

        // Act: Remove the paragraph containing the text "Two".
        boolean wasRemoved = paragraphs.removeIf(p -> p.text().equals("Two"));

        // Assert: The collection and the DOM should be modified as expected.
        assertTrue(wasRemoved, "Should return true as an element was removed.");
        assertEquals(3, paragraphs.size(), "The Elements collection size should be reduced by one.");

        String expectedHtml = "<p>One</p>\n<p>Three</p>\n<p>Four</p>";
        assertEquals(expectedHtml, doc.body().html(), "The underlying DOM should be modified.");
    }

    @Test
    @DisplayName("removeIf should not modify the DOM and return false if no elements match")
    void removeIf_whenNoElementMatches_doesNothingAndReturnsFalse() {
        // Arrange: Create a document and capture its initial state.
        Document doc = Jsoup.parse("<body><p>One</p><p>Two</p><p>Three</p><p>Four</p></body>");
        Elements paragraphs = doc.select("p");
        String initialHtml = doc.body().html();
        int initialSize = paragraphs.size();

        // Act: Attempt to remove an element that does not exist.
        boolean wasRemoved = paragraphs.removeIf(p -> p.text().equals("Five"));

        // Assert: The collection and the DOM should remain unchanged.
        assertFalse(wasRemoved, "Should return false as no element was removed.");
        assertEquals(initialSize, paragraphs.size(), "The Elements collection size should be unchanged.");
        assertEquals(initialHtml, doc.body().html(), "The underlying DOM should not be modified.");
    }
}