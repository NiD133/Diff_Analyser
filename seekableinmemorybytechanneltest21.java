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

    private static final byte[] TEST_DATA = "Some data".getBytes(UTF_8);

    /**
     * Tests that writing to a position beyond the current end of the channel
     * grows the channel and leaves a "gap" of unspecified bytes.
     *
     * <p>This behavior is specified in the {@link SeekableByteChannel} interface:
     * "A later attempt to write bytes at such a position will cause the entity
     * to grow to accommodate the new bytes..."</p>
     */
    @Test
    void writingPastEndShouldGrowChannelAndLeaveGap() throws IOException {
        // Arrange
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
            final int gapSize = 2;
            final long positionToWrite = channel.size() + gapSize;
            final ByteBuffer writeBuffer = ByteBuffer.wrap(TEST_DATA);
            final long expectedSize = positionToWrite + TEST_DATA.length;
            final long expectedPositionAfterWrite = expectedSize;

            // Act
            channel.position(positionToWrite);
            final int bytesWritten = channel.write(writeBuffer);

            // Assert
            assertEquals(TEST_DATA.length, bytesWritten, "Should write all bytes from the buffer.");
            assertEquals(expectedSize, channel.size(), "Channel size should grow to accommodate the gap and the new data.");
            assertEquals(expectedPositionAfterWrite, channel.position(), "Position should be at the end of the newly written data.");

            // Verify that the written data can be read back correctly
            final ByteBuffer readBuffer = ByteBuffer.allocate(TEST_DATA.length);
            channel.position(positionToWrite);
            channel.read(readBuffer);
            assertArrayEquals(TEST_DATA, readBuffer.array());
        }
    }

    /**
     * Tests that truncating the channel adjusts the position if the current
     * position is greater than the new size.
     *
     * <p>This behavior is specified in the {@link SeekableByteChannel} interface:
     * "if the current position is greater than the given size then it is set to that size."</p>
     */
    @Test
    void truncatingShouldMovePositionIfGreaterThanNewSize() throws IOException {
        // Arrange
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel(TEST_DATA)) {
            final long originalSize = channel.size();
            // Set position far beyond the current size
            channel.position(originalSize * 2);

            // Act
            // Truncate to the original size, which is less than the current position.
            channel.truncate(originalSize);

            // Assert
            assertEquals(originalSize, channel.size(), "Size should be truncated as requested.");
            assertEquals(originalSize, channel.position(), "Position should be moved back to the new size.");
        }
    }
}