package com.google.gson.internal;

import org.junit.Test;

/**
 * Tests for {@link LazilyParsedNumber}.
 */
public class LazilyParsedNumberTest {

    /**
     * Tests that calling {@link LazilyParsedNumber#floatValue()} on an instance
     * created with a {@code null} string throws a {@link NullPointerException}.
     *
     * <p>Although the constructor's Javadoc states the input string must not be null,
     * this test verifies the actual behavior where the NullPointerException is deferred
     * until a conversion method is called.
     */
    @Test(expected = NullPointerException.class)
    public void floatValue_whenConstructedWithNull_throwsNullPointerException() {
        // Arrange: Create a LazilyParsedNumber with a null internal value.
        LazilyParsedNumber number = new LazilyParsedNumber(null);

        // Act & Assert: Attempting to get the float value should throw a NullPointerException.
        // The assertion is handled by the `expected` attribute of the @Test annotation.
        number.floatValue();
    }
}