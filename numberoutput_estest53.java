package com.fasterxml.jackson.core.io;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link NumberOutput} class, focusing on boundary conditions.
 */
public class NumberOutputTest {

    /**
     * Verifies that {@link NumberOutput#outputLong(long, byte[], int)} throws an
     * {@link ArrayIndexOutOfBoundsException} when the provided offset is outside
     * the bounds of the destination buffer.
     */
    @Test
    public void outputLongShouldThrowExceptionWhenOffsetIsOutOfBounds() {
        // Arrange: Set up a buffer and an invalid offset that points outside the buffer.
        final int bufferSize = 45;
        final int invalidOffset = 50; // An offset greater than the buffer's length.
        byte[] buffer = new byte[bufferSize];
        long anyNumber = 7039226437L; // The specific number doesn't matter for this boundary test.

        // Act & Assert: Attempt the operation and verify that the expected exception is thrown.
        try {
            NumberOutput.outputLong(anyNumber, buffer, invalidOffset);
            fail("Expected an ArrayIndexOutOfBoundsException because the offset is outside the buffer's bounds.");
        } catch (ArrayIndexOutOfBoundsException e) {
            // This is the expected outcome.
            // For added robustness, we can check that the exception message contains the invalid index.
            String expectedMessagePart = String.valueOf(invalidOffset);
            assertTrue(
                "The exception message should contain the invalid offset '" + expectedMessagePart + "'.",
                e.getMessage().contains(expectedMessagePart)
            );
        }
    }
}