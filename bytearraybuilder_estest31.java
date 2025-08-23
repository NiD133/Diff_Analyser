package com.fasterxml.jackson.core.util;

import org.junit.Test;

/**
 * Unit tests for the {@link ByteArrayBuilder} class, focusing on specific edge cases.
 */
public class ByteArrayBuilderTest {

    /**
     * Verifies that calling {@link ByteArrayBuilder#finishCurrentSegment()} throws a
     * {@link NullPointerException} when the builder has been initialized with a null
     * internal buffer.
     *
     * This scenario tests the handling of an internally inconsistent state, which can be
     * created via the package-private {@code fromInitial} factory method.
     */
    @Test(expected = NullPointerException.class)
    public void finishCurrentSegmentShouldThrowExceptionWhenInitializedWithNullBuffer() {
        // Arrange: Create a ByteArrayBuilder with a null internal buffer.
        // The fromInitial() method allows creating this specific state for testing.
        ByteArrayBuilder builder = ByteArrayBuilder.fromInitial(null, 2000);

        // Act: Attempt to finish the current segment, which is null.
        // This is expected to throw a NullPointerException.
        builder.finishCurrentSegment();
    }
}