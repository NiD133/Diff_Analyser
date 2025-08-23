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
 * Tests for positioning and writing behavior in {@link SeekableInMemoryByteChannel}.
 * This suite focuses on the requirements of the {@link SeekableByteChannel} interface.
 */
public class SeekableInMemoryByteChannelPositioningTest {

    /**
     * Tests that writing to a position beyond the current end of the channel
     * grows the channel and leaves a "gap". This behavior is specified by the
     * SeekableByteChannel interface. The gap's content is unspecified by the
     * interface, but this implementation fills it with null bytes.
     */
    @Test
    void writeAtPositionBeyondSizeShouldGrowChannel() throws IOException {
        // Arrange
        final byte[] dataToWrite = "Some data".getBytes(UTF_8);
        final int positionBeyondSize = 2;
        final long expectedSize = positionBeyondSize + dataToWrite.length;

        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
            assertEquals(0, channel.size(), "Initial size should be 0");

            // Act: Set position past the end and write data.
            channel.position(positionBeyondSize);
            final int bytesWritten = channel.write(ByteBuffer.wrap(dataToWrite));

            // Assert: Verify size, position, and number of bytes written.
            assertEquals(dataToWrite.length, bytesWritten, "Should write all bytes from the buffer");
            assertEquals(expectedSize, channel.size(), "Channel size should grow to accommodate the gap and new data");
            assertEquals(expectedSize, channel.position(), "Position should be at the end of the written data");

            // Assert: Verify the gap is filled with null bytes.
            final byte[] gap = new byte[positionBeyondSize];
            channel.position(0);
            channel.read(ByteBuffer.wrap(gap));
            assertArrayEquals(new byte[]{0, 0}, gap, "The gap should be filled with null bytes");

            // Assert: Verify the written data can be read back correctly.
            final ByteBuffer readBuffer = ByteBuffer.allocate(dataToWrite.length);
            channel.position(positionBeyondSize);
            channel.read(readBuffer);
            assertArrayEquals(dataToWrite, readBuffer.array(), "The data written should be readable from the new position");
        }
    }

    /**
     * Tests that attempting to set a negative position throws an IOException,
     * as required by the SeekableByteChannel interface.
     */
    @Test
    void positionShouldThrowIOExceptionForNegativeValue() throws IOException {
        // Arrange
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
            // Act & Assert
            assertThrows(IOException.class, () -> channel.position(-1),
                "Setting a negative position should throw IOException");
        }
    }
}