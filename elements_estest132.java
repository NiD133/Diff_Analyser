package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link Elements} class.
 */
public class ElementsTest {

    /**
     * Verifies that hasText() returns true when at least one element in the collection
     * contains text content.
     */
    @Test
    public void hasTextShouldReturnTrueWhenAnElementContainsText() {
        // Arrange: Create a document where the <body> element contains text.
        // The getAllElements() method will return a collection including <html>, <head>, and <body>.
        Document doc = Parser.parseBodyFragment("This is some text", "");
        Elements elements = doc.getAllElements();

        // Act: Check if any element in the collection has text.
        boolean hasText = elements.hasText();

        // Assert: The result should be true because the <body> element contains text.
        assertTrue("Expected hasText() to be true when an element in the collection has text.", hasText);
    }
}