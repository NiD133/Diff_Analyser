package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link IOCase} enum, focusing on the static {@code value} method.
 */
public class IOCaseTest {

    @Test
    public void value_shouldReturnValue_whenGivenValueIsNotNull() {
        // Arrange
        final IOCase inputValue = IOCase.SENSITIVE;
        final IOCase defaultValue = IOCase.INSENSITIVE;

        // Act
        final IOCase result = IOCase.value(inputValue, defaultValue);

        // Assert
        assertEquals("The non-null input value should be returned", inputValue, result);
    }

    @Test
    public void value_shouldReturnDefault_whenGivenValueIsNull() {
        // Arrange
        final IOCase defaultValue = IOCase.SYSTEM;

        // Act
        // The first argument is null, so the default should be returned.
        final IOCase result = IOCase.value(null, defaultValue);

        // Assert
        assertEquals("The default value should be returned when the input is null", defaultValue, result);
    }
}