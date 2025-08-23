package org.jsoup.select;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link Elements#select(String)} method.
 */
public class ElementsTest {

    /**
     * Verifies that calling select() with an empty query string throws an IllegalArgumentException.
     * The underlying validation logic prevents empty queries.
     */
    @Test
    public void selectWithEmptyQueryThrowsIllegalArgumentException() {
        // Arrange: Create an empty Elements collection.
        Elements elements = new Elements();

        // Act & Assert: Verify that the expected exception is thrown.
        try {
            elements.select("");
            fail("Expected an IllegalArgumentException to be thrown for an empty query string.");
        } catch (IllegalArgumentException e) {
            // Verify the exception message is correct.
            assertEquals("String must not be empty", e.getMessage());
        }
    }
}