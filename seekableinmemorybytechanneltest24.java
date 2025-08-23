package org.apache.commons.compress.utils;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SeekableInMemoryByteChannel} focusing on writing and truncating behavior.
 */
class SeekableInMemoryByteChannelTest {

    private final byte[] testData = "Some data".getBytes(UTF_8);

    /**
     * Tests that writing to a position beyond the current size grows the channel.
     * As per the SeekableByteChannel contract, setting a position > size is legal.
     * A subsequent write should grow the channel to accommodate the new data.
     */
    @Test
    void shouldGrowChannelWhenWritingPastEnd() throws IOException {
        // Arrange
        final int writePosition = 2;
        final long expectedSize = writePosition + testData.length;
        // Using a try-with-resources statement to ensure the channel is closed.
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) { // Initially empty, size = 0

            // Act: Position past the end and write data.
            channel.position(writePosition);
            final int bytesWritten = channel.write(ByteBuffer.wrap(testData));

            // Assert: Verify size, bytes written, and that the data can be read back.
            assertEquals(testData.length, bytesWritten, "Bytes written should match data length");
            assertEquals(expectedSize, channel.size(), "Channel size should be position + data length");

            // Verify data can be read back from the write position.
            channel.position(writePosition);
            final ByteBuffer readBuffer = ByteBuffer.allocate(testData.length);
            final int bytesRead = channel.read(readBuffer);

            assertEquals(testData.length, bytesRead, "Bytes read should match data length");
            assertArrayEquals(testData, readBuffer.array(), "Data read back should match data written");
        }
    }

    /**
     * Tests that truncating to the current size has no effect on the channel's content or size.
     * As per the SeekableByteChannel contract, if the new size >= current size, the entity is not modified.
     */
    @Test
    void shouldNotModifyChannelWhenTruncatingToSameSize() throws IOException {
        // Arrange
        // Using a try-with-resources statement to ensure the channel is closed.
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel(testData)) {
            final long initialSize = channel.size();
            assertEquals(testData.length, initialSize, "Initial size should match data length");

            // Act: Truncate to the current size.
            channel.truncate(initialSize);

            // Assert: Verify size and content are unchanged.
            assertEquals(initialSize, channel.size(), "Size should not change after truncating to the same size");

            // Read the entire channel content to verify it's intact.
            channel.position(0); // Ensure reading from the start for clarity.
            final ByteBuffer readBuffer = ByteBuffer.allocate(testData.length);
            final int bytesRead = channel.read(readBuffer);

            assertEquals(testData.length, bytesRead, "Should be able to read the full original content");
            assertArrayEquals(testData, readBuffer.array(), "Content should be unchanged after truncation");
        }
    }
}