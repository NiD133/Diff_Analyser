package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link Elements#selectFirst(String)} method.
 */
public class ElementsTest {

    @Test
    @DisplayName("selectFirst on an Elements collection should find the first matching descendant")
    void selectFirstOnElementsFindsFirstMatchingDescendant() {
        // Arrange: Create a document with multiple potential matches spread across different elements.
        String html = String.join("\n",
            "<div><p>One</p></div>",
            "<div><p><span>Two</span></p></div>", // The first 'span' is in the second 'div'
            "<div><p><span>Three</span></p></div>"
        );
        Document doc = Jsoup.parse(html);
        Elements divs = doc.select("div");

        // Act: Select the first 'span' descendant from the collection of 'divs'.
        Element firstSpan = divs.selectFirst("p span");

        // Assert: The correct element is found, and its content is as expected.
        assertNotNull(firstSpan, "A matching element should be found.");
        assertEquals("Two", firstSpan.text(), "Should find the span with text 'Two', not 'Three'.");

        // Further Assertions: Verify selector behavior on both the result and the original collection.

        // 1. On the resulting Element ('firstSpan'):
        // A 'span' can be selected from itself, but not as a direct child.
        assertNotNull(firstSpan.selectFirst("span"), "Should be able to re-select the element itself.");
        assertNull(firstSpan.selectFirst(">span"), "A span cannot be a direct child of itself.");

        // 2. On the original Elements collection ('divs'):
        // The first 'div' can be selected from the collection, but not as a direct child of another 'div' in the list.
        assertNotNull(divs.selectFirst("div"), "Should find the first 'div' within the collection.");
        assertNull(divs.selectFirst(">div"), "Should not find a direct child 'div' as they are siblings at the document root.");
    }
}