package org.jsoup.helper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link Validate#noNullElements(Object[], String)}.
 */
class ValidateTest {

    @Test
    @DisplayName("noNullElements with message should not throw for a valid array")
    void noNullElementsWithMessage_withValidArray_doesNotThrow() {
        // Arrange: Create an array that is valid (contains no null elements).
        Object[] validArray = {new Object(), new Object()};
        String message = "This message should not be used";

        // Act & Assert: Verify that no exception is thrown when validating the array.
        assertDoesNotThrow(() -> Validate.noNullElements(validArray, message));
    }

    @Test
    @DisplayName("noNullElements with message should throw ValidationException for an array with a null")
    void noNullElementsWithMessage_withNullInArray_throwsValidationException() {
        // Arrange: Create an array with a null element and define the expected error message.
        Object[] arrayWithNull = {new Object(), null};
        String expectedMessage = "Custom error message";

        // Act & Assert: Verify that a ValidationException is thrown and capture it.
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            Validate.noNullElements(arrayWithNull, expectedMessage);
        });

        // Assert: Check if the exception's message is the one we provided.
        assertEquals(expectedMessage, exception.getMessage());
    }
}