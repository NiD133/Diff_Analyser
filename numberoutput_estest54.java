package com.fasterxml.jackson.core.io;

import org.junit.Test;

/**
 * Unit tests for the {@link NumberOutput} class, focusing on boundary conditions.
 */
public class NumberOutputTest {

    /**
     * Verifies that calling {@code outputLong} with an offset that is outside
     * the bounds of the destination byte array throws an
     * {@link ArrayIndexOutOfBoundsException}.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void outputLong_shouldThrowException_whenOffsetIsOutOfBounds() {
        // Arrange
        byte[] buffer = new byte[10]; // A buffer of any size smaller than the offset.
        long value = 12345L;          // The specific value is irrelevant for this boundary check.
        int invalidOffset = 100;      // An offset clearly outside the buffer's bounds.

        // Act
        // This call is expected to throw ArrayIndexOutOfBoundsException.
        NumberOutput.outputLong(value, buffer, invalidOffset);

        // Assert is handled by the @Test(expected=...) annotation.
    }
}