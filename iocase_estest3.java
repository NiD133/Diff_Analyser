package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Tests for the {@link IOCase} enum, focusing on the {@code value()} static method.
 */
public class IOCaseTest {

    @Test
    public void valueShouldReturnDefaultWhenValueIsNull() {
        // Arrange
        final IOCase defaultValue = IOCase.INSENSITIVE;

        // Act
        final IOCase result = IOCase.value(null, defaultValue);

        // Assert
        assertSame("When the input value is null, the default value should be returned.",
                     defaultValue, result);
    }

    @Test
    public void valueShouldReturnValueWhenValueIsNotNull() {
        // Arrange
        final IOCase inputValue = IOCase.SENSITIVE;
        final IOCase defaultValue = IOCase.INSENSITIVE; // A different value to ensure we don't get a false positive.

        // Act
        final IOCase result = IOCase.value(inputValue, defaultValue);

        // Assert
        assertSame("When the input value is not null, the input value itself should be returned.",
                     inputValue, result);
    }
}