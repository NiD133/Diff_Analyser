package com.google.gson.internal;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link LazilyParsedNumber} class.
 */
public class LazilyParsedNumberTest {

    /**
     * Tests that the {@link LazilyParsedNumber#intValue()} method correctly parses
     * and returns the integer value from a valid integer string.
     */
    @Test
    public void intValue_shouldReturnCorrectInteger_whenValueIsIntegerString() {
        // Arrange
        LazilyParsedNumber number = new LazilyParsedNumber("5");
        int expectedValue = 5;

        // Act
        int actualValue = number.intValue();

        // Assert
        assertEquals(expectedValue, actualValue);
    }
}