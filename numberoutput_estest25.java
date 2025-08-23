package com.fasterxml.jackson.core.io;

import org.junit.Test;

/**
 * Contains tests for the {@link NumberOutput} class, focusing on boundary conditions.
 */
public class NumberOutputTest {

    /**
     * Verifies that calling {@code outputLong} with an offset that is outside
     * the bounds of the destination buffer correctly throws an
     * {@link ArrayIndexOutOfBoundsException}.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void outputLong_whenOffsetIsOutOfBounds_shouldThrowException() {
        // Arrange: A small buffer and an offset that is clearly out of bounds.
        byte[] buffer = new byte[10];
        int outOfBoundsOffset = 20;
        long value = Integer.MIN_VALUE; // The specific value is not critical for this test.

        // Act & Assert: This call is expected to throw an exception because the
        // offset is greater than the buffer's length.
        NumberOutput.outputLong(value, buffer, outOfBoundsOffset);
    }
}