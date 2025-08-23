package org.apache.commons.compress.utils;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests positioning behavior of {@link SeekableInMemoryByteChannel}.
 */
@DisplayName("SeekableInMemoryByteChannel positioning and resizing behavior")
class SeekableInMemoryByteChannelPositioningTest {

    private static final byte[] SAMPLE_DATA = "Some data".getBytes(UTF_8);

    @Test
    @DisplayName("Position should be settable within and beyond the current channel size without changing the size")
    void positionCanBeSetWithinAndBeyondCurrentSize() throws IOException {
        // Arrange
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel(SAMPLE_DATA)) {
            final long initialSize = channel.size();

            // Act & Assert: Set position within current size
            channel.position(4L);
            assertEquals(4L, channel.position(), "Position should be updated correctly within the current size");

            // Act & Assert: Set position to the end of the channel
            channel.position(initialSize);
            assertEquals(initialSize, channel.position(), "Position should be updatable to the end of the channel");

            // Act & Assert: Set position beyond the current size
            final long positionPastEnd = initialSize + 5L;
            channel.position(positionPastEnd);
            assertEquals(positionPastEnd, channel.position(), "Position should be updatable beyond the current size");
            assertEquals(initialSize, channel.size(), "Setting position past the end should not change the channel size");
        }
    }

    /**
     * This test verifies behavior described in the SeekableByteChannel interface:
     * "Setting the position to a value that is greater than the current size is legal
     * but does not change the size of the entity. A later attempt to write bytes at
     * such a position will cause the entity to grow to accommodate the new bytes;
     * the values of any bytes between the previous end-of-file and the newly-written
     * bytes are unspecified."
     */
    @Test
    @DisplayName("Writing at a position past the end should grow the channel and fill the gap with null bytes")
    void writingAtPositionPastEndShouldGrowChannel() throws IOException {
        // Arrange
        final byte[] dataToWrite = "new data".getBytes(UTF_8);
        final int gapSize = 2;

        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
            assertEquals(0, channel.size(), "Channel should be initially empty");

            // Act: Position past the end and write data
            channel.position(gapSize);
            final int bytesWritten = channel.write(ByteBuffer.wrap(dataToWrite));

            // Assert: Verify size and number of bytes written
            assertEquals(dataToWrite.length, bytesWritten, "The number of bytes written should match the source buffer");
            final long expectedSize = (long) gapSize + dataToWrite.length;
            assertEquals(expectedSize, channel.size(), "Channel size should grow to accommodate the gap and the new data");

            // Assert: Verify the content of the gap and the written data
            final byte[] expectedContent = new byte[(int) expectedSize];
            // The gap (from index 0 to gapSize-1) is filled with null bytes by default.
            // Copy the written data into the expected buffer at the correct offset.
            System.arraycopy(dataToWrite, 0, expectedContent, gapSize, dataToWrite.length);

            final byte[] actualContent = new byte[(int) channel.size()];
            channel.position(0);
            channel.read(ByteBuffer.wrap(actualContent));

            assertArrayEquals(expectedContent, actualContent, "The channel content should contain a gap followed by the written data");
        }
    }
}