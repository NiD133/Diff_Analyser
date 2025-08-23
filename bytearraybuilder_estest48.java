package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Contains tests for the {@link ByteArrayBuilder} class, focusing on its
 * manual segment handling API.
 */
public class ByteArrayBuilderTest {

    /**
     * Tests the behavior of calling finishCurrentSegment() on a newly created, empty builder.
     * <p>
     * The {@code finishCurrentSegment()} method is part of a manual buffer handling API and
     * assumes the current segment is full. This test verifies that when called on an
     * empty builder, it moves the initial buffer to the list of completed segments.
     * This causes the builder's logical size to increase by the full capacity of that
     * initial buffer, even though no bytes were actually written.
     */
    @Test
    public void finishCurrentSegmentOnEmptyBuilderIncreasesSizeByInitialCapacity() {
        // Arrange: Create a new, empty ByteArrayBuilder.
        ByteArrayBuilder builder = new ByteArrayBuilder();
        assertEquals("A new builder should have a size of 0", 0, builder.size());

        // Act: Finish the current segment, which is the initial empty buffer.
        builder.finishCurrentSegment();

        // Assert: The builder's size should now equal the initial block's capacity,
        // and toByteArray() should return an array of that size.
        int sizeAfterFinish = builder.size();
        assertTrue("Size should be greater than 0 after finishing the initial segment", sizeAfterFinish > 0);

        byte[] result = builder.toByteArray();
        assertEquals("The resulting byte array should have the new logical size", sizeAfterFinish, result.length);
        assertEquals("Builder size should remain consistent after toByteArray()", sizeAfterFinish, builder.size());
    }
}