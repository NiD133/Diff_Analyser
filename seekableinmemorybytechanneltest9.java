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

    private static final byte[] TEST_DATA = "Some data".getBytes(UTF_8);

    /**
     * The SeekableByteChannel interface states:
     * "Setting the position to a value that is greater than the current size is legal...
     * A later attempt to write bytes at such a position will cause the entity to grow
     * to accommodate the new bytes; the values of any bytes between the previous
     * end-of-file and the newly-written bytes are unspecified."
     * This test verifies this behavior for SeekableInMemoryByteChannel.
     */
    @Test
    @DisplayName("Writing at a position beyond the current size should grow the channel")
    void writeBeyondSizeShouldGrowChannel() throws IOException {
        // Arrange
        final int gapSize = 2;
        final long expectedNewSize = gapSize + TEST_DATA.length;

        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
            // Act: Position the channel past the current end (size=0) and write data.
            channel.position(gapSize);
            final int bytesWritten = channel.write(ByteBuffer.wrap(TEST_DATA));

            // Assert
            assertEquals(TEST_DATA.length, bytesWritten, "Should write all bytes from the buffer");
            assertEquals(expectedNewSize, channel.size(), "Channel size should grow to accommodate the gap and new data");

            // Verify that the data can be read back correctly from its written position.
            final ByteBuffer readBuffer = ByteBuffer.allocate(TEST_DATA.length);
            channel.position(gapSize);
            channel.read(readBuffer);
            assertArrayEquals(TEST_DATA, readBuffer.array(), "Data read back should match the data written");

            // Verify that the gap created by positioning past the end is filled with null bytes.
            final byte[] gapBuffer = new byte[gapSize];
            channel.position(0);
            channel.read(ByteBuffer.wrap(gapBuffer));
            assertArrayEquals(new byte[gapSize], gapBuffer, "The gap should be filled with null bytes");
        }
    }

    @Test
    @DisplayName("Reading from a closed channel should throw ClosedChannelException")
    void readOnClosedChannelShouldThrowClosedChannelException() throws IOException {
        // Arrange
        final SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel();
        channel.close();

        // Act & Assert
        assertThrows(ClosedChannelException.class, () -> channel.read(ByteBuffer.allocate(1)),
            "read() on a closed channel should throw ClosedChannelException");
    }
}