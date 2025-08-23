package org.apache.commons.io.input.buffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit tests for {@link CircularByteBuffer}.
 */
public class CircularByteBufferTest {

    /**
     * Tests that hasBytes() correctly returns true when the buffer is not empty.
     */
    @Test
    public void testHasBytesReturnsTrueWhenBufferIsNotEmpty() {
        // Arrange: Create a buffer and add a single byte to it.
        final CircularByteBuffer buffer = new CircularByteBuffer();
        buffer.add((byte) 42);

        // Act: Check if the buffer has bytes.
        final boolean result = buffer.hasBytes();

        // Assert: Verify that the buffer reports it has bytes and its size is 1.
        assertTrue("hasBytes() should return true for a non-empty buffer.", result);
        assertEquals("The buffer should contain exactly one byte.", 1, buffer.getCurrentNumberOfBytes());
    }
}