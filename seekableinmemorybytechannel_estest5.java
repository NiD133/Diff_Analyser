package org.apache.commons.compress.utils;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;

/**
 * Tests for {@link SeekableInMemoryByteChannel}.
 */
public class SeekableInMemoryByteChannelTest {

    /**
     * Tests that reading from a position beyond the channel's size correctly
     * returns -1, indicating the end of the stream.
     */
    @Test
    public void readShouldReturnEndOfStreamWhenPositionIsBeyondSize() throws IOException {
        // Arrange: Create an empty channel and a destination buffer.
        // The channel is initially empty, so its size is 0.
        SeekableByteChannel channel = new SeekableInMemoryByteChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // Act: Position the channel far beyond its current size.
        // According to the SeekableByteChannel contract, this is a valid operation.
        channel.position(Integer.MAX_VALUE);
        final int bytesRead = channel.read(buffer);

        // Assert: The read operation should return -1, indicating the end of the stream,
        // as the position is past the end of the channel's content.
        assertEquals(-1, bytesRead);
    }
}