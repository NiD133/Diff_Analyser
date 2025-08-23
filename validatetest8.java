package org.jsoup.helper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link Validate}.
 */
class ValidateTest {

    @Test
    @DisplayName("expectNotNull should return the object when it is not null")
    void expectNotNullReturnsObjectForNonNullInput() {
        // Arrange: A non-null object
        String input = "A non-null string";

        // Act: Call the method under test
        String result = Validate.expectNotNull(input);

        // Assert: The returned object is the same as the input object
        assertSame(input, result, "The method should return the same object instance.");
    }

    @Test
    @DisplayName("expectNotNull should throw ValidationException when the object is null")
    void expectNotNullThrowsExceptionForNullInput() {
        // Arrange: A null object. No explicit object needed.

        // Act & Assert: The method call should throw a ValidationException
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            Validate.expectNotNull(null);
        });

        // Assert: The exception message is correct
        assertEquals("Object must not be null", exception.getMessage());
    }
}