package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Tests for the {@link IOCase} enum, focusing on the {@code value} static method.
 */
public class IOCaseTest {

    /**
     * Tests that {@link IOCase#value(IOCase, IOCase)} returns the default value
     * when the first argument is null.
     */
    @Test
    public void valueShouldReturnDefaultWhenFirstArgumentIsNull() {
        // Arrange
        final IOCase defaultValue = IOCase.SYSTEM;

        // Act
        final IOCase result = IOCase.value(null, defaultValue);

        // Assert
        assertSame("The method should return the provided default value when the input value is null.",
                   defaultValue, result);
    }
}