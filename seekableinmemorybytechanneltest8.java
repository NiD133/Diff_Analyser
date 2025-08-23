package org.apache.commons.compress.utils;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for SeekableInMemoryByteChannel")
class SeekableInMemoryByteChannelTest {

    private static final byte[] TEST_DATA = "Some data".getBytes(UTF_8);

    @Test
    @DisplayName("Writing at a position beyond the current size should grow the channel, leaving a gap")
    void shouldGrowChannelWhenWritingPastCurrentSize() throws IOException {
        // Arrange
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
            final int gapSize = 2;
            final ByteBuffer writeBuffer = ByteBuffer.wrap(TEST_DATA);
            final long expectedSize = gapSize + TEST_DATA.length;
            final long expectedPositionAfterWrite = gapSize + TEST_DATA.length;

            // Act
            channel.position(gapSize);
            int bytesWritten = channel.write(writeBuffer);

            // Assert
            assertEquals(TEST_DATA.length, bytesWritten, "The number of bytes written should match the buffer size");
            assertEquals(expectedSize, channel.size(), "Channel size should grow to accommodate the gap and new data");
            assertEquals(expectedPositionAfterWrite, channel.position(), "Position should be at the end of the newly written data");

            // Verify the gap (bytes 0 and 1) is filled with null bytes
            byte[] gapData = new byte[gapSize];
            channel.position(0);
            channel.read(ByteBuffer.wrap(gapData));
            assertArrayEquals(new byte[]{0, 0}, gapData, "The gap should be filled with null bytes");

            // Verify the written data is correct
            byte[] writtenData = new byte[TEST_DATA.length];
            channel.position(gapSize);
            channel.read(ByteBuffer.wrap(writtenData));
            assertArrayEquals(TEST_DATA, writtenData, "The data read back should match the data written");
        }
    }

    @Test
    @DisplayName("Read should return -1 (EOF) when the position is exactly at the end of the channel")
    void readShouldReturnEOFWhenPositionIsAtSize() throws IOException {
        // Arrange
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel(TEST_DATA)) {
            ByteBuffer readBuffer = ByteBuffer.allocate(10);
            channel.position(channel.size()); // Position at the very end

            // Act
            int bytesRead = channel.read(readBuffer);

            // Assert
            assertEquals(-1, bytesRead, "read() should return -1 for EOF");
            assertEquals(0, readBuffer.position(), "Buffer position should not change on EOF");
        }
    }

    @Test
    @DisplayName("Read should return -1 (EOF) when the position is beyond the end of the channel")
    void readShouldReturnEOFWhenPositionIsBeyondSize() throws IOException {
        // Arrange
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel(TEST_DATA)) {
            ByteBuffer readBuffer = ByteBuffer.allocate(10);
            channel.position(channel.size() + 1); // Position past the end

            // Act
            int firstReadCount = channel.read(readBuffer);
            int secondReadCount = channel.read(readBuffer); // Subsequent read should also be EOF

            // Assert
            assertEquals(-1, firstReadCount, "First read should return -1 for EOF");
            assertEquals(-1, secondReadCount, "Subsequent read should also return -1");
            assertEquals(0, readBuffer.position(), "Buffer position should not change on EOF");
        }
    }
}