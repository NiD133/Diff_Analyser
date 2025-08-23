package com.fasterxml.jackson.core.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Contains tests for the {@link ByteArrayBuilder} class, focusing on its buffer management.
 */
public class ByteArrayBuilderTest {

    /**
     * Tests that calling {@code resetAndGetFirstSegment()} on a builder that was
     * initialized with an empty byte array returns that same empty array.
     * This verifies that the method correctly handles this edge case without
     * allocating a new buffer.
     */
    @Test
    public void resetAndGetFirstSegment_whenInitializedWithEmptyArray_shouldReturnEmptyArray() {
        // Arrange: Create a ByteArrayBuilder from an empty initial byte array.
        // The initial length parameter is irrelevant because reset() will clear it,
        // so we use 0 for clarity instead of a confusing arbitrary value.
        ByteArrayBuilder builder = ByteArrayBuilder.fromInitial(ByteArrayBuilder.NO_BYTES, 0);

        // Act: Reset the builder and get the first segment.
        byte[] firstSegment = builder.resetAndGetFirstSegment();

        // Assert: The returned segment should be a non-null, empty array.
        assertNotNull("The returned segment should not be null.", firstSegment);
        assertEquals("The first segment should be an empty array.", 0, firstSegment.length);
    }
}