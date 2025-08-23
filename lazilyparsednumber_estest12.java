package com.google.gson.internal;

import org.junit.Test;

/**
 * Unit tests for the {@link LazilyParsedNumber} class.
 */
public class LazilyParsedNumberTest {

    /**
     * Verifies that calling longValue() on an instance created with a non-numeric
     * string correctly throws a NumberFormatException.
     */
    @Test(expected = NumberFormatException.class)
    public void longValueShouldThrowNumberFormatExceptionForNonNumericString() {
        // Arrange: Create a LazilyParsedNumber with a string that cannot be parsed as a number.
        LazilyParsedNumber nonNumeric = new LazilyParsedNumber("not a number");

        // Act & Assert: Calling longValue() should trigger the NumberFormatException.
        nonNumeric.longValue();
    }
}