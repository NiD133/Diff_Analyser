package org.apache.commons.compress.utils;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Contains tests for the SeekableInMemoryByteChannel class.
 */
public class SeekableInMemoryByteChannelTest {

    @Test
    public void truncateOnClosedChannelShouldBeANoOpAndNotThrowException() throws IOException {
        // Arrange
        // The Javadoc for SeekableInMemoryByteChannel.truncate() states it intentionally
        // violates the SeekableByteChannel contract by NOT throwing an exception on a
        // closed channel. This test verifies this specific behavior.
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(); // Starts with size 0
        channel.close();

        // Pre-conditions to ensure our initial state is correct
        assertFalse("Precondition: channel should be closed", channel.isOpen());
        assertEquals("Precondition: channel size should be 0", 0, channel.size());

        // Act
        // The core of the test: calling truncate on a closed channel.
        // This line will cause the test to fail if any exception is thrown.
        channel.truncate(100L);

        // Assert
        // Verify that the channel's state remains completely unchanged.
        assertFalse("Channel should remain closed after calling truncate", channel.isOpen());
        assertEquals("Size should not change after calling truncate on a closed channel", 0, channel.size());
    }
}