package org.apache.commons.compress.utils;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link SeekableInMemoryByteChannel} class, focusing on the truncate functionality.
 */
public class SeekableInMemoryByteChannelTest {

    @Test
    public void truncateToZeroShouldSetSizeToZero() {
        // Arrange: Create a channel with a non-zero initial size.
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(1);

        // Act: Truncate the channel to a size of 0.
        // The 'L' suffix clarifies that the argument is a long, matching the method signature.
        channel.truncate(0L);

        // Assert: The channel's size should be updated to 0.
        assertEquals("The channel size should be 0 after being truncated to zero.", 0L, channel.size());
    }
}