package com.fasterxml.jackson.core.util;

import org.junit.Test;

import static org.junit.Assert.assertNull;

/**
 * This test suite focuses on the behavior of the ByteArrayBuilder class.
 */
public class ByteArrayBuilderTest {

    /**
     * Verifies that calling getCurrentSegment() after release() returns null.
     * The release() method is expected to clear all internal buffers, making
     * the current segment inaccessible.
     */
    @Test
    public void getCurrentSegmentShouldReturnNullAfterRelease() {
        // Arrange: Create a ByteArrayBuilder.
        // A BufferRecycler is needed for instantiation, but its specific behavior is not under test.
        // The initial size of -1 is an edge case from the original test, kept for robustness.
        ByteArrayBuilder builder = new ByteArrayBuilder(new BufferRecycler(), -1);

        // Act: Release the builder's internal resources.
        builder.release();

        // Assert: The current segment should now be null.
        byte[] currentSegment = builder.getCurrentSegment();
        assertNull("The current segment should be null after the builder is released.", currentSegment);
    }
}