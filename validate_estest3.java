package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link Validate} helper class.
 */
public class ValidateTest {

    @Test
    public void failWithMessageShouldThrowIllegalArgumentException() {
        // Arrange: Define a clear and descriptive error message.
        final String expectedErrorMessage = "This is a specific failure message.";

        try {
            // Act: Call the method that is expected to fail.
            // We use the varargs version with an empty array to match the original test's intent.
            Validate.fail(expectedErrorMessage, new Object[]{});
            
            // This line should not be reached. If it is, the test fails.
            fail("Expected an IllegalArgumentException to be thrown, but it was not.");
        } catch (IllegalArgumentException e) {
            // Assert: Verify that the exception was thrown and contains the correct message.
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }
}