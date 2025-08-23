package org.apache.commons.compress.utils;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;

/**
 * Unit tests for the {@link SeekableInMemoryByteChannel} class.
 */
public class SeekableInMemoryByteChannelTest {

    /**
     * Verifies that writing an empty ByteBuffer to the channel writes zero bytes
     * and does not alter the channel's position or size.
     */
    @Test
    public void writeWithEmptyBufferShouldReturnZeroAndNotChangeState() throws IOException {
        // Arrange: Create a channel with 5 bytes of initial data.
        final byte[] initialData = new byte[5];
        final SeekableByteChannel channel = new SeekableInMemoryByteChannel(initialData);
        final ByteBuffer emptyBuffer = ByteBuffer.allocate(0);

        // Act: Attempt to write the contents of the empty buffer to the channel.
        final int bytesWritten = channel.write(emptyBuffer);

        // Assert: Check that the write operation had no effect.
        assertEquals("Writing an empty buffer should result in 0 bytes written.", 0, bytesWritten);
        assertEquals("The channel position should not change.", 0L, channel.position());
        assertEquals("The channel size should remain unchanged.", 5L, channel.size());
    }
}