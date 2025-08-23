package org.jsoup.helper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link Validate} helper class.
 */
class ValidateTest {

    @Test
    @DisplayName("notNullParam should not throw an exception for a non-null object")
    void notNullParam_withNonNullObject_doesNotThrow() {
        // Arrange: A non-null object and a parameter name.
        Object objectToValidate = new Object();
        String paramName = "testParam";

        // Act & Assert: The method should complete without throwing an exception.
        assertDoesNotThrow(() -> Validate.notNullParam(objectToValidate, paramName));
    }

    @Test
    @DisplayName("notNullParam should throw ValidationException for a null object")
    void notNullParam_withNullObject_throwsValidationException() {
        // Arrange: A specific parameter name to check in the exception message.
        String paramName = "param";
        String expectedMessage = "The parameter '" + paramName + "' must not be null.";

        // Act & Assert: The method call should throw a ValidationException.
        // The exception is caught and its message is verified.
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            Validate.notNullParam(null, paramName);
        });

        assertEquals(expectedMessage, exception.getMessage());
    }
}