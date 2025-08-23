package org.apache.commons.io.input.buffer;

import static org.junit.Assert.assertEquals;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

/**
 * Contains tests for the {@link CircularByteBuffer} class.
 */
public class CircularByteBufferTest {

    /**
     * Tests that calling read() with a length of zero is a no-op
     * and does not alter the buffer's state.
     */
    @Test
    public void readWithZeroLengthShouldNotChangeBufferState() {
        // Arrange: Create an empty buffer with the default size.
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final byte[] destination = new byte[10]; // A non-empty destination buffer.
        final int expectedSpace = IOUtils.DEFAULT_BUFFER_SIZE;

        // Pre-condition check: Ensure the buffer is empty and has full space.
        assertEquals("Buffer should initially be empty.", 0, buffer.getCurrentNumberOfBytes());
        assertEquals("Initial space should equal the default buffer size.", expectedSpace, buffer.getSpace());

        // Act: Attempt to read zero bytes from the buffer.
        buffer.read(destination, 0, 0);

        // Assert: Verify that the buffer's state remains unchanged.
        assertEquals("Buffer should still be empty after reading zero bytes.", 0, buffer.getCurrentNumberOfBytes());
        assertEquals("Space should be unchanged after reading zero bytes.", expectedSpace, buffer.getSpace());
    }
}