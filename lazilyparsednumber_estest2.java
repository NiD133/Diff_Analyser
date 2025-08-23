package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link LazilyParsedNumber} class.
 */
public class LazilyParsedNumberTest {

    @Test
    public void toString_shouldReturnTheOriginalStringRepresentation() {
        // Arrange
        // Use a string that is not a valid number to confirm that the toString() method
        // returns the original raw value without performing any parsing or validation.
        String originalValue = "A_>  sS8(ab<";
        LazilyParsedNumber lazilyParsedNumber = new LazilyParsedNumber(originalValue);

        // Act
        String result = lazilyParsedNumber.toString();

        // Assert
        assertEquals(originalValue, result);
    }
}