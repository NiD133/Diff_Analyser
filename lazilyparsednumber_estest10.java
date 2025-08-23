package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link LazilyParsedNumber} class.
 */
public class LazilyParsedNumberTest {

    /**
     * Verifies that calling {@code doubleValue()} on an instance created
     * with the string "0" correctly returns the double value 0.0.
     */
    @Test
    public void doubleValue_whenNumberIsZero_returnsZero() {
        // Arrange
        LazilyParsedNumber number = new LazilyParsedNumber("0");
        double expectedValue = 0.0;

        // Act
        double actualValue = number.doubleValue();

        // Assert
        // A delta of 0.0 is used because we expect an exact conversion for "0".
        assertEquals(expectedValue, actualValue, 0.0);
    }
}