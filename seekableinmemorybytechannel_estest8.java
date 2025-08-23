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
    public void writeShouldReturnZeroWhenBufferHasNoRemainingBytes() throws IOException {
        // Arrange: Create a channel and a buffer.
        // The buffer is then "exhausted" by reading from the channel,
        // which sets its position equal to its limit.
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(1);
        ByteBuffer buffer = ByteBuffer.allocate(1);
        
        // Read one byte to exhaust the buffer (position becomes 1, limit is 1).
        channel.read(buffer);
        
        // Pre-condition check: ensure the buffer has no remaining bytes to write.
        assertEquals("Buffer should be exhausted before writing", 0, buffer.remaining());
        assertEquals("Channel position should be at the end after reading", 1L, channel.position());

        // Act: Attempt to write from the exhausted buffer to the channel.
        int bytesWritten = channel.write(buffer);

        // Assert: The write operation should do nothing.
        assertEquals("Bytes written should be 0 for an exhausted buffer", 0, bytesWritten);
        assertEquals("Channel position should not change after a no-op write", 1L, channel.position());
    }
}