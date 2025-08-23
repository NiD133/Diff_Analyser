package org.jsoup.helper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link Validate}.
 * This suite focuses on the {@link Validate#notEmptyParam(String, String)} method.
 */
// The original class name 'ValidateTestTest14' was likely auto-generated.
// A clearer name like 'ValidateNotEmptyParamTest' or simply 'ValidateTest' is better.
public class ValidateTest {

    private static final String PARAM_NAME = "testParam";

    @Test
    @DisplayName("notEmptyParam should not throw for a non-empty string")
    void notEmptyParamWithValidStringShouldNotThrow() {
        // This test verifies the "happy path".
        // assertDoesNotThrow clearly states the intent: the method call should complete without an exception.
        assertDoesNotThrow(() -> Validate.notEmptyParam("valid", PARAM_NAME));
    }

    @Test
    @DisplayName("notEmptyParam should throw ValidationException for an empty string")
    void notEmptyParamWithEmptyStringShouldThrow() {
        // Using assertThrows is the modern, recommended way to test for exceptions in JUnit 5.
        // It's more concise and less error-prone than a try-catch block with a boolean flag.
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            Validate.notEmptyParam("", PARAM_NAME);
        });

        // The assertion on the exception message is now part of the same clear, focused test.
        assertEquals("The '" + PARAM_NAME + "' parameter must not be empty.", exception.getMessage());
    }

    @Test
    @DisplayName("notEmptyParam should throw ValidationException for a null string")
    void notEmptyParamWithNullStringShouldThrow() {
        // This test isolates the null input case, making it obvious what specific scenario is being tested.
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            Validate.notEmptyParam(null, PARAM_NAME);
        });

        assertEquals("The '" + PARAM_NAME + "' parameter must not be empty.", exception.getMessage());
    }
}