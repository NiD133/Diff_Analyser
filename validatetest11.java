package org.jsoup.helper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link Validate#isTrue(boolean)} method.
 */
public class ValidateTestTest11 {

    @Test
    @DisplayName("isTrue should not throw an exception for a true condition")
    void isTrue_whenConditionIsTrue_doesNotThrow() {
        // The primary success scenario: a true condition should not cause any exception.
        assertDoesNotThrow(() -> Validate.isTrue(true));
    }

    @Test
    @DisplayName("isTrue should throw ValidationException for a false condition")
    void isTrue_whenConditionIsFalse_throwsValidationException() {
        // The failure scenario: a false condition must trigger a ValidationException.
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            Validate.isTrue(false);
        });

        // Additionally, verify that the exception message is as expected.
        assertEquals("Must be true", exception.getMessage());
    }
}