package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link NumberOutput} class.
 */
public class NumberOutputTest {

    /**
     * Tests that {@link NumberOutput#toString(int)} correctly converts a negative,
     * four-digit integer into its string representation.
     */
    @Test
    public void toString_shouldConvertNegativeIntegerToString() {
        // Arrange
        int numberToConvert = -1400;
        String expectedString = "-1400";

        // Act
        String actualString = NumberOutput.toString(numberToConvert);

        // Assert
        assertEquals(expectedString, actualString);
    }
}