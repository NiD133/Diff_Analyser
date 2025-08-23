package org.jsoup.helper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link Validate#ensureNotNull(Object, String)} method.
 */
class ValidateTest {

    @Test
    @SuppressWarnings("deprecation") // Testing a deprecated method to ensure it still works as expected.
    void ensureNotNullWithMessage_withNonNullInput_returnsTheInputObject() {
        // Arrange
        Object object = new Object();
        String message = "This message should not be used";

        // Act
        Object result = Validate.ensureNotNull(object, message);

        // Assert
        assertSame(object, result, "The returned object should be the same as the input object.");
    }

    @Test
    @SuppressWarnings("deprecation") // Testing a deprecated method to ensure it still works as expected.
    void ensureNotNullWithMessage_withNullInput_throwsValidationException() {
        // Arrange
        String expectedMessage = "Custom error message";

        // Act & Assert
        // Verify that the correct exception is thrown.
        ValidationException thrown = assertThrows(
            ValidationException.class,
            () -> Validate.ensureNotNull(null, expectedMessage),
            "A ValidationException should be thrown for null input."
        );

        // Verify that the exception has the correct message.
        assertEquals(expectedMessage, thrown.getMessage(), "The exception message should match the one provided.");
    }
}