package org.apache.commons.compress.utils;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SeekableInMemoryByteChannel} focusing on behavior when positioning beyond the channel's size.
 */
class SeekableInMemoryByteChannelTest {

    private static final byte[] TEST_DATA = "Some data".getBytes(UTF_8);

    /**
     * The SeekableByteChannel Javadoc states that writing after the current end-of-file is legal and will grow the channel.
     * The content of the "gap" created is officially "unspecified". This test verifies that our implementation grows the
     * channel and fills the gap with null bytes.
     */
    @Test
    void writeShouldExpandChannelAndFillGapWhenPositionIsAfterEnd() throws IOException {
        // Arrange
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
            final int gapSize = 2;
            final ByteBuffer dataToWrite = ByteBuffer.wrap(TEST_DATA);
            final byte[] expectedData = new byte[gapSize + TEST_DATA.length];
            System.arraycopy(TEST_DATA, 0, expectedData, gapSize, TEST_DATA.length);

            // Act
            channel.position(gapSize);
            final int bytesWritten = channel.write(dataToWrite);

            // Assert
            assertEquals(TEST_DATA.length, bytesWritten, "Should write the full length of the input data.");
            assertEquals(expectedData.length, channel.size(), "Channel size should equal the gap plus written data.");

            // Verify the entire channel content, including the gap
            final byte[] actualData = new byte[(int) channel.size()];
            channel.position(0);
            channel.read(ByteBuffer.wrap(actualData));
            assertArrayEquals(expectedData, actualData, "Channel content should have a null-byte gap followed by the data.");
        }
    }

    /**
     * The SeekableByteChannel Javadoc states that setting the position beyond the current size is legal,
     * but a subsequent read should immediately return an end-of-file (-1).
     */
    @Test
    void readShouldReturnEofWhenPositionIsAfterEnd() throws IOException {
        // Arrange
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) { // Start with an empty channel
            final ByteBuffer readBuffer = ByteBuffer.allocate(10);
            final long positionAfterEnd = 5;

            // Act
            channel.position(positionAfterEnd);
            final int bytesRead = channel.read(readBuffer);

            // Assert
            assertEquals(-1, bytesRead, "Reading from a position after the end should return -1 (EOF).");
            assertEquals(0, channel.size(), "Positioning and attempting to read should not change the channel's size.");
            assertEquals(positionAfterEnd, channel.position(), "Position should remain where it was set.");
        }
    }
}