package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link NumberOutput} class.
 */
public class NumberOutputTest {

    /**
     * Tests that the {@code toString(long)} method correctly converts a positive 
     * two-digit long value into its string representation.
     */
    @Test
    public void toString_shouldConvertPositiveTwoDigitLongToString() {
        // Arrange
        long numberToConvert = 11L;
        String expectedString = "11";

        // Act
        String actualString = NumberOutput.toString(numberToConvert);

        // Assert
        assertEquals(expectedString, actualString);
    }
}