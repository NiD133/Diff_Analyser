package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link LazilyParsedNumber}.
 */
public class LazilyParsedNumberTest {

    @Test
    public void floatValue_whenGivenIntegerString_returnsCorrectFloat() {
        // Arrange
        LazilyParsedNumber number = new LazilyParsedNumber("7");
        float expectedValue = 7.0F;

        // Act
        float actualValue = number.floatValue();

        // Assert
        // A delta of 0.0 is used because the conversion from "7" to 7.0F is exact.
        assertEquals(expectedValue, actualValue, 0.0F);
    }
}