package com.fasterxml.jackson.core.io;

import org.junit.Test;

/**
 * Unit tests for the {@link NumberOutput} class, focusing on exception scenarios.
 */
public class NumberOutputTest {

    /**
     * Verifies that outputLong() throws an ArrayIndexOutOfBoundsException when the
     * provided character buffer is too small to hold the string representation of the number.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void outputLongShouldThrowExceptionWhenBufferIsTooSmall() {
        // Arrange
        // A long value that requires 18 characters to represent.
        long longValue = 833495342724150664L;
        
        // A buffer that is intentionally too small. The number requires 18 characters,
        // but the buffer only has 9.
        char[] buffer = new char[9];
        int offset = 1;

        // Act
        // This call is expected to throw an ArrayIndexOutOfBoundsException because
        // writing 18 digits starting at offset 1 requires a buffer of at least size 19.
        NumberOutput.outputLong(longValue, buffer, offset);

        // Assert
        // The test will pass if the expected ArrayIndexOutOfBoundsException is thrown.
        // This is handled by the @Test(expected = ...) annotation.
    }
}