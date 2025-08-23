package com.fasterxml.jackson.core.io;

import org.junit.Test;

/**
 * Unit tests for the {@link NumberOutput} class, focusing on boundary conditions and error handling.
 */
public class NumberOutputTest {

    /**
     * Verifies that calling {@code outputLong} with a negative offset throws an
     * {@link ArrayIndexOutOfBoundsException}. The underlying array access should
     * be protected against invalid negative indices.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void outputLongShouldThrowExceptionForNegativeOffset() {
        // Arrange: Set up the test conditions
        byte[] buffer = new byte[20]; // A buffer large enough to hold any long value
        int negativeOffset = -1;      // An invalid negative offset
        long anyNumber = 123L;        // The actual number doesn't matter for this test

        // Act & Assert: This call is expected to throw the exception
        NumberOutput.outputLong(anyNumber, buffer, negativeOffset);
    }
}