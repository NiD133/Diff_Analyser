package com.fasterxml.jackson.core.util;

import org.junit.Test;

/**
 * Unit tests for {@link ByteArrayBuilder}, focusing on its constructor logic
 * and interaction with {@link BufferRecycler}.
 */
public class ByteArrayBuilderTest {

    /**
     * Verifies that the ByteArrayBuilder constructor throws an IndexOutOfBoundsException
     * if it's provided with a BufferRecycler that is too small to supply the
     * required buffer type.
     * <p>
     * The constructor requests a buffer of type {@code BufferRecycler.BYTE_WRITE_CONCAT_BUFFER},
     * which corresponds to index 2. This test provides a BufferRecycler configured with
     * internal arrays of size 1, which cannot satisfy the request.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void constructorWithInsufficientlySizedBufferRecyclerShouldThrowException() {
        // Arrange: Create a BufferRecycler with internal arrays of size 1, which is
        // smaller than the required buffer index (2).
        BufferRecycler smallBufferRecycler = new BufferRecycler(1, 1);

        // Act: Attempt to construct a ByteArrayBuilder. This will try to allocate
        // a buffer at an index that is out of bounds for the small recycler.
        // Assert: The @Test(expected=...) annotation verifies that the expected
        // IndexOutOfBoundsException is thrown.
        new ByteArrayBuilder(smallBufferRecycler);
    }
}