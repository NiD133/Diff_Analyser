package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link Elements} class.
 */
public class ElementsTest {

    /**
     * Verifies that calling the tagName() method with an empty string
     * correctly throws an IllegalArgumentException.
     */
    @Test
    public void tagNameShouldThrowIllegalArgumentExceptionForEmptyName() {
        // Arrange: Create an Elements object containing a single element.
        Document doc = Jsoup.parse("<div>An element</div>");
        Elements elements = doc.select("div");

        // Act & Assert
        try {
            elements.tagName(""); // Attempt to rename the element with an empty tag name.
            fail("Expected an IllegalArgumentException to be thrown, but it was not.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception was thrown for the right reason.
            assertEquals("The 'tagName' parameter must not be empty.", e.getMessage());
        }
    }
}