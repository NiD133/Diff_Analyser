package org.apache.commons.compress.utils;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link SeekableInMemoryByteChannel}.
 */
class SeekableInMemoryByteChannelTest {

    /**
     * Tests that writing past the end of the channel grows it, as specified by the SeekableByteChannel interface.
     * <p>
     * From {@link SeekableByteChannel#write(ByteBuffer)}: "Setting the position to a value that is greater than the
     * current size is legal but does not change the size of the entity. A later attempt to write bytes at such a
     * position will cause the entity to grow to accommodate the new bytes..."
     * </p>
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    @DisplayName("Writing data past the current end of the channel should grow it")
    void writingPastEndOfFileShouldGrowChannel() throws IOException {
        // Arrange
        final byte[] dataToWrite = "Some data".getBytes(UTF_8);
        final int positionToStartWriting = 2;
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
            assertEquals(0, channel.size(), "Initial channel size should be 0.");

            // Act: Set position past the current end (size=0) and write data.
            channel.position(positionToStartWriting);
            final int bytesWritten = channel.write(ByteBuffer.wrap(dataToWrite));

            // Assert
            // 1. Check that the write operation reported success and the channel grew as expected.
            assertEquals(dataToWrite.length, bytesWritten, "The number of bytes written should match the input data length.");
            long expectedSize = positionToStartWriting + dataToWrite.length;
            assertEquals(expectedSize, channel.size(), "Channel size should have grown to accommodate the write position.");

            // 2. Verify that the written data can be read back correctly.
            channel.position(positionToStartWriting);
            final ByteBuffer readBuffer = ByteBuffer.allocate(dataToWrite.length);
            final int bytesRead = channel.read(readBuffer);

            assertEquals(dataToWrite.length, bytesRead, "Should be able to read back the exact number of bytes written.");
            assertArrayEquals(dataToWrite, readBuffer.array(), "The data read back should be identical to the data written.");
        }
    }

    /**
     * Tests that attempting to truncate the channel to a negative size throws an IllegalArgumentException,
     * as specified by the {@link SeekableByteChannel#truncate(long)} contract.
     *
     * @throws IOException if an I/O error occurs during channel closing.
     */
    @Test
    @DisplayName("Truncating the channel to a negative size should throw IllegalArgumentException")
    void truncateWithNegativeSizeShouldThrowException() throws IOException {
        // Arrange
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
            // Act & Assert
            assertThrows(IllegalArgumentException.class,
                () -> channel.truncate(-1),
                "truncate() should not accept a negative size.");
        }
    }
}