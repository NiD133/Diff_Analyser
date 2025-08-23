package org.jsoup.select;

import org.jsoup.nodes.Element;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the {@link Elements} class.
 */
public class ElementsTest {

    /**
     * Verifies that calling the text() method on an empty Elements collection
     * returns an empty string.
     */
    @Test
    public void textOfEmptyElementsShouldReturnEmptyString() {
        // Arrange: Create an empty collection of elements.
        Elements emptyElements = new Elements();

        // Act: Retrieve the combined text from the empty collection.
        String actualText = emptyElements.text();

        // Assert: The resulting text should be empty.
        assertTrue("The text of empty elements should be an empty string.", actualText.isEmpty());
    }
}