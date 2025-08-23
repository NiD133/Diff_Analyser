package com.fasterxml.jackson.core.io;

import org.junit.Test;

/**
 * Test suite for the {@link NumberOutput} class.
 */
public class NumberOutputTest {

    /**
     * Verifies that {@link NumberOutput#outputLong(long, byte[], int)} throws an
     * {@link ArrayIndexOutOfBoundsException} when the provided buffer is too small
     * to accommodate the string representation of the number.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void outputLongShouldThrowExceptionWhenBufferIsTooSmall() {
        // Arrange: A value that requires more space than the buffer provides.
        // The number Integer.MAX_VALUE (2147483647) requires 10 bytes.
        long valueToWrite = Integer.MAX_VALUE;
        byte[] insufficientBuffer = new byte[1];
        int offset = 0;

        // Act & Assert:
        // Attempting to write the long into the small buffer should throw an
        // ArrayIndexOutOfBoundsException. The @Test(expected=...) annotation
        // handles the assertion.
        NumberOutput.outputLong(valueToWrite, insufficientBuffer, offset);
    }
}