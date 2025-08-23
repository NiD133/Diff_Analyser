package org.apache.commons.compress.utils;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SeekableInMemoryByteChannel}.
 */
@DisplayName("Tests for SeekableInMemoryByteChannel")
class SeekableInMemoryByteChannelTest {

    /**
     * The JavaDoc for SeekableByteChannel states: "Setting the position to a value that is greater than the current size is
     * legal... A later attempt to write bytes at such a position will cause the entity to grow to accommodate the new
     * bytes; the values of any bytes between the previous end-of-file and the newly-written bytes are unspecified."
     *
     * This test verifies that writing after the end of the channel grows it and that the gap is filled with zeros.
     */
    @Test
    @DisplayName("Writing after the end of the channel should grow it and fill the gap with zeros")
    void writeShouldExpandChannelWhenPositionIsAfterEnd() throws IOException {
        // Arrange
        final byte[] dataToWrite = "Some data".getBytes(UTF_8);
        final int positionToSeek = 2;
        try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel()) {
            // Act
            channel.position(positionToSeek);
            final int bytesWritten = channel.write(ByteBuffer.wrap(dataToWrite));

            // Assert
            // 1. Verify write count and new channel size
            assertEquals(dataToWrite.length, bytesWritten);
            final long expectedSize = positionToSeek + dataToWrite.length;
            assertEquals(expectedSize, channel.size());

            // 2. Verify the entire content, including the gap created by seeking
            final byte[] actualChannelContent = new byte[(int) channel.size()];
            channel.position(0);
            channel.read(ByteBuffer.wrap(actualChannelContent));

            final byte[] expectedChannelContent = new byte[(int) expectedSize];
            // The gap from index 0 to 1 should be zeros, and the data should start at index 2.
            System.arraycopy(dataToWrite, 0, expectedChannelContent, positionToSeek, dataToWrite.length);

            assertArrayEquals(expectedChannelContent, actualChannelContent);
        }
    }

    @Test
    @DisplayName("Reading into a buffer larger than the channel's content reads only available data")
    void readShouldStopAtChannelEndWhenBufferIsLarger() throws IOException {
        // Arrange
        final byte[] initialContent = "Some data".getBytes(UTF_8);
        try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(initialContent)) {
            // Use a buffer that is larger than the channel's content
            final ByteBuffer readBuffer = ByteBuffer.allocate(initialContent.length + 10);

            // Act
            final int bytesRead = channel.read(readBuffer);

            // Assert
            // 1. Verify the number of bytes read matches the content length
            assertEquals(initialContent.length, bytesRead);

            // 2. Verify the data read into the buffer is correct
            final byte[] actualData = Arrays.copyOf(readBuffer.array(), bytesRead);
            assertArrayEquals(initialContent, actualData);

            // 3. Verify the channel's position is at the end
            assertEquals(initialContent.length, channel.position());
        }
    }
}