package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link Elements#toString()} method.
 */
public class ElementsToStringTest {

    /**
     * Verifies that calling toString() on an empty Elements collection
     * returns an empty string.
     */
    @Test
    public void toStringOnEmptyElementsReturnsEmptyString() {
        // Arrange: Create an empty Elements object.
        Elements emptyElements = new Elements();

        // Act: Call the toString() method.
        String result = emptyElements.toString();

        // Assert: The result should be an empty string, as there are no elements to represent.
        assertEquals("", result);
    }
}