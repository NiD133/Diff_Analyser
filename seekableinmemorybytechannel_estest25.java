package org.apache.commons.compress.utils;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Tests for {@link SeekableInMemoryByteChannel}.
 */
public class SeekableInMemoryByteChannelTest {

    @Test
    public void readFromEmptyChannelShouldReturnEndOfStream() throws IOException {
        // Arrange: Create an empty channel and a destination buffer.
        SeekableInMemoryByteChannel emptyChannel = new SeekableInMemoryByteChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // Act: Attempt to read from the empty channel.
        int bytesRead = emptyChannel.read(buffer);

        // Assert: The read operation should return -1, indicating the end of the stream.
        assertEquals("Reading from an empty channel should return -1 for end-of-stream.", -1, bytesRead);
    }
}