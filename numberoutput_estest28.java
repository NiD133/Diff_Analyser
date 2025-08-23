package com.fasterxml.jackson.core.io;

import org.junit.Test;

/**
 * Unit tests for the {@link NumberOutput} class, focusing on buffer boundary conditions.
 */
public class NumberOutputTest {

    /**
     * Verifies that {@code outputLong} throws an {@link ArrayIndexOutOfBoundsException}
     * when the destination byte array is too small to hold the complete string
     * representation of the number.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void outputLongShouldThrowExceptionForInsufficientBuffer() {
        // Arrange:
        // The number -424 requires 4 bytes for its string representation ("-424").
        long number = -424L;
        // We provide a buffer that is one byte too short to hold the result.
        byte[] buffer = new byte[3];
        int offset = 0;

        // Act:
        // This call should attempt to write the 4th byte into a 3-byte array,
        // which will cause an exception.
        NumberOutput.outputLong(number, buffer, offset);

        // Assert:
        // The test succeeds if an ArrayIndexOutOfBoundsException is thrown,
        // as specified by the @Test annotation. If no exception is thrown,
        // the test will automatically fail.
    }
}