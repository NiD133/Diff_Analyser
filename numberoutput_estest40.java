package com.fasterxml.jackson.core.io;

import org.junit.Test;

/**
 * This test class verifies the behavior of the NumberOutput class, focusing on
 * edge cases and error handling.
 */
public class NumberOutputTest {

    /**
     * Tests that {@link NumberOutput#outputInt(int, byte[], int)} throws an
     * {@link ArrayIndexOutOfBoundsException} when the provided offset is outside
     * the bounds of the destination buffer.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void outputInt_shouldThrowException_whenOffsetIsOutOfBounds() {
        // Arrange: A buffer and an offset that is clearly outside the buffer's valid range.
        byte[] buffer = new byte[8];
        int outOfBoundsOffset = 11; // An offset greater than the buffer's length.
        int arbitraryValue = 1_000_000;

        // Act: Attempt to write an integer at the invalid offset.
        // This call is expected to throw an ArrayIndexOutOfBoundsException.
        NumberOutput.outputInt(arbitraryValue, buffer, outOfBoundsOffset);

        // Assert: The test succeeds if the expected exception is thrown, as declared
        // in the @Test annotation. No further assertions are needed.
    }
}