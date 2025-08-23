package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link Elements} class.
 */
public class ElementsTest {

    /**
     * Verifies that calling outerHtml() on an empty Elements collection returns an empty string.
     */
    @Test
    public void outerHtmlOnEmptyElementsReturnsEmptyString() {
        // Arrange: Create an empty collection of elements.
        Elements emptyElements = new Elements();

        // Act: Get the outer HTML of the empty collection.
        String outerHtml = emptyElements.outerHtml();

        // Assert: The result should be an empty string.
        assertEquals("", outerHtml);
    }
}