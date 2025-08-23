package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Tests for the static utility methods of {@link IOCase}.
 */
public class IOCaseTest {

    /**
     * Tests that {@link IOCase#value(IOCase, IOCase)} returns the first argument
     * when it is not null.
     */
    @Test
    public void value_whenFirstArgumentIsNotNull_shouldReturnFirstArgument() {
        // Arrange
        final IOCase inputValue = IOCase.INSENSITIVE;
        final IOCase defaultValue = IOCase.SENSITIVE; // Use a different default to be explicit

        // Act
        final IOCase result = IOCase.value(inputValue, defaultValue);

        // Assert
        // The method should return the first argument, ignoring the default.
        assertSame("Expected the non-null input value to be returned", inputValue, result);
    }

    /**
     * Tests that {@link IOCase#value(IOCase, IOCase)} returns the default value
     * when the first argument is null.
     */
    @Test
    public void value_whenFirstArgumentIsNull_shouldReturnDefaultValue() {
        // Arrange
        final IOCase defaultValue = IOCase.SYSTEM;

        // Act
        final IOCase result = IOCase.value(null, defaultValue);

        // Assert
        // The method should fall back to the default value.
        assertSame("Expected the default value to be returned when the input is null", defaultValue, result);
    }
}