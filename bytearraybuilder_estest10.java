package com.fasterxml.jackson.core.util;

import org.junit.Test;

import static org.junit.Assert.assertNull;

/**
 * Unit tests for the {@link ByteArrayBuilder} class.
 */
public class ByteArrayBuilderTest {

    /**
     * Tests that calling {@code resetAndGetFirstSegment()} returns null
     * when the builder was initialized with a null buffer via the
     * {@code fromInitial()} factory method.
     *
     * This test case verifies the handling of a specific edge case where the builder
     * is put into a state with a null internal buffer before being reset.
     */
    @Test
    public void resetAndGetFirstSegmentShouldReturnNullWhenInitializedWithNullBuffer() {
        // Arrange: Create a ByteArrayBuilder instance using the fromInitial static factory
        // with a null buffer. This sets up the specific state to be tested.
        ByteArrayBuilder builder = ByteArrayBuilder.fromInitial(null, 2);

        // Act: Reset the builder and attempt to get its first segment.
        byte[] firstSegment = builder.resetAndGetFirstSegment();

        // Assert: The returned segment should be null, as the builder was effectively
        // empty and had no buffer to return after being reset.
        assertNull("Expected the first segment to be null after resetting a builder that was created with a null buffer.", firstSegment);
    }
}