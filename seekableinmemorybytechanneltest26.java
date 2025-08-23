package org.apache.commons.compress.utils;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SeekableInMemoryByteChannel}.
 */
class SeekableInMemoryByteChannelTest {

    private static final byte[] TEST_DATA = "Some data".getBytes(UTF_8);
    private SeekableInMemoryByteChannel channel;

    @BeforeEach
    void setUp() {
        // Use a fresh channel for each test to ensure isolation.
        channel = new SeekableInMemoryByteChannel();
    }

    /**
     * As per the SeekableByteChannel spec: "Setting the position to a value that is greater than the current size is
     * legal... A later attempt to write bytes at such a position will cause the entity to grow to accommodate the new
     * bytes."
     */
    @Test
    @DisplayName("Write should grow the channel when the position is set beyond the current size")
    void writeShouldGrowChannelWhenPositionIsAfterEnd() throws IOException {
        // Arrange
        final int positionBeyondEnd = 2;
        final long expectedSize = TEST_DATA.length + positionBeyondEnd;
        final ByteBuffer sourceBuffer = ByteBuffer.wrap(TEST_DATA);

        // Act
        channel.position(positionBeyondEnd);
        final int bytesWritten = channel.write(sourceBuffer);

        // Assert
        assertEquals(positionBeyondEnd, channel.position() - bytesWritten, "Position should be set correctly before write.");
        assertEquals(TEST_DATA.length, bytesWritten, "The number of bytes written should match the source buffer's length.");
        assertEquals(expectedSize, channel.size(), "Channel size should grow to accommodate the write position and data.");

        // Verify the written data can be read back correctly
        channel.position(positionBeyondEnd);
        final ByteBuffer readBuffer = ByteBuffer.allocate(TEST_DATA.length);
        channel.read(readBuffer);
        assertArrayEquals(TEST_DATA, readBuffer.array());
    }

    /**
     * This test verifies the documented behavior of SeekableInMemoryByteChannel, which deliberately deviates from the
     * SeekableByteChannel interface contract. The contract states that size() should throw a ClosedChannelException
     * if the channel is closed. This implementation does not, and instead returns the size at the time of closing.
     */
    @Test
    @DisplayName("size() on a closed channel should return the size at the time of close without throwing an exception")
    void sizeOnClosedChannelShouldReturnSizeAtTimeOfClose() throws IOException {
        // Arrange
        try (SeekableByteChannel populatedChannel = new SeekableInMemoryByteChannel(TEST_DATA)) {
            final long expectedSize = TEST_DATA.length;
            assertEquals(expectedSize, populatedChannel.size(), "Pre-condition: size should be correct before closing.");

            // Act
            populatedChannel.close();

            // Assert
            // Verify that size() can be called on a closed channel and returns the last known size.
            final long sizeAfterClose = assertDoesNotThrow(populatedChannel::size,
                "size() should not throw an exception on a closed channel.");
            assertEquals(expectedSize, sizeAfterClose, "Size after close should be the same as size before close.");
        }
    }
}