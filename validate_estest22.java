package org.jsoup.helper;

import org.junit.Test;

/**
 * Test suite for the {@link Validate} utility class.
 */
public class ValidateTest {

    /**
     * Verifies that {@link Validate#notEmptyParam(String, String)} does not throw an exception
     * when provided with a non-empty string. This is the expected "happy path" behavior.
     */
    @Test
    public void notEmptyParam_shouldNotThrowException_whenStringIsNotEmpty() {
        // Arrange
        String nonEmptyInput = "jZ#C5";
        String parameterName = "testParam";

        // Act & Assert
        // The test succeeds if this method call completes without throwing an exception.
        Validate.notEmptyParam(nonEmptyInput, parameterName);
    }
}