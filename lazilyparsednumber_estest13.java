package com.google.gson.internal;

import org.junit.Test;

/**
 * Tests for {@link LazilyParsedNumber} to ensure it handles various inputs correctly.
 */
public class LazilyParsedNumberTest {

    /**
     * Verifies that calling intValue() on a LazilyParsedNumber created with a
     * non-numeric string throws a NumberFormatException.
     */
    @Test(expected = NumberFormatException.class)
    public void intValueShouldThrowNumberFormatExceptionForInvalidNumberString() {
        // Arrange: Create a LazilyParsedNumber with a string that cannot be parsed into a number.
        String invalidNumberString = "not a number";
        LazilyParsedNumber number = new LazilyParsedNumber(invalidNumberString);

        // Act: Attempt to convert the invalid string to an integer.
        // This action is expected to throw a NumberFormatException.
        number.intValue();
    }
}