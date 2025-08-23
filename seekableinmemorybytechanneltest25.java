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
class SeekableInMemoryByteChannelTest {

    /**
     * Tests that writing to a position beyond the current end of the channel
     * grows the channel and fills the intermediate space with unspecified bytes (zeroes in this implementation).
     * This behavior is consistent with the SeekableByteChannel interface documentation.
     *
     * @see java.nio.channels.SeekableByteChannel#write(ByteBuffer)
     */
    @Test
    void shouldGrowChannelWhenWritingPastEnd() throws IOException {
        // Arrange
        final byte[] dataToWrite = "Some data".getBytes(UTF_8);
        final int positionBeyondEnd = 2;
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) { // Initial size is 0
            // Act: Position the channel past the current end and write data.
            channel.position(positionBeyondEnd);
            final int bytesWritten = channel.write(ByteBuffer.wrap(dataToWrite));

            // Assert
            // 1. The write operation should report that all bytes were written.
            assertEquals(dataToWrite.length, bytesWritten);
            // 2. The new size should reflect the gap and the newly written data.
            assertEquals(positionBeyondEnd + dataToWrite.length, channel.size());

            // 3. Verify that the data can be read back correctly from its written position.
            channel.position(positionBeyondEnd);
            final ByteBuffer readBuffer = ByteBuffer.allocate(dataToWrite.length);
            channel.read(readBuffer);
            assertArrayEquals(dataToWrite, readBuffer.array());
        }
    }

    /**
     * Tests that reading the position of a closed channel does not throw an exception,
     * but instead returns the last known position. This is a documented, deliberate
     * deviation from the SeekableByteChannel interface specification.
     *
     * @see SeekableInMemoryByteChannel#position()
     */
    @Test
    void shouldReturnLastPositionOnClosedChannelWithoutException() throws IOException {
        // Arrange
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel("test data".getBytes(UTF_8))) {
            final long expectedPosition = 5L;
            channel.position(expectedPosition);
            channel.close(); // Close the channel

            // Act: Get the position from the closed channel.
            final long positionAfterClose = channel.position();

            // Assert: The position should be the last known position, and no exception should be thrown.
            // This test verifies the implementation's documented behavior, which violates the
            // standard contract that would require a ClosedChannelException.
            assertEquals(expectedPosition, positionAfterClose);
        }
    }
}