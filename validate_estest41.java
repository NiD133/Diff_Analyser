package org.jsoup.helper;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for {@link Validate}.
 * Note: The original test class was auto-generated and has been renamed for clarity.
 */
public class ValidateTest {

    @Test
    public void notNullWithCustomMessage_shouldThrowException_whenObjectIsNull() {
        // Arrange
        String expectedMessage = ""; // The specific scenario tests an empty message.

        // Act & Assert
        try {
            Validate.notNull(null, expectedMessage);
            fail("Expected an IllegalArgumentException to be thrown for a null object, but it was not.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception was thrown and its message matches the one provided.
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}