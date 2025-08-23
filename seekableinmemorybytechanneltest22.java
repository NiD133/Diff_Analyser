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
     * Tests that writing to a position beyond the current channel size grows the channel.
     * The Javadoc for SeekableByteChannel states: "Setting the position to a value that is
     * greater than the current size is legal... A later attempt to write bytes at such a
     * position will cause the entity to grow to accommodate the new bytes."
     */
    @Test
    void shouldGrowChannelWhenWritingPastEnd() throws IOException {
        // Arrange
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
            final int positionAfterEnd = 2;
            final ByteBuffer dataToWrite = ByteBuffer.wrap(TEST_DATA);

            // Act
            channel.position(positionAfterEnd);
            final int bytesWritten = channel.write(dataToWrite);

            // Assert
            // Verify all data was written
            assertEquals(TEST_DATA.length, bytesWritten);
            // Verify the new size includes the gap created by seeking past the end
            assertEquals(TEST_DATA.length + positionAfterEnd, channel.size());

            // Verify the data can be read back correctly from its written position
            channel.position(positionAfterEnd);
            final ByteBuffer readBuffer = ByteBuffer.allocate(TEST_DATA.length);
            channel.read(readBuffer);
            assertArrayEquals(TEST_DATA, readBuffer.array());
        }
    }

    /**
     * Tests that if the channel is truncated to a size smaller than the current position,
     * the position is moved to the new end of the channel (the new size). The Javadoc for
     * SeekableByteChannel states: "if the current position is greater than the given
     * size then it is set to that size."
     */
    @Test
    void shouldMovePositionWhenTruncatingToSizeSmallerThanPosition() throws IOException {
        // Arrange
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel(TEST_DATA)) {
            channel.position(4);
            final long newSize = 3;

            // Act
            channel.truncate(newSize);

            // Assert
            assertEquals(newSize, channel.size(), "Channel size should be truncated.");
            assertEquals(newSize, channel.position(), "Position should be moved to the new size.");
        }
    }
}