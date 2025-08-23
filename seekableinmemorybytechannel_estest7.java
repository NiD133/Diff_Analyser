package org.apache.commons.compress.utils;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

/**
 * Contains tests for the {@link SeekableInMemoryByteChannel} class.
 */
public class SeekableInMemoryByteChannelTest {

    /**
     * Verifies that setting the position to 0 on a new, empty channel
     * is a safe operation that does not alter the channel's state.
     */
    @Test
    public void positionToZeroOnEmptyChannelShouldNotChangeState() throws IOException {
        // Arrange: Create a new, empty channel.
        // A new channel is expected to have a position and size of 0.
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel();

        // Act: Set the position to the beginning of the channel.
        channel.position(0L);

        // Assert: Verify that the position and size remain unchanged.
        assertEquals("Position should remain at 0", 0L, channel.position());
        assertEquals("Size should remain 0", 0L, channel.size());
    }
}