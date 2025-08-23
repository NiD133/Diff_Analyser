package com.fasterxml.jackson.core.util;

import org.junit.Test;

/**
 * This test verifies the behavior of the {@link ByteArrayBuilder#completeAndCoalesce(int)} method
 * when it's called with an invalid length.
 */
public class ByteArrayBuilderCoalesceTest {

    /**
     * Tests that calling {@code completeAndCoalesce} with a length greater than the
     * underlying buffer's capacity throws an {@link ArrayIndexOutOfBoundsException}.
     *
     * A new {@link ByteArrayBuilder} starts with an initial buffer of 500 bytes. By
     * specifying a length larger than this, we force an illegal memory access during
     * the internal array copy operation.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void completeAndCoalesceWithLengthExceedingBufferCapacityThrowsException() {
        // Arrange: Create a builder with its default initial buffer (500 bytes).
        ByteArrayBuilder builder = new ByteArrayBuilder();
        int lengthExceedingCapacity = 2014; // Any value > 500 demonstrates the issue.

        // Act: Attempt to finalize the byte array with a declared length that is larger
        // than the actual buffer. This is expected to throw an exception.
        builder.completeAndCoalesce(lengthExceedingCapacity);

        // Assert: The 'expected' attribute of the @Test annotation handles the assertion.
        // The test will fail automatically if the expected exception is not thrown.
    }
}