package org.apache.commons.compress.utils;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SeekableInMemoryByteChannel}.
 */
class SeekableInMemoryByteChannelTest {

    /**
     * Tests that writing to a position beyond the current end of the channel
     * creates a "gap" filled with zero bytes and correctly increases the channel's size.
     * This behavior is specified by the SeekableByteChannel interface.
     *
     * @see SeekableByteChannel#write(ByteBuffer)
     * @see SeekableByteChannel#position(long)
     */
    @Test
    void writePastEndOfChannelShouldCreateGapAndIncreaseSize() throws IOException {
        // Arrange
        final byte[] dataToWrite = "Some data".getBytes(UTF_8);
        final int gapSize = 2;
        final long expectedSize = gapSize + dataToWrite.length;
        final byte[] expectedContent = new byte[(int) expectedSize];
        // The gap from index 0 to gapSize-1 will be filled with default byte value (0).
        System.arraycopy(dataToWrite, 0, expectedContent, gapSize, dataToWrite.length);

        try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel()) {
            // Act: Position past the current end (which is 0) and write data.
            channel.position(gapSize);
            final int bytesWritten = channel.write(ByteBuffer.wrap(dataToWrite));

            // Assert
            assertEquals(dataToWrite.length, bytesWritten, "Number of bytes written should match data length.");
            assertEquals(expectedSize, channel.size(), "Channel size should be the sum of the gap and written data.");
            assertEquals(expectedSize, channel.position(), "Position should be at the new end of the channel.");

            // Verify the entire channel content, including the gap of null bytes.
            final byte[] actualContent = new byte[(int) channel.size()];
            channel.position(0);
            channel.read(ByteBuffer.wrap(actualContent));
            assertArrayEquals(expectedContent, actualContent, "Channel content should match expected content with the gap.");
        }
    }

    /**
     * Tests that attempting to set the position to a value greater than
     * Integer.MAX_VALUE throws an IOException. This is because the channel is backed by a
     * byte array, which cannot be indexed by a long.
     */
    @Test
    void positionShouldThrowIOExceptionWhenPositionIsBeyondMaxInt() throws IOException {
        // Arrange
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
            final long invalidPosition = (long) Integer.MAX_VALUE + 1;

            // Act & Assert
            assertThrows(IOException.class, () -> channel.position(invalidPosition),
                "Positioning beyond Integer.MAX_VALUE should throw an IOException.");
        }
    }
}