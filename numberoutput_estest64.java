package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link NumberOutput} class, focusing on integer to string conversion.
 */
public class NumberOutputTest {

    /**
     * Tests that {@link NumberOutput#toString(int)} correctly converts the integer 0
     * to its string representation "0".
     */
    @Test
    public void toString_withZeroInteger_shouldReturnZeroString() {
        // Arrange
        int numberToConvert = 0;
        String expectedString = "0";

        // Act
        String actualString = NumberOutput.toString(numberToConvert);

        // Assert
        assertEquals(expectedString, actualString);
    }
}