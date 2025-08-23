package com.fasterxml.jackson.core.util;

import org.junit.Test;

import static org.junit.Assert.assertSame;

/**
 * Unit tests for the {@link ByteArrayBuilder} class, focusing on its interaction
 * with {@link BufferRecycler}.
 */
public class ByteArrayBuilderTest {

    /**
     * Verifies that the {@code bufferRecycler()} method returns the same
     * {@link BufferRecycler} instance that was provided in the constructor.
     */
    @Test
    public void bufferRecycler_whenSetInConstructor_shouldReturnSameInstance() {
        // Arrange: Create a BufferRecycler and use it to initialize a ByteArrayBuilder.
        BufferRecycler expectedRecycler = new BufferRecycler();
        ByteArrayBuilder byteArrayBuilder = new ByteArrayBuilder(expectedRecycler);

        // Act: Retrieve the BufferRecycler from the ByteArrayBuilder.
        BufferRecycler actualRecycler = byteArrayBuilder.bufferRecycler();

        // Assert: The returned BufferRecycler should be the exact same instance.
        assertSame("The bufferRecycler() method should return the instance provided in the constructor.",
                expectedRecycler, actualRecycler);
    }
}