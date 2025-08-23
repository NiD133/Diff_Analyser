package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link Validate} helper class.
 */
public class ValidateTest {

    /**
     * Verifies that the {@link Validate#fail(String)} method correctly throws
     * an {@link IllegalArgumentException} with the provided message.
     */
    @Test
    public void failThrowsIllegalArgumentExceptionWithMessage() {
        // Arrange: Define the message that we expect the exception to contain.
        String expectedMessage = "This is a test failure message.";

        try {
            // Act: Call the method that is expected to throw an exception.
            Validate.fail(expectedMessage);

            // Assert: If this line is reached, the test fails because no exception was thrown.
            fail("Expected an IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            // Assert: The correct exception was thrown. Now, verify its message.
            assertEquals("The exception message should match the one provided.", expectedMessage, e.getMessage());
        }
    }
}