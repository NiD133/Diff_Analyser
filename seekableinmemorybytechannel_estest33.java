package org.apache.commons.compress.utils;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link SeekableInMemoryByteChannel}.
 */
public class SeekableInMemoryByteChannelTest {

    /**
     * Verifies that a newly created channel using the default constructor
     * has an initial position of 0.
     */
    @Test
    public void newChannelShouldHavePositionZero() {
        // Arrange: Create a new, empty channel.
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel();

        // Act: Get the channel's position.
        long position = channel.position();

        // Assert: The initial position should be 0.
        assertEquals(0L, position);
    }
}