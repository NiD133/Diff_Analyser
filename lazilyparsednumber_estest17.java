package com.google.gson.internal;

import org.junit.Test;

/**
 * Tests for {@link LazilyParsedNumber}, focusing on its behavior with null values.
 */
public class LazilyParsedNumberTest {

    /**
     * The LazilyParsedNumber constructor accepts a null string, but the equals() method
     * is expected to throw a NullPointerException when invoked on that instance.
     * This test verifies that specific behavior.
     */
    @Test(expected = NullPointerException.class)
    public void equals_whenCalledOnInstanceWithNullValue_throwsNullPointerException() {
        // Arrange: Create an instance with a null internal value and another for comparison.
        LazilyParsedNumber numberWithNullValue = new LazilyParsedNumber(null);
        LazilyParsedNumber otherNumber = new LazilyParsedNumber("123");

        // Act & Assert: Calling equals() on the instance with the null value should throw.
        numberWithNullValue.equals(otherNumber);
    }
}