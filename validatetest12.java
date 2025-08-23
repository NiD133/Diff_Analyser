package org.jsoup.helper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link Validate#isFalse(boolean)}.
 */
public class ValidateTestTest12 {

    @Test
    void isFalse_whenConditionIsFalse_shouldNotThrowException() {
        // Arrange: A condition that is false.
        boolean condition = false;

        // Act & Assert: Verify that calling Validate.isFalse with a false condition does not throw an exception.
        assertDoesNotThrow(() -> Validate.isFalse(condition));
    }

    @Test
    void isFalse_whenConditionIsTrue_shouldThrowValidationException() {
        // Arrange: A condition that is true.
        boolean condition = true;
        String expectedMessage = "Must be false";

        // Act: Call the method and capture the thrown exception.
        ValidationException thrown = assertThrows(
            ValidationException.class,
            () -> Validate.isFalse(condition),
            "Expected Validate.isFalse(true) to throw, but it didn't"
        );

        // Assert: Verify that the exception has the expected message.
        assertEquals(expectedMessage, thrown.getMessage());
    }
}