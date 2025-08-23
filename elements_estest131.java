package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * Test suite for the {@link Elements#hasText()} method.
 */
public class ElementsHasTextTest {

    /**
     * Verifies that hasText() returns false when an Elements collection contains
     * elements that have no text content, even if they have child elements.
     */
    @Test
    public void hasTextShouldReturnFalseWhenElementsContainNoText() {
        // Arrange: Create a document with elements that are empty or contain only other empty elements.
        String htmlWithEmptyElements = "<body><p></p><span></span></body>";
        Document doc = Jsoup.parse(htmlWithEmptyElements);
        Elements elements = doc.select("body"); // Select the <body>, which contains empty children.

        // Act: Check if the collection of elements has any text content.
        boolean hasText = elements.hasText();

        // Assert: The result should be false, as neither the <body> nor its children contain text.
        assertFalse("Expected hasText() to be false for elements without text", hasText);
    }
}