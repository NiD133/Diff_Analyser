package org.apache.commons.compress.utils;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SeekableByteChannel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SeekableInMemoryByteChannel}.
 */
class SeekableInMemoryByteChannelTest {

    private final byte[] testData = "Some data".getBytes(UTF_8);

    @Test
    @DisplayName("Writing past the end of the channel grows it, filling the gap with null bytes")
    void writePastEndShouldGrowChannel() throws IOException {
        // Arrange
        final int gapSize = 2;
        final byte[] expectedContent = new byte[gapSize + testData.length];
        // The implementation fills the gap with null bytes (\0), which is the default for a new byte array.
        System.arraycopy(testData, 0, expectedContent, gapSize, testData.length);

        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
            assertEquals(0, channel.size(), "Initial channel size should be 0");

            // Act: Position after the current end and write data.
            channel.position(gapSize);
            int bytesWritten = channel.write(ByteBuffer.wrap(testData));

            // Assert
            assertEquals(testData.length, bytesWritten, "Should write the full length of the data");
            assertEquals(expectedContent.length, channel.size(), "Channel size should grow to accommodate the gap and data");

            // Verify the entire content by reading it back from the beginning.
            byte[] actualContent = new byte[expectedContent.length];
            channel.position(0);
            channel.read(ByteBuffer.wrap(actualContent));

            assertArrayEquals(expectedContent, actualContent, "Channel content should include the gap and the written data");
        }
    }

    @Test
    @DisplayName("Setting the position on a closed channel should throw ClosedChannelException")
    void settingPositionOnClosedChannelShouldThrowException() throws IOException {
        // Arrange
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
            channel.close();

            // Act & Assert
            assertThrows(ClosedChannelException.class,
                () -> channel.position(0),
                "Attempting to set position on a closed channel should fail.");
        }
    }
}