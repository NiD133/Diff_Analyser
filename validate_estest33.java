package org.jsoup.helper;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link Validate} helper class.
 */
public class ValidateTest {

    /**
     * Verifies that calling {@code Validate.isTrue(false, ...)} throws an
     * {@link IllegalArgumentException}. The test also confirms that the
     * exception message matches the string provided to the method.
     */
    @Test
    public void isTrue_shouldThrowException_whenConditionIsFalse() {
        // Arrange
        // The original test used an empty string for the message.
        String expectedMessage = "";

        // Act & Assert
        try {
            Validate.isTrue(false, expectedMessage);
            fail("Expected an IllegalArgumentException to be thrown, but no exception was thrown.");
        } catch (IllegalArgumentException e) {
            // Verify that the correct exception was thrown with the expected message.
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}