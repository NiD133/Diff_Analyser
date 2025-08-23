package org.apache.commons.compress.utils;

import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;

/**
 * Unit tests for the SeekableInMemoryByteChannel class.
 */
public class SeekableInMemoryByteChannelTest {

    /**
     * Verifies that attempting to write to a channel that has been closed
     * results in a ClosedChannelException.
     */
    @Test(expected = ClosedChannelException.class)
    public void writeToClosedChannelShouldThrowException() throws IOException {
        // Arrange: Create a channel, close it, and prepare a buffer for writing.
        final SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel();
        channel.close();
        final ByteBuffer buffer = ByteBuffer.allocate(1);

        // Act: Attempt to write to the closed channel.
        // This action is expected to throw the ClosedChannelException.
        channel.write(buffer);

        // Assert: The test passes if the expected exception is thrown.
        // This is handled automatically by the @Test(expected=...) annotation.
    }
}