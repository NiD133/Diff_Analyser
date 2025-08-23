package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for the {@link ByteArrayBuilder} class.
 */
public class ByteArrayBuilderTest {

    /**
     * Verifies that when a ByteArrayBuilder is created using its default constructor,
     * it does not have an associated BufferRecycler.
     */
    @Test
    public void shouldReturnNullBufferRecyclerWhenConstructedWithoutOne() {
        // Arrange: Create a ByteArrayBuilder using the default constructor, which
        // does not supply a BufferRecycler.
        ByteArrayBuilder builder = new ByteArrayBuilder();

        // Act: Retrieve the BufferRecycler from the builder.
        BufferRecycler recycler = builder.bufferRecycler();

        // Assert: The recycler should be null, as none was provided at construction.
        assertNull("The BufferRecycler should be null when the builder is created without one.", recycler);
    }

    /**
     * Verifies that when a ByteArrayBuilder is created with a BufferRecycler,
     * the same instance is returned by the bufferRecycler() method.
     */
    @Test
    public void shouldReturnTheProvidedBufferRecycler() {
        // Arrange: Create a BufferRecycler and pass it to the ByteArrayBuilder's constructor.
        BufferRecycler expectedRecycler = new BufferRecycler();
        ByteArrayBuilder builder = new ByteArrayBuilder(expectedRecycler);

        // Act: Retrieve the BufferRecycler from the builder.
        BufferRecycler actualRecycler = builder.bufferRecycler();

        // Assert: The retrieved recycler should be the exact same instance that was provided.
        assertSame("The returned BufferRecycler should be the same instance provided at construction.",
                expectedRecycler, actualRecycler);
    }
}