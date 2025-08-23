package com.google.gson.internal;

import org.junit.Test;

/**
 * Tests for {@link LazilyParsedNumber}.
 */
public class LazilyParsedNumberTest {

    /**
     * Verifies that calling floatValue() on an instance created with a non-numeric
     * string throws a NumberFormatException.
     */
    @Test(expected = NumberFormatException.class)
    public void floatValueShouldThrowNumberFormatExceptionForInvalidNumberString() {
        // Arrange: Create a LazilyParsedNumber with a string that cannot be parsed as a number.
        LazilyParsedNumber nonNumeric = new LazilyParsedNumber("not a number");

        // Act: Attempt to convert the non-numeric string to a float.
        // This call is expected to throw a NumberFormatException.
        nonNumeric.floatValue();

        // Assert: The test passes if the expected exception is thrown.
        // No explicit assert is needed due to the @Test(expected=...) annotation.
    }
}