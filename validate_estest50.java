package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link Validate} helper class.
 */
public class ValidateTest {

    /**
     * Verifies that the wtf() method always throws an IllegalStateException
     * with the message provided to it.
     */
    @Test
    public void wtfShouldAlwaysThrowIllegalStateException() {
        // Arrange
        String expectedMessage = "This is an unexpected state.";

        try {
            // Act
            Validate.wtf(expectedMessage);
            
            // Assert: The test should fail if no exception is thrown.
            fail("Expected an IllegalStateException to be thrown.");
            
        } catch (IllegalStateException e) {
            // Assert: The exception was thrown and has the correct message.
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}