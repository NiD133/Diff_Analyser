package com.fasterxml.jackson.core.io;

import org.junit.Test;

/**
 * Tests for {@link NumberOutput} focusing on exception handling with undersized buffers.
 */
public class NumberOutput_ESTestTest15 { // Note: Test class name kept for context.

    /**
     * Verifies that calling {@code outputLong} with a byte array that is too small
     * to hold the number's string representation throws an {@link ArrayIndexOutOfBoundsException}.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void outputLongShouldThrowExceptionWhenBufferIsTooSmall() {
        // GIVEN a long value that requires 10 bytes to represent ("1000000000").
        final long valueToWrite = 1_000_000_000L;
        
        // AND a buffer that is intentionally too small to hold the result.
        final int insufficientBufferSize = 5;
        byte[] smallBuffer = new byte[insufficientBufferSize];
        final int offset = 0;

        // WHEN attempting to write the long value into the small buffer.
        NumberOutput.outputLong(valueToWrite, smallBuffer, offset);

        // THEN an ArrayIndexOutOfBoundsException is expected.
    }
}