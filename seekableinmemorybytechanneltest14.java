package org.apache.commons.compress.utils;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for writing data to a {@link SeekableInMemoryByteChannel}.
 */
@DisplayName("Tests for writing to a SeekableInMemoryByteChannel")
class SeekableInMemoryByteChannelWriteTest {

    private static final byte[] TEST_DATA = "Some data".getBytes(UTF_8);

    @Test
    @DisplayName("should append data and correctly update size and position when writing to a channel")
    void writeAppendsDataAndUpdatesState() throws IOException {
        // Arrange
        try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel()) {
            final ByteBuffer bufferToWrite = ByteBuffer.wrap(TEST_DATA);
            final int dataLength = TEST_DATA.length;

            // Act
            final int bytesWritten = channel.write(bufferToWrite);

            // Assert
            final byte[] channelContent = Arrays.copyOf(channel.array(), (int) channel.size());
            assertAll("After writing, channel state should be updated correctly",
                () -> assertEquals(dataLength, bytesWritten, "Should return the number of bytes written"),
                () -> assertEquals(dataLength, channel.size(), "Size should match the written data length"),
                () -> assertEquals(dataLength, channel.position(), "Position should be at the end of written data"),
                () -> assertArrayEquals(TEST_DATA, channelContent, "The channel's content should match the written data")
            );
        }
    }

    /**
     * This test verifies behavior described in the SeekableByteChannel interface:
     * "Setting the position to a value that is greater than the current size is legal...
     * A later attempt to write bytes at such a position will cause the entity to grow...
     * the values of any bytes between the previous end-of-file and the newly-written bytes are unspecified."
     *
     * Note: This implementation fills the gap with zero bytes, which is a common and reasonable behavior.
     */
    @Test
    @DisplayName("should grow the channel and pad the gap when writing past the current end")
    void writingPastEndGrowsChannelAndPadsGap() throws IOException {
        // Arrange
        try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel()) {
            final int gapSize = 2;
            final ByteBuffer dataToWrite = ByteBuffer.wrap(TEST_DATA);

            // The expected content will have a gap at the beginning, followed by the test data.
            // This implementation fills the gap with null bytes (zeros).
            final byte[] expectedContent = new byte[gapSize + TEST_DATA.length];
            System.arraycopy(TEST_DATA, 0, expectedContent, gapSize, TEST_DATA.length);

            // Act
            channel.position(gapSize);
            final int bytesWritten = channel.write(dataToWrite);

            // Assert
            final long expectedSize = gapSize + TEST_DATA.length;
            final long expectedPosition = expectedSize;
            final byte[] actualContent = Arrays.copyOf(channel.array(), (int) channel.size());

            assertAll("Writing past the end of the channel",
                () -> assertEquals(TEST_DATA.length, bytesWritten, "Should return the number of bytes written"),
                () -> assertEquals(expectedSize, channel.size(), "Size should include the gap and the written data"),
                () -> assertEquals(expectedPosition, channel.position(), "Position should be at the new end of the channel"),
                () -> assertArrayEquals(expectedContent, actualContent, "Channel content should be padded, followed by the data")
            );

            // Also verify that the written data can be read back correctly
            final ByteBuffer readBuffer = ByteBuffer.allocate(TEST_DATA.length);
            channel.position(gapSize);
            channel.read(readBuffer);
            assertArrayEquals(TEST_DATA, readBuffer.array(), "Reading back the data from its offset should succeed");
        }
    }
}