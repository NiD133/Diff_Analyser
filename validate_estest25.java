package org.jsoup.helper;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link Validate} helper class.
 */
public class ValidateTest {

    /**
     * Verifies that calling {@link Validate#notEmpty(String)} with an empty string
     * correctly throws an IllegalArgumentException.
     */
    @Test
    public void notEmpty_withEmptyString_throwsIllegalArgumentException() {
        // Arrange: Define the expected exception message.
        String expectedMessage = "String must not be empty";

        try {
            // Act: Call the method under test with an invalid argument.
            Validate.notEmpty("");
            
            // Assert: If the method does not throw an exception, this line is reached,
            // and the test should fail.
            fail("Expected an IllegalArgumentException to be thrown, but it was not.");
        } catch (IllegalArgumentException e) {
            // Assert: Verify that the caught exception has the expected message.
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}