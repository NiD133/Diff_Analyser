package org.jsoup.helper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link Validate}.
 */
public class ValidateTest {

    @Test
    void notNullParamThrowsExceptionForNullInput() {
        // Arrange
        String paramName = "foo";
        String expectedMessage = "The parameter 'foo' must not be null.";

        // Act & Assert
        // Verify that calling notNullParam with a null object throws a ValidationException.
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            Validate.notNullParam(null, paramName);
        });

        // Verify that the exception message is correct.
        assertEquals(expectedMessage, exception.getMessage());
    }
}