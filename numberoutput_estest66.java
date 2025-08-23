package com.fasterxml.jackson.core.io;

import org.junit.Test;

/**
 * Unit tests for the {@link NumberOutput} class, focusing on boundary conditions.
 */
public class NumberOutputTest {

    /**
     * Verifies that calling {@code outputLong} with an offset that is outside
     * the bounds of the destination buffer correctly throws an
     * {@link ArrayIndexOutOfBoundsException}.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void outputLong_withOffsetBeyondBufferLength_throwsArrayIndexOutOfBoundsException() {
        // Arrange: Set up a buffer and an offset that is intentionally invalid.
        byte[] buffer = new byte[20];
        int invalidOffset = 25; // An offset greater than the buffer's length.
        long anyLongValue = -9223372036854775717L;

        // Act: Attempt to write a long value at the invalid offset.
        // The @Test(expected=...) annotation asserts that the expected exception is thrown.
        NumberOutput.outputLong(anyLongValue, buffer, invalidOffset);

        // Assert: The test framework will automatically fail the test if the expected
        // exception is not thrown. No explicit 'fail()' call is needed.
    }
}