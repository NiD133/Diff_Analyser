package org.apache.commons.compress.utils;

import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link SeekableInMemoryByteChannel}.
 */
public class SeekableInMemoryByteChannelTest {

    /**
     * Verifies that attempting to read into an empty ByteBuffer (zero capacity)
     * results in zero bytes being read and does not change the channel's position.
     * This is consistent with the contract of {@link java.nio.channels.ReadableByteChannel#read(ByteBuffer)}.
     */
    @Test
    public void readWithEmptyBufferShouldReturnZeroAndNotChangePosition() throws IOException {
        // Arrange: Create a channel with some data and set a non-zero starting position.
        byte[] channelData = new byte[10];
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(channelData);
        
        long initialPosition = 5L;
        channel.position(initialPosition);

        ByteBuffer emptyBuffer = ByteBuffer.allocate(0);

        // Act: Attempt to read from the channel into the empty buffer.
        int bytesRead = channel.read(emptyBuffer);

        // Assert: Verify that no bytes were read and the position is unchanged.
        assertEquals("Should read 0 bytes when the destination buffer is empty", 0, bytesRead);
        assertEquals("Channel position should not change after reading into an empty buffer",
                initialPosition, channel.position());
    }
}