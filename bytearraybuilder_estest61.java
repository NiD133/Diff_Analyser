package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link ByteArrayBuilder} class, focusing on its initial state.
 */
public class ByteArrayBuilderTest {

    /**
     * Verifies that a new ByteArrayBuilder instance, created with a specific
     * initial buffer capacity, correctly reports its initial size as zero.
     * The size represents the number of bytes written, not the allocated capacity.
     */
    @Test
    public void newBuilderWithInitialCapacityShouldHaveZeroSize() {
        // Arrange: Create a ByteArrayBuilder with a non-default initial capacity.
        int initialCapacity = 512;
        ByteArrayBuilder builder = new ByteArrayBuilder(initialCapacity);

        // Act: Get the current size of the builder.
        int currentSize = builder.size();

        // Assert: The size should be 0, as no bytes have been written yet.
        assertEquals("A new ByteArrayBuilder should have a size of 0, regardless of its initial capacity.", 0, currentSize);
    }
}