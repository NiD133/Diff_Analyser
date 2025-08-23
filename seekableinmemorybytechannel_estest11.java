package org.apache.commons.compress.utils;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Contains tests for the {@link SeekableInMemoryByteChannel} class.
 * This class focuses on verifying the behavior of channel operations like read, write, and position.
 */
public class SeekableInMemoryByteChannelTest {

    /**
     * Verifies that the channel's position is correctly advanced after a read operation.
     * When data is read from the channel, its internal position should move forward
     * by the number of bytes that were successfully read.
     */
    @Test
    public void positionShouldAdvanceAfterReading() throws IOException {
        // Arrange: Create a channel with a size of 1 byte, and a destination buffer
        // that has more capacity than the channel's content.
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(1);
        ByteBuffer destinationBuffer = ByteBuffer.allocate(3);

        // Pre-condition check: Ensure the channel starts at position 0.
        assertEquals("Initial position should be 0", 0L, channel.position());

        // Act: Read from the channel into the buffer.
        int bytesRead = channel.read(destinationBuffer);

        // Assert: The read operation should consume the single byte from the channel,
        // and the channel's position should advance accordingly.
        assertEquals("Should have read 1 byte from the channel", 1, bytesRead);
        assertEquals("Position should be advanced to 1 after reading", 1L, channel.position());
    }
}