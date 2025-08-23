package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link LazilyParsedNumber} class.
 */
public class LazilyParsedNumberTest {

    @Test
    public void floatValue_whenNumberIsZero_returnsZeroFloat() {
        // Arrange
        LazilyParsedNumber number = new LazilyParsedNumber("0");
        float expected = 0.0F;

        // Act
        float actual = number.floatValue();

        // Assert
        // A delta of 0.0 is used for an exact comparison of floating-point numbers.
        assertEquals(expected, actual, 0.0F);
    }
}