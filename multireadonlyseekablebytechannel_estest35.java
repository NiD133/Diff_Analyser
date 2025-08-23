package org.apache.commons.compress.utils;

import org.junit.Test;
import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link MultiReadOnlySeekableByteChannel} class.
 */
public class MultiReadOnlySeekableByteChannelTest {

    /**
     * Verifies that creating a composite channel from an empty array of channels
     * results in a valid, empty channel. An empty channel should have a size
     * and an initial position of zero.
     */
    @Test
    public void forSeekableByteChannelsWithEmptyArrayReturnsEmptyChannel() throws IOException {
        // Arrange: Define an empty source of channels.
        SeekableByteChannel[] emptyChannelArray = new SeekableByteChannel[0];

        // Act: Create a multi-channel from the empty array.
        SeekableByteChannel resultChannel = MultiReadOnlySeekableByteChannel.forSeekableByteChannels(emptyChannelArray);

        // Assert: The resulting channel should behave as an empty channel.
        assertEquals("Position of an empty combined channel should be 0", 0L, resultChannel.position());
        assertEquals("Size of an empty combined channel should be 0", 0L, resultChannel.size());
    }
}