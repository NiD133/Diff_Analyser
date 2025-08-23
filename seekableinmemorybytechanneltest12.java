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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link SeekableInMemoryByteChannel}.
 */
class SeekableInMemoryByteChannelTest {

    private final byte[] sampleData = "Some data".getBytes(UTF_8);

    /**
     * This test verifies the behavior specified by the SeekableByteChannel interface:
     * "Setting the position to a value that is greater than the current size is legal
     * but does not change the size of the entity. A later attempt to write bytes at
     * such a position will cause the entity to grow to accommodate the new bytes;
     * the values of any bytes between the previous end-of-file and the newly-written
     * bytes are unspecified."
     *
     * In our implementation, the gap should be filled with null bytes.
     */
    @Test
    @DisplayName("Writing past the end of the channel should grow it and leave a gap")
    void writingPastEndOfFileShouldGrowChannel() throws IOException {
        // Arrange
        final int gapSize = 2;
        final long expectedSize = gapSize + sampleData.length;
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
            // Act: Position the channel past the current end (which is 0) and write data.
            channel.position(gapSize);
            final int bytesWritten = channel.write(ByteBuffer.wrap(sampleData));

            // Assert: Check size, position, and number of bytes written.
            assertEquals(sampleData.length, bytesWritten);
            assertEquals(expectedSize, channel.size(), "Channel size should be gap + data length");
            assertEquals(expectedSize, channel.position(), "Position should be at the new end of the channel");

            // Assert: Verify the entire content, including the gap which should be null bytes.
            final byte[] actualContents = new byte[(int) channel.size()];
            channel.position(0);
            channel.read(ByteBuffer.wrap(actualContents));

            final byte[] expectedContents = new byte[(int) expectedSize];
            System.arraycopy(sampleData, 0, expectedContents, gapSize, sampleData.length);

            assertArrayEquals(expectedContents, actualContents,
                "Channel content should have a leading gap of null bytes followed by the written data");
        }
    }

    @ParameterizedTest
    @ValueSource(longs = {-1L, (long) Integer.MAX_VALUE + 1})
    @DisplayName("truncate() should throw IllegalArgumentException for invalid sizes")
    void truncateShouldThrowExceptionForInvalidSize(final long invalidSize) throws IOException {
        // Arrange
        try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(sampleData)) {
            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> channel.truncate(invalidSize),
                "Truncating to a negative size or a size > Integer.MAX_VALUE should fail.");
        }
    }
}