package org.apache.commons.compress.utils;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SeekableByteChannel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the behavior of {@link MultiReadOnlySeekableByteChannel} when it is closed.
 */
class MultiReadOnlySeekableByteChannelClosedChannelTest {

    /**
     * Verifies that calling {@link SeekableByteChannel#size()} on a closed
     * {@link MultiReadOnlySeekableByteChannel} throws a {@link ClosedChannelException},
     * as per the contract of {@link SeekableByteChannel}.
     *
     * @throws IOException if an I/O error occurs during channel creation or closing.
     */
    @Test
    @DisplayName("Calling size() on a closed MultiReadOnlySeekableByteChannel should throw ClosedChannelException")
    void callingSizeOnClosedChannelShouldThrowException() throws IOException {
        // Arrange: Create a multi-channel from two empty sub-channels.
        // The content of the channels is not relevant for this test, only its closed state.
        // SeekableInMemoryByteChannel is a test utility class that provides an in-memory channel.
        SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forSeekableByteChannels(
            new SeekableInMemoryByteChannel(new byte[0]),
            new SeekableInMemoryByteChannel(new byte[0])
        );

        // Act: Close the channel to put it into the state under test.
        channel.close();

        // Assert: Verify that calling size() now throws a ClosedChannelException.
        assertThrows(ClosedChannelException.class, channel::size,
            "Should not be able to get size from a closed channel.");
    }
}