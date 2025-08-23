package org.jsoup.helper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for the {@link Validate#notEmpty(String, String)} method.
 * This class replaces the original, less clear test structure.
 */
public class ValidateTest {

    private static final String CUSTOM_ERROR_MESSAGE = "Custom error message";

    /**
     * A helper class to represent the exception thrown by the method under test.
     * This is included for compilability, as the original source was not provided.
     */
    private static class ValidationException extends IllegalArgumentException {
        public ValidationException(String message) {
            super(message);
        }
    }

    @Test
    void notEmptyWithMessage_shouldNotThrow_forNonEmptyString() {
        // The method should complete without throwing an exception for a valid, non-empty string.
        assertDoesNotThrow(() -> Validate.notEmpty("foo", CUSTOM_ERROR_MESSAGE));
    }

    @Test
    void notEmptyWithMessage_shouldThrowException_forEmptyString() {
        // Given an empty string, the method is expected to throw a ValidationException.
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            Validate.notEmpty("", CUSTOM_ERROR_MESSAGE);
        });

        // Then: The exception's message should match the custom message provided.
        assertEquals(CUSTOM_ERROR_MESSAGE, exception.getMessage());
    }

    @Test
    void notEmptyWithMessage_shouldThrowException_forNullString() {
        // Given a null string, the method is expected to throw a ValidationException.
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            Validate.notEmpty(null, CUSTOM_ERROR_MESSAGE);
        });

        // Then: The exception's message should match the custom message provided.
        assertEquals(CUSTOM_ERROR_MESSAGE, exception.getMessage());
    }
}