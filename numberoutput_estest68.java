package com.fasterxml.jackson.core.io;

import org.junit.Test;

/**
 * Unit tests for the {@link NumberOutput} class, focusing on boundary conditions.
 */
public class NumberOutputTest {

    /**
     * Tests that {@link NumberOutput#outputInt(int, byte[], int)} throws an
     * {@link ArrayIndexOutOfBoundsException} when the provided offset is outside
     * the bounds of the destination byte array.
     *
     * The method should perform a bounds check before attempting to write any data.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void outputIntShouldThrowExceptionWhenOffsetIsOutOfBounds() {
        // Arrange: Create a buffer and an offset that is clearly out of bounds.
        final byte[] buffer = new byte[20];
        final int outOfBoundsOffset = 45;

        // The actual integer value doesn't matter for this test, as the
        // exception should be thrown due to the invalid offset.
        final int value = Integer.MIN_VALUE;

        // Act & Assert: Calling outputInt with an offset greater than the buffer's
        // length should immediately throw an ArrayIndexOutOfBoundsException.
        NumberOutput.outputInt(value, buffer, outOfBoundsOffset);
    }
}