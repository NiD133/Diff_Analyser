package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Tests for the {@link Elements#tagName(String)} method.
 */
public class ElementsTagNameTest {

    @Test
    public void tagNameShouldBeChainable() {
        // Arrange: Create a simple document and get all its elements.
        Document doc = Document.createShell(""); // A shell doc contains <html>, <head>, <body>
        Elements elements = doc.getAllElements();
        
        // Act: Call the tagName method to rename the elements.
        Elements returnedElements = elements.tagName("div");

        // Assert: The method should return the same instance to allow for method chaining.
        assertSame(
            "The tagName() method must return the same instance for chaining.",
            elements,
            returnedElements
        );

        // Also assert that the underlying elements were actually modified.
        assertEquals(3, doc.select("div").size());
        assertEquals(0, doc.select("html, head, body").size());
    }
}