package com.google.gson.internal;

import org.junit.Test;

/**
 * Tests for {@link LazilyParsedNumber}.
 */
public class LazilyParsedNumberTest {

    /**
     * The constructor's Javadoc states that the value must not be null.
     * This test verifies that if this contract is violated, a subsequent call
     * to hashCode() will fail with a NullPointerException, as it tries to
     * access the null internal value.
     */
    @Test(expected = NullPointerException.class)
    public void hashCode_whenConstructedWithNull_throwsNullPointerException() {
        // Arrange: Create a LazilyParsedNumber with a null string value,
        // which violates the class's contract.
        LazilyParsedNumber number = new LazilyParsedNumber(null);

        // Act: Attempt to calculate the hash code.
        // This action is expected to throw a NullPointerException.
        number.hashCode();

        // Assert: The test passes if the expected NullPointerException is thrown.
        // This is handled by the @Test(expected = ...) annotation.
    }
}