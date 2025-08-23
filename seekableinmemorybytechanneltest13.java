package org.apache.commons.compress.utils;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SeekableInMemoryByteChannel}.
 */
class SeekableInMemoryByteChannelTest {

    private final byte[] sampleData = "Some data".getBytes(UTF_8);

    /**
     * Tests that writing to a position beyond the current end of the channel
     * correctly grows the channel size and leaves a gap, as per the
     * {@link SeekableByteChannel} contract. For this implementation, the gap
     * is filled with null bytes.
     */
    @Test
    void shouldGrowChannelWhenWritingPastEnd() throws IOException {
        // Arrange
        final int gapSize = 2;
        final byte[] expectedContent = new byte[gapSize + sampleData.length];
        System.arraycopy(sampleData, 0, expectedContent, gapSize, sampleData.length);

        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
            // Act: Position the channel past the current end (0) and write data.
            channel.position(gapSize);
            final int bytesWritten = channel.write(ByteBuffer.wrap(sampleData));

            // Assert
            assertEquals(sampleData.length, bytesWritten,
                "The number of bytes written should match the source data length.");
            assertEquals(expectedContent.length, channel.size(),
                "The new channel size should include the gap and the written data.");

            // Verify the entire content by reading it back.
            final byte[] actualContent = new byte[(int) channel.size()];
            channel.position(0);
            channel.read(ByteBuffer.wrap(actualContent));

            assertArrayEquals(expectedContent, actualContent,
                "The channel should contain a gap of null bytes followed by the written data.");
        }
    }

    /**
     * Tests that truncating the channel to a shorter length correctly reduces
     * its size, discards the extra data, and adjusts the position if it was
     * beyond the new size.
     */
    @Test
    void truncateShouldReduceSizeAndContentAndAdjustPosition() throws IOException {
        // Arrange
        final int truncatedSize = 4;
        final byte[] expectedData = "Some".getBytes(UTF_8);

        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel(sampleData)) {
            // Set position to the end, which is beyond the new truncated size.
            channel.position(sampleData.length);

            // Act
            channel.truncate(truncatedSize);

            // Assert
            assertEquals(truncatedSize, channel.size(),
                "Channel size should be reduced to the specified truncated size.");
            assertEquals(truncatedSize, channel.position(),
                "Position should be moved to the new, truncated size.");

            // Verify the content is correctly truncated by reading it back.
            final byte[] actualData = new byte[truncatedSize];
            channel.position(0);
            final int bytesRead = channel.read(ByteBuffer.wrap(actualData));

            assertEquals(truncatedSize, bytesRead, "Should read the exact number of truncated bytes.");
            assertArrayEquals(expectedData, actualData, "Channel content should match the expected truncated data.");
        }
    }
}