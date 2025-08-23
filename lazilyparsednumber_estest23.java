package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link LazilyParsedNumber} class.
 */
public class LazilyParsedNumberTest {

    /**
     * Tests that {@link LazilyParsedNumber#doubleValue()} correctly converts a string
     * representation of a negative integer into its corresponding double value.
     */
    @Test
    public void doubleValue_withNegativeIntegerString_returnsCorrectDouble() {
        // Arrange
        String negativeIntegerString = "-6";
        LazilyParsedNumber number = new LazilyParsedNumber(negativeIntegerString);
        double expectedDouble = -6.0;

        // Act
        double actualDouble = number.doubleValue();

        // Assert
        // A delta of 0.0 is used because the conversion from an integer string
        // to a double should be exact.
        assertEquals(expectedDouble, actualDouble, 0.0);
    }
}