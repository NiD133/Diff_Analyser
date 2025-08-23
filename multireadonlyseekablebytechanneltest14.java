package org.apache.commons.compress.utils;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SeekableByteChannel;
import org.junit.jupiter.api.Test;

/**
 * Tests for the behavior of {@link MultiReadOnlySeekableByteChannel} when it is closed.
 */
class MultiReadOnlySeekableByteChannelClosedChannelTest {

    /**
     * The {@link SeekableByteChannel} interface contract specifies that
     * attempting to modify the position of a closed channel must result
     * in a {@link ClosedChannelException}. This test verifies that
     * {@link MultiReadOnlySeekableByteChannel} adheres to this contract.
     *
     * @see SeekableByteChannel#position(long)
     */
    @Test
    void positioningAClosedChannelShouldThrowException() throws IOException {
        // Arrange: Create a multi-channel from two empty underlying channels.
        // The content of the channels is irrelevant for this test.
        final SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forSeekableByteChannels(
            new SeekableInMemoryByteChannel(),
            new SeekableInMemoryByteChannel()
        );

        // Act: Close the channel.
        channel.close();

        // Assert: Attempting to set the position on the closed channel should fail.
        assertThrows(ClosedChannelException.class, () -> channel.position(0),
            "Expected a ClosedChannelException when setting position on a closed channel.");
    }
}