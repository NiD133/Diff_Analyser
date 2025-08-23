package com.google.gson.internal;

import org.junit.Test;

/**
 * Unit tests for the {@link LazilyParsedNumber} class.
 */
public class LazilyParsedNumberTest {

    /**
     * Verifies that calling {@code doubleValue()} on an instance created with a
     * non-numeric string throws a {@link NumberFormatException}.
     */
    @Test(expected = NumberFormatException.class)
    public void doubleValueShouldThrowNumberFormatExceptionForInvalidString() {
        // Given a LazilyParsedNumber initialized with a string that is not a valid number
        LazilyParsedNumber invalidNumber = new LazilyParsedNumber("...");

        // When doubleValue() is called, a NumberFormatException is expected.
        // The @Test(expected=...) annotation handles the assertion.
        invalidNumber.doubleValue();
    }
}