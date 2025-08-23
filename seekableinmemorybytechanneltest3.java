package org.apache.commons.compress.utils;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SeekableInMemoryByteChannel}.
 */
class SeekableInMemoryByteChannelTest {

    private static final byte[] TEST_DATA = "Some data".getBytes(UTF_8);

    @Test
    @DisplayName("Read should return all bytes from a channel initialized with data")
    void readShouldReturnAllBytesFromChannelInitializedWithData() throws IOException {
        // Arrange
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel(TEST_DATA)) {
            final ByteBuffer readBuffer = ByteBuffer.allocate(TEST_DATA.length);

            // Act
            final int bytesRead = channel.read(readBuffer);

            // Assert
            assertEquals(TEST_DATA.length, bytesRead, "Should read the correct number of bytes");
            assertArrayEquals(TEST_DATA, readBuffer.array(), "The content read should match the initial data");
            assertEquals(TEST_DATA.length, channel.position(), "Position should be at the end after reading");
        }
    }

    @Test
    @DisplayName("Write should expand channel when position is set beyond its current size")
    void writeShouldExpandChannelWhenPositionIsBeyondSize() throws IOException {
        // This test verifies that writing to a position greater than the current size
        // grows the channel to accommodate the new bytes, as specified by the SeekableByteChannel interface.

        // Arrange
        final int gapSize = 2;
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) { // Starts empty
            assertEquals(0, channel.size(), "New channel should be empty");
            final ByteBuffer writeBuffer = ByteBuffer.wrap(TEST_DATA);

            // Act
            channel.position(gapSize);
            final int bytesWritten = channel.write(writeBuffer);

            // Assert
            // 1. Verify the write operation was successful.
            assertEquals(TEST_DATA.length, bytesWritten, "Should write the correct number of bytes");

            // 2. Verify the channel grew to include the gap and the written data.
            final long expectedSize = gapSize + TEST_DATA.length;
            assertEquals(expectedSize, channel.size(), "Size should include the initial gap and written data");
            assertEquals(expectedSize, channel.position(), "Position should be at the end after writing");

            // 3. Verify the written data can be read back correctly.
            final ByteBuffer readBuffer = ByteBuffer.allocate(TEST_DATA.length);
            channel.position(gapSize); // Move position back to the start of where data was written.
            final int bytesRead = channel.read(readBuffer);

            assertEquals(TEST_DATA.length, bytesRead, "Should read back the correct number of bytes");
            assertArrayEquals(TEST_DATA, readBuffer.array(), "The content read should match the data written");
        }
    }
}