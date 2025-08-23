package com.fasterxml.jackson.core.io;

import org.junit.Test;

/**
 * This test suite focuses on verifying the behavior of the {@link NumberOutput} class,
 * specifically its exception handling for invalid arguments.
 */
public class NumberOutput_ESTestTest6 extends NumberOutput_ESTest_scaffolding {

    /**
     * Tests that `outputLong` throws an `ArrayIndexOutOfBoundsException`
     * when the provided offset is outside the valid range of the buffer's indices.
     * The method must not attempt to write past the buffer's boundaries.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void outputLong_shouldThrowException_whenOffsetIsOutOfBounds() {
        // Arrange: Set up a buffer and an invalid offset.
        // A long value requires at most 20 bytes (for Long.MIN_VALUE).
        byte[] buffer = new byte[20];
        long valueToWrite = 1_000_000_000_000L;

        // An offset equal to or greater than the buffer length is invalid.
        int invalidOffset = buffer.length;

        // Act & Assert: Call the method with an out-of-bounds offset.
        // The @Test(expected=...) annotation will automatically handle the assertion.
        NumberOutput.outputLong(valueToWrite, buffer, invalidOffset);
    }
}