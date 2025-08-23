package com.google.gson.internal;

import org.junit.Test;

/**
 * Tests for {@link LazilyParsedNumber}.
 */
public class LazilyParsedNumberTest {

    @Test(expected = NullPointerException.class)
    public void doubleValue_whenConstructedWithNull_throwsNullPointerException() {
        // Arrange: Create an instance with a null string, which is an invalid state.
        LazilyParsedNumber number = new LazilyParsedNumber(null);

        // Act: Attempt to get the double value, which triggers parsing of the null string.
        // The @Test(expected=...) annotation will assert that a NullPointerException is thrown.
        number.doubleValue();
    }
}