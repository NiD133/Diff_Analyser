package org.apache.commons.compress.utils;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SeekableInMemoryByteChannel}.
 */
public class SeekableInMemoryByteChannelTest {

    private final byte[] testData = "Some data".getBytes(UTF_8);

    /**
     * Tests that writing to a position after the end of the channel grows the channel accordingly.
     * <p>
     * As per the {@link SeekableByteChannel} spec: "Setting the position to a value that is greater than the
     * current size is legal but does not change the size of the entity. A later attempt to write
     * bytes at such a position will cause the entity to grow to accommodate the new bytes..."
     * </p>
     */
    @Test
    void writingAtPositionBeyondSizeShouldGrowChannel() throws IOException {
        // Arrange
        final int gapSize = 2;
        final long expectedSize = testData.length + gapSize;
        final ByteBuffer writeBuffer = ByteBuffer.wrap(testData);

        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
            // Act: Set position past the current end-of-file and write data.
            channel.position(gapSize);
            final int bytesWritten = channel.write(writeBuffer);

            // Assert: Check that the write was successful and the channel size increased correctly.
            assertEquals(testData.length, bytesWritten);
            assertEquals(expectedSize, channel.size());

            // Assert: Verify the written data can be read back from the correct position.
            channel.position(gapSize);
            final ByteBuffer readBuffer = ByteBuffer.allocate(testData.length);
            channel.read(readBuffer);
            assertArrayEquals(testData, readBuffer.array());
        }
    }

    /**
     * Verifies that calling truncate on a closed channel does not throw an exception,
     * which is a deliberate deviation from the {@link SeekableByteChannel} interface specification.
     * <p>
     * The spec for {@code truncate} states it should throw a {@code ClosedChannelException}
     * if the channel is closed. This implementation intentionally does not.
     * </p>
     */
    @Test
    void truncateOnClosedChannelShouldNotThrowException() throws IOException {
        // Arrange
        final SeekableByteChannel channel = new SeekableInMemoryByteChannel();
        channel.close(); // Close the channel before the action.

        // Act & Assert: Verify that truncate() does not throw an exception on a closed channel.
        assertDoesNotThrow(() -> channel.truncate(0));
    }
}