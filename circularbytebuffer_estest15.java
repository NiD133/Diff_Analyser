package org.apache.commons.io.input.buffer;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link CircularByteBuffer}.
 */
public class CircularByteBufferTest {

    /**
     * Tests that adding a single byte to a new buffer correctly updates the
     * number of available bytes and the remaining space.
     */
    @Test
    public void addSingleByteShouldIncreaseCountAndDecreaseSpace() {
        // Arrange: Create a buffer with the default size.
        // The default size is IOUtils.DEFAULT_BUFFER_SIZE (8192 bytes).
        final CircularByteBuffer buffer = new CircularByteBuffer();

        // Act: Add a single byte to the buffer.
        buffer.add((byte) 0);

        // Assert: Check that the byte count is 1 and the available space has decreased by 1.
        final int expectedCount = 1;
        final int expectedSpace = IOUtils.DEFAULT_BUFFER_SIZE - 1; // 8192 - 1 = 8191

        assertEquals("The number of bytes in the buffer should be 1.",
                     expectedCount, buffer.getCurrentNumberOfBytes());
        assertEquals("The available space should be the total size minus 1.",
                     expectedSpace, buffer.getSpace());
    }
}