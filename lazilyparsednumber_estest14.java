package com.google.gson.internal;

import org.junit.Test;

/**
 * Tests for {@link LazilyParsedNumber}.
 */
public class LazilyParsedNumberTest {

    /**
     * Verifies that attempting to parse a LazilyParsedNumber constructed with a null
     * string value results in a NullPointerException. The class contract requires a
     * non-null value.
     */
    @Test(expected = NullPointerException.class)
    public void intValue_whenConstructedWithNull_throwsNullPointerException() {
        // Arrange: Create a LazilyParsedNumber with a null string, which is invalid input.
        LazilyParsedNumber number = new LazilyParsedNumber(null);

        // Act: Attempt to get the integer value. This should trigger the exception
        // because the internal parsing logic cannot handle a null string.
        number.intValue();

        // Assert: The @Test(expected) annotation verifies that a NullPointerException was thrown.
    }
}