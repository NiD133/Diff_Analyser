package org.apache.commons.io.input.buffer;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link CircularByteBuffer}.
 */
public class CircularByteBufferTest {

    @Test
    public void getSpaceShouldDecreaseCorrectlyAfterAddingOneByte() {
        // Arrange: Create a buffer with the default capacity.
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final int expectedBytesInBuffer = 1;
        final int expectedSpace = IOUtils.DEFAULT_BUFFER_SIZE - expectedBytesInBuffer;

        // Act: Add a single byte to the buffer.
        buffer.add((byte) 42);

        // Assert: Verify the number of bytes and the remaining space are as expected.
        assertEquals("The buffer should contain exactly one byte after one was added.",
                expectedBytesInBuffer, buffer.getCurrentNumberOfBytes());
        assertEquals("The available space should be the total size minus one.",
                expectedSpace, buffer.getSpace());
    }
}