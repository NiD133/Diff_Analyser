package org.apache.commons.compress.utils;

import org.junit.Test;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;

/**
 * Unit tests for the {@link SeekableInMemoryByteChannel} class.
 */
public class SeekableInMemoryByteChannelTest {

    /**
     * Verifies that attempting to set the position on a closed channel
     * throws a ClosedChannelException, as expected by the SeekableByteChannel contract.
     */
    @Test(expected = ClosedChannelException.class)
    public void positionShouldThrowExceptionWhenChannelIsClosed() throws IOException {
        // Arrange: Create a channel and immediately close it.
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel();
        channel.close();

        // Act: Attempt to change the position of the closed channel.
        // This action is expected to throw the exception.
        channel.position(100L);

        // Assert: The test passes if a ClosedChannelException is thrown,
        // which is handled by the @Test(expected=...) annotation.
    }
}