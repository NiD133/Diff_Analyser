package org.jsoup.helper;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link Validate} helper class.
 */
public class ValidateTest {

    /**
     * Verifies that {@link Validate#notEmpty(String, String)} throws an
     * {@link IllegalArgumentException} when the input string is empty.
     * The exception message should match the custom message provided.
     */
    @Test
    public void notEmptyShouldThrowExceptionForEmptyString() {
        // Arrange: Define an empty input string and the expected error message.
        final String emptyString = "";
        final String expectedErrorMessage = "String must not be empty.";

        // Act & Assert: Call the method and verify that the correct exception is thrown.
        try {
            Validate.notEmpty(emptyString, expectedErrorMessage);
            fail("Expected an IllegalArgumentException, but no exception was thrown.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message is the one we provided.
            assertEquals("The exception message should match the custom one.", expectedErrorMessage, e.getMessage());
        }
    }
}