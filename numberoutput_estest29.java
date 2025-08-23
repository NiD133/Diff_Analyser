package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link NumberOutput} class, focusing on edge cases and exception handling.
 */
public class NumberOutputTest {

    /**
     * Tests that {@link NumberOutput#outputLong(long, byte[], int)} throws an
     * {@link ArrayIndexOutOfBoundsException} when the provided byte array buffer
     * is too small to hold the string representation of the long value.
     */
    @Test
    public void outputLongShouldThrowExceptionWhenBufferIsTooSmall() {
        // Arrange
        // The number 10,000,000,000,008 requires 14 characters to represent as a string.
        long valueToWrite = 10000000000008L;
        int offset = 1;

        // A buffer of size 8, with an offset of 1, has only 7 bytes of writable space.
        // This is insufficient for the 14 characters required, so an exception is expected.
        byte[] buffer = new byte[8];

        // Act & Assert
        try {
            NumberOutput.outputLong(valueToWrite, buffer, offset);
            fail("Expected an ArrayIndexOutOfBoundsException because the buffer is too small.");
        } catch (ArrayIndexOutOfBoundsException e) {
            // The implementation is expected to fail when it tries to write to index 8,
            // which is the first index beyond the buffer's bounds.
            // We verify the exception message to confirm this specific behavior.
            assertEquals("8", e.getMessage());
        }
    }
}