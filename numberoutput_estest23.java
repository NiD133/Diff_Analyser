package com.fasterxml.jackson.core.io;

import org.junit.Test;

/**
 * Unit tests for the {@link NumberOutput} class, focusing on edge cases and exception handling.
 */
public class NumberOutputTest {

    /**
     * Verifies that {@link NumberOutput#outputLong(long, char[], int)} throws an
     * {@link ArrayIndexOutOfBoundsException} when the provided buffer is too small
     * to hold the string representation of the number.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void outputLong_shouldThrowException_whenBufferIsTooSmall() {
        // Arrange
        // The string representation of Integer.MIN_VALUE ("-2147483648") has 11 characters.
        long valueToConvert = Integer.MIN_VALUE;
        
        // We provide a buffer that is intentionally too small to hold the result.
        // A buffer of size 10 is one character short.
        char[] buffer = new char[10];
        int offset = 0;

        // Act
        // This call should attempt to write past the end of the buffer, triggering an exception.
        NumberOutput.outputLong(valueToConvert, buffer, offset);

        // Assert
        // The test will pass if the expected ArrayIndexOutOfBoundsException is thrown.
        // If no exception is thrown, the test will fail.
    }
}