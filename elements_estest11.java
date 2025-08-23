package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link Elements} class.
 */
public class ElementsTest {

    /**
     * Verifies that calling the text() method on an empty Elements collection
     * returns an empty string.
     */
    @Test
    public void textOfEmptyElementsShouldBeEmpty() {
        // Arrange: Create an empty collection of elements.
        Elements emptyElements = new Elements();

        // Act: Get the combined text from the collection.
        String resultText = emptyElements.text();

        // Assert: The resulting text should be an empty string.
        assertEquals("The text of an empty collection should be an empty string.", "", resultText);
    }
}