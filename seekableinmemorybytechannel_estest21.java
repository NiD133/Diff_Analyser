package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Unit tests for the {@link SeekableInMemoryByteChannel} class.
 */
public class SeekableInMemoryByteChannelTest {

    /**
     * Verifies that truncating a channel to zero correctly sets its size,
     * even after the internal position has been advanced by a read operation.
     */
    @Test
    public void truncateToZeroSetsSizeToZeroAfterPositionIsAdvanced() throws IOException {
        // Arrange: Create a channel with an initial size of 1 byte.
        final int initialSize = 1;
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(initialSize);
        assertEquals("Initial channel size should be 1.", initialSize, channel.size());

        // Act: Advance the position by reading the entire channel. This ensures
        // that the truncate operation is independent of the current position.
        ByteBuffer buffer = ByteBuffer.allocate(initialSize);
        channel.read(buffer);
        assertEquals("Position should be at the end after reading.", initialSize, channel.position());

        // Act: Truncate the channel to a size of 0.
        channel.truncate(0);

        // Assert: The channel's size should now be 0.
        assertEquals("Channel size should be 0 after truncation.", 0L, channel.size());
    }
}