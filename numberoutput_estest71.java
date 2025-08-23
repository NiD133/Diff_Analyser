package com.fasterxml.jackson.core.io;

import org.junit.Test;

/**
 * Unit tests for the {@link NumberOutput} class, focusing on edge cases and error handling.
 */
public class NumberOutputTest {

    /**
     * Verifies that {@code outputLong} throws an {@link ArrayIndexOutOfBoundsException}
     * when the provided character buffer is too small to hold the string representation
     * of the long value.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void outputLong_shouldThrowExceptionWhenBufferIsTooSmall() {
        // Arrange: A long value whose string representation requires more space than the buffer provides.
        // The number 12345L requires 5 characters, but the buffer only has a capacity of 4.
        long numberTooLargeForBuffer = 12345L;
        char[] smallBuffer = new char[4];
        int offset = 0;

        // Act & Assert: Attempting to write the long into the small buffer should throw an exception.
        // The @Test(expected=...) annotation handles the assertion.
        NumberOutput.outputLong(numberTooLargeForBuffer, smallBuffer, offset);
    }
}