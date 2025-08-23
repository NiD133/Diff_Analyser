package org.apache.commons.compress.utils;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SeekableInMemoryByteChannel}.
 */
class SeekableInMemoryByteChannelTest {

    /**
     * This test verifies the behavior described in the SeekableByteChannel Javadoc:
     * "Setting the position to a value that is greater than the current size is legal...
     * A later attempt to write bytes at such a position will cause the entity to grow...
     * the values of any bytes between the previous end-of-file and the newly-written bytes are unspecified."
     * For SeekableInMemoryByteChannel, this gap is expected to be filled with null bytes.
     */
    @Test
    @DisplayName("Writing past the end of the channel should grow it, filling the gap with null bytes")
    void writeShouldExtendChannelWhenPositionIsBeyondSize() throws IOException {
        // Arrange
        final byte[] dataToWrite = "Some data".getBytes(UTF_8);
        final int gapSize = 2;
        final int writePosition = gapSize;
        final int expectedTotalSize = dataToWrite.length + gapSize;

        // The expected content is two null bytes (the gap) followed by the data.
        final byte[] expectedContent = new byte[expectedTotalSize];
        System.arraycopy(dataToWrite, 0, expectedContent, gapSize, dataToWrite.length);

        try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel()) {
            // Act: Set position past the initial size (0) and write data.
            channel.position(writePosition);
            final int bytesWritten = channel.write(ByteBuffer.wrap(dataToWrite));

            // Assert
            assertEquals(dataToWrite.length, bytesWritten, "Number of bytes written should match data length");
            assertEquals(expectedTotalSize, channel.size(), "Channel size should be extended to accommodate the gap and data");

            // Verify the entire content, including the gap.
            final byte[] actualContent = Arrays.copyOf(channel.array(), (int) channel.size());
            assertArrayEquals(expectedContent, actualContent,
                "Channel content should have a leading gap of null bytes followed by the written data");
        }
    }

    @Test
    @DisplayName("Writing to a closed channel should throw ClosedChannelException")
    void writeShouldThrowExceptionOnClosedChannel() throws IOException {
        // Arrange
        final SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel();
        channel.close(); // Close the channel before attempting to write

        // Act & Assert
        assertThrows(ClosedChannelException.class, () -> {
            channel.write(ByteBuffer.allocate(1));
        }, "Writing to a closed channel must throw ClosedChannelException");
    }
}