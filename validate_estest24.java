package org.jsoup.helper;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link Validate}.
 */
public class ValidateTest {

    @Test
    public void notEmptyParamWithNullStringThrowsIllegalArgumentException() {
        // Arrange
        String nullString = null;
        String paramName = ""; // The original test used an empty string for the parameter name

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> Validate.notEmptyParam(nullString, paramName)
        );

        // Further assert on the exception message for correctness
        String expectedMessage = "The '' parameter must not be empty.";
        assertEquals(expectedMessage, exception.getMessage());
    }
}