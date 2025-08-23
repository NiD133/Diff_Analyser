package org.apache.commons.compress.utils;

import static org.junit.jupiter.api.Assertions.assertSame;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the factory methods of {@link MultiReadOnlySeekableByteChannel}.
 * This test focuses on the optimization behavior of the factory methods.
 */
class MultiReadOnlySeekableByteChannelFactoryTest {

    /**
     * The {@code forSeekableByteChannels} factory method is expected to return
     * the original channel instance if it's the only one provided. This is an
     * optimization to avoid wrapping a single channel unnecessarily.
     */
    @Test
    @DisplayName("forSeekableByteChannels should return the same instance when given a single channel")
    void forSeekableByteChannelsWithSingleChannelReturnsSameInstance() throws IOException {
        // Arrange: Create a single source channel.
        // A try-with-resources block ensures the channel is closed automatically.
        try (SeekableByteChannel singleChannel = new SeekableInMemoryByteChannel(ByteUtils.EMPTY_BYTE_ARRAY)) {

            // Act: Call the factory method with the single channel.
            SeekableByteChannel resultChannel = MultiReadOnlySeekableByteChannel.forSeekableByteChannels(singleChannel);

            // Assert: The result should be the exact same instance as the input.
            assertSame(singleChannel, resultChannel,
                "The factory method should return the original channel instance as an optimization.");
        }
    }
}