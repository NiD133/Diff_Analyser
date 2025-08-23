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
public class SeekableInMemoryByteChannelTest {

    private static final byte[] TEST_DATA = "Some data".getBytes(UTF_8);

    /**
     * Tests that writing to a position beyond the current end of the channel
     * grows the channel and leaves the intermediate bytes unspecified.
     *
     * <p>As per the SeekableByteChannel documentation:
     * "Setting the position to a value that is greater than the current size is legal
     * but does not change the size of the entity. A later attempt to write bytes at
     * such a position will cause the entity to grow to accommodate the new bytes..."
     * </p>
     */
    @Test
    void shouldGrowChannelWhenWritingPastEnd() throws IOException {
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
            // Arrange
            final int gapSize = 2;
            final long writePosition = gapSize;
            final long expectedSize = TEST_DATA.length + gapSize;
            final long expectedPositionAfterWrite = writePosition + TEST_DATA.length;
            final ByteBuffer inData = ByteBuffer.wrap(TEST_DATA);

            // Act
            channel.position(writePosition);
            final int bytesWritten = channel.write(inData);

            // Assert: Verify channel state after writing
            assertEquals(TEST_DATA.length, bytesWritten, "Should write all bytes from the buffer.");
            assertEquals(expectedSize, channel.size(), "Channel size should grow to accommodate the gap and new data.");
            assertEquals(expectedPositionAfterWrite, channel.position(), "Position should be at the end of the written data.");

            // Assert: Verify data integrity by reading it back
            channel.position(writePosition);
            final ByteBuffer readBuffer = ByteBuffer.allocate(TEST_DATA.length);
            channel.read(readBuffer);
            assertArrayEquals(TEST_DATA, readBuffer.array(), "Data written past the end should be readable from the correct position.");
        }
    }

    /**
     * Tests that truncating a channel adjusts the position if it's beyond the new size,
     * even when the truncation size is larger than the current channel size (which results in no size change).
     *
     * <p>As per the SeekableByteChannel#truncate documentation:
     * "if the current position is greater than the given size then it is set to that size."
     * </p>
     */
    @Test
    void shouldAdjustPositionWhenTruncatingWithPositionBeyondNewSize() throws IOException {
        // Arrange
        final int initialSize = TEST_DATA.length;
        final long positionPastEnd = initialSize * 2L;
        // A truncate size larger than the initial size, but smaller than the current position.
        final long truncateSize = initialSize + 1L;

        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel(TEST_DATA)) {
            channel.position(positionPastEnd);
            assertEquals(positionPastEnd, channel.position(), "Pre-condition: position is set past the end.");

            // Act
            channel.truncate(truncateSize);

            // Assert
            // Truncating to a larger size should not change the channel's content size.
            assertEquals(initialSize, channel.size(), "Size should not change when truncating to a larger size.");
            // But the position should be moved to the truncate size, as it was beyond it.
            assertEquals(truncateSize, channel.position(), "Position should be moved to the truncate size.");
        }
    }
}