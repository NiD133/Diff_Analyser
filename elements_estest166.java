package org.jsoup.select;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link Elements} class, focusing on exception handling.
 */
public class ElementsTest {

    /**
     * Verifies that calling selectFirst() with an empty query string
     * throws an IllegalArgumentException, as the query must not be empty.
     */
    @Test
    public void selectFirstShouldThrowIllegalArgumentExceptionForEmptyQuery() {
        // Arrange
        Elements elements = new Elements();

        // Act & Assert
        try {
            elements.selectFirst("");
            fail("Expected an IllegalArgumentException to be thrown for an empty query.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message is correct, confirming the validation logic.
            assertEquals("String must not be empty", e.getMessage());
        }
    }
}