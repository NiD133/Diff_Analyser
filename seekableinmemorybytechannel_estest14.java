package org.apache.commons.compress.utils;

import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;

/**
 * Contains tests for the {@link SeekableInMemoryByteChannel} class.
 */
public class SeekableInMemoryByteChannelTest {

    /**
     * Verifies that attempting to read from a channel that has been closed
     * results in a {@link ClosedChannelException}.
     */
    @Test(expected = ClosedChannelException.class)
    public void readFromClosedChannelShouldThrowClosedChannelException() throws IOException {
        // Arrange: Create a channel and then immediately close it.
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel();
        channel.close();
        ByteBuffer buffer = ByteBuffer.allocate(128);

        // Act: Attempt to read data from the closed channel.
        // The exception is expected here.
        channel.read(buffer);

        // Assert: The test passes if a ClosedChannelException is thrown,
        // which is handled by the @Test(expected=...) annotation.
    }
}