package org.apache.commons.compress.utils;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

/**
 * Tests for write operations on {@link SeekableInMemoryByteChannel}.
 */
class SeekableInMemoryByteChannelWriteTest {

    /**
     * Extracts the actual data content from the channel as a byte array.
     * The channel's backing array can be larger than its logical size, so this helper
     * returns a correctly sized copy.
     */
    private byte[] getChannelContentAsBytes(final SeekableInMemoryByteChannel channel) {
        return Arrays.copyOf(channel.array(), (int) channel.size());
    }

    /**
     * Tests that writing to a position beyond the current end-of-file grows the channel,
     * as specified by the SeekableByteChannel interface. The bytes between the previous
     * end-of-file and the newly written data form a "gap".
     */
    @Test
    void write_whenPositionIsPastEnd_createsGapAndAppendsData() throws IOException {
        // Arrange
        final byte[] dataToWrite = "Some data".getBytes(UTF_8);
        final long gapSize = 2;
        try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel()) { // Starts empty
            channel.position(gapSize);

            // Act
            final int bytesWritten = channel.write(ByteBuffer.wrap(dataToWrite));

            // Assert
            final long expectedSize = gapSize + dataToWrite.length;
            assertEquals(dataToWrite.length, bytesWritten);
            assertEquals(expectedSize, channel.size(), "Channel size should grow to accommodate the gap and new data.");
            assertEquals(expectedSize, channel.position(), "Position should be at the new end of the channel.");

            // Verify the content of the written data by reading it back
            final ByteBuffer readBuffer = ByteBuffer.allocate(dataToWrite.length);
            channel.position(gapSize); // Move position to the start of the data we wrote
            final int bytesRead = channel.read(readBuffer);

            assertEquals(dataToWrite.length, bytesRead);
            assertArrayEquals(dataToWrite, readBuffer.array());
        }
    }

    /**
     * Tests that writing to a position that starts within the existing data but extends
     * beyond the end of it correctly overwrites the existing data and extends the channel's size.
     */
    @Test
    void write_whenOverwritingPastEnd_extendsChannel() throws IOException {
        // Arrange
        final byte[] initialData = "abcdefghi".getBytes(UTF_8); // 9 bytes
        final byte[] dataToWrite = "123456789".getBytes(UTF_8); // 9 bytes
        final byte[] expectedData = ("abcde" + "123456789").getBytes(UTF_8); // 5 + 9 = 14 bytes

        try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(initialData)) {
            final long writePosition = 5L;
            channel.position(writePosition);

            // Act
            final int bytesWritten = channel.write(ByteBuffer.wrap(dataToWrite));

            // Assert
            assertEquals(dataToWrite.length, bytesWritten, "Should write all bytes from the source buffer.");
            assertEquals(expectedData.length, channel.size(), "Channel size should be extended.");
            assertEquals(writePosition + bytesWritten, channel.position(), "Position should be at the new end of the channel.");
            assertArrayEquals(expectedData, getChannelContentAsBytes(channel), "Channel content should match the expected overwritten and extended data.");
        }
    }
}