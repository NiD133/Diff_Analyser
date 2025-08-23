package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.junit.Test;

import static org.junit.Assert.assertSame;

/**
 * Test suite for the {@link Elements} class.
 */
public class ElementsTest {

    /**
     * Verifies that calling {@link Elements#addClass(String)} with an empty string
     * is a no-op and returns the same {@link Elements} instance. This ensures that
     * method chaining is preserved.
     */
    @Test
    public void addClassWithEmptyStringShouldReturnSameInstanceForChaining() {
        // Arrange: Create a document and select its elements.
        // A shell document contains <html>, <head>, and <body> elements.
        Document doc = Document.createShell("");
        Elements elements = doc.getAllElements();

        // Act: Call addClass with an empty string.
        Elements returnedElements = elements.addClass("");

        // Assert: The returned object should be the same instance as the original,
        // confirming the method supports chaining.
        assertSame("The addClass method should return 'this' to allow for chaining.", elements, returnedElements);
    }
}