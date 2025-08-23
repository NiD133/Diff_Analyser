package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.io.BufferRecycler;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for the {@link ByteArrayBuilder} class.
 */
public class ByteArrayBuilderTest {

    /**
     * Verifies that the ByteArrayBuilder constructor, when provided with a BufferRecycler,
     * ignores the 'firstBlockSize' hint and instead requests a default-sized buffer
     * from the recycler.
     */
    @Test
    public void constructorWithBufferRecycler_shouldUseDefaultRecyclerBufferSize() {
        // Arrange
        BufferRecycler recycler = new BufferRecycler();
        // Use a nonsensical size to emphasize that this argument should be ignored.
        int ignoredInitialSize = -1;

        // Act
        // This constructor should request a BYTE_WRITE_CONCAT_BUFFER from the recycler.
        ByteArrayBuilder builder = new ByteArrayBuilder(recycler, ignoredInitialSize);
        byte[] currentSegment = builder.getCurrentSegment();

        // Assert
        assertNotNull("The initial segment should not be null", currentSegment);

        // The default size for a BYTE_WRITE_CONCAT_BUFFER from a standard BufferRecycler is 2000 bytes.
        final int expectedDefaultBufferSize = 2000;
        assertEquals("Initial buffer size should match the recycler's default",
                expectedDefaultBufferSize, currentSegment.length);
    }
}