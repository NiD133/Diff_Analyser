package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/**
 * Contains tests for the {@link ByteArrayBuilder} class, focusing on specific state-related behaviors.
 */
public class ByteArrayBuilderTest {

    /**
     * Verifies that getClearAndRelease() can be called multiple times on an empty builder
     * without causing errors, and that it consistently returns an empty byte array.
     * This ensures the method correctly resets the builder's state.
     */
    @Test
    public void getClearAndReleaseOnEmptyBuilderShouldBeRepeatable() {
        // Arrange: Create an empty ByteArrayBuilder.
        // A BufferRecycler is used as per the original test's setup.
        BufferRecycler recycler = new BufferRecycler();
        ByteArrayBuilder builder = new ByteArrayBuilder(recycler);

        // Act: Call getClearAndRelease() once to clear the builder.
        // The result of this first call is intentionally ignored.
        builder.getClearAndRelease();

        // Call it a second time to test the behavior on an already-cleared builder.
        byte[] result = builder.getClearAndRelease();

        // Assert: The second call should return an empty byte array, confirming
        // that the builder was correctly reset and the operation is safe to repeat.
        assertArrayEquals(ByteArrayBuilder.NO_BYTES, result);
    }
}