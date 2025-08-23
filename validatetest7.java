package org.jsoup.helper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for the deprecated {@link Validate#ensureNotNull(Object, String, Object...)} method.
 */
@SuppressWarnings("deprecation") // Intentionally testing a deprecated method for backward compatibility.
public class ValidateTest {

    @Test
    @DisplayName("ensureNotNull should return the object when it is not null")
    void ensureNotNull_withNonNullObject_returnsSameObject() {
        // Arrange
        Object inputObject = new Object();
        String messageFormat = "Object must not be null: %s";
        String messageArg = "additional info";

        // Act
        Object resultObject = Validate.ensureNotNull(inputObject, messageFormat, messageArg);

        // Assert
        assertSame(inputObject, resultObject, "The returned object should be the same instance as the input.");
    }

    @Test
    @DisplayName("ensureNotNull should throw ValidationException with a formatted message when the object is null")
    void ensureNotNull_withNullObject_throwsValidationException() {
        // Arrange
        String messageFormat = "Object must not be null: %s";
        String messageArg = "additional info";
        String expectedMessage = "Object must not be null: additional info";

        // Act & Assert
        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            Validate.ensureNotNull(null, messageFormat, messageArg);
        });

        assertEquals(expectedMessage, thrown.getMessage(), "The exception message should be correctly formatted.");
    }
}