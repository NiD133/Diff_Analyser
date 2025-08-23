package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link Elements#retainAll(java.util.Collection)} method.
 */
@DisplayName("Elements.retainAll")
class ElementsRetainAllTest {

    @Test
    @DisplayName("should modify the DOM and return true when elements are removed")
    void retainAll_whenElementsAreRemoved_modifiesDomAndReturnsTrue() {
        // Arrange
        String html = """
            <body>
              <p>One</p>
              <p>Two</p>
              <p>Three</p>
              <p>Four</p>
              <div>Div</div>
            </body>""";
        Document doc = Jsoup.parse(html);
        Elements allParagraphs = doc.select("p");

        // Select the middle two paragraphs ("Two" and "Three") to be retained.
        Elements paragraphsToKeep = doc.select("p:gt(0):lt(3)");
        assertEquals(4, allParagraphs.size(), "Should initially find all four paragraphs.");
        assertEquals(2, paragraphsToKeep.size(), "Selector for elements to keep should find two paragraphs.");

        // Act
        boolean wasModified = allParagraphs.retainAll(paragraphsToKeep);

        // Assert
        assertTrue(wasModified, "Should return true because the list was modified.");
        assertEquals(2, allParagraphs.size(), "The list should now contain only the 2 retained elements.");

        String expectedHtml = """
            <p>Two</p>
            <p>Three</p>
            <div>Div</div>""";
        // Comparing the normalized HTML makes the test robust against minor formatting differences.
        assertEquals(normalizeHtml(expectedHtml), normalizeHtml(doc.body().html()),
            "The DOM should be updated, removing the non-retained elements.");
    }

    @Test
    @DisplayName("should not modify the DOM and return false when no elements are removed")
    void retainAll_whenNoElementsAreRemoved_doesNotModifyDomAndReturnsFalse() {
        // Arrange
        String html = """
            <body>
              <p>One</p>
              <p>Two</p>
              <div>Div</div>
            </body>""";
        Document doc = Jsoup.parse(html);
        Elements paragraphs = doc.select("p");
        Elements sameParagraphs = doc.select("p"); // A collection containing the exact same elements.

        String originalHtml = doc.body().html();
        assertEquals(2, paragraphs.size());

        // Act
        // Attempt to retain all elements, which should result in no change.
        boolean wasModified = paragraphs.retainAll(sameParagraphs);

        // Assert
        assertFalse(wasModified, "Should return false because the list was not modified.");
        assertEquals(2, paragraphs.size(), "The list size should be unchanged.");
        assertEquals(normalizeHtml(originalHtml), normalizeHtml(doc.body().html()),
            "The DOM should not be modified.");
    }

    /**
     * Normalizes an HTML string by removing newlines and collapsing whitespace to make comparisons more reliable.
     * @param html The HTML string to normalize.
     * @return The normalized HTML string.
     */
    private String normalizeHtml(String html) {
        return html.replace("\n", "").replaceAll("\\s+", " ").trim();
    }
}