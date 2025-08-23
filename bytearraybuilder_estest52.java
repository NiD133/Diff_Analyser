package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link ByteArrayBuilder} class.
 */
public class ByteArrayBuilderTest {

    /**
     * Tests that calling release() on a newly created, empty ByteArrayBuilder
     * correctly maintains the size as zero.
     */
    @Test
    public void shouldHaveZeroSizeAfterReleasingEmptyBuilder() {
        // Arrange: Create a new, empty ByteArrayBuilder.
        // A new builder is expected to have a size of 0.
        ByteArrayBuilder builder = new ByteArrayBuilder();
        assertEquals("Pre-condition: A new builder should have a size of 0.", 0, builder.size());

        // Act: Release the builder's resources.
        builder.release();

        // Assert: The size should remain 0 after the release operation.
        assertEquals("The size of an empty builder should still be 0 after release.", 0, builder.size());
    }
}