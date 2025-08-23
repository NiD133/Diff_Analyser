package org.apache.commons.compress.utils;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the SeekableInMemoryByteChannel class.
 */
public class SeekableInMemoryByteChannelTest {

    /**
     * Verifies that a new SeekableInMemoryByteChannel, created with the default
     * constructor, has an initial size of zero.
     */
    @Test
    public void newlyCreatedChannelShouldHaveZeroSize() {
        // Arrange: Create a new channel, which should be empty by default.
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel();

        // Act: Get the size of the newly created channel.
        long size = channel.size();

        // Assert: The size should be 0.
        assertEquals("A new channel created without initial data should have a size of 0.", 0L, size);
    }
}