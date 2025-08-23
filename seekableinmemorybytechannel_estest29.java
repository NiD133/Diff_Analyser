package org.apache.commons.compress.utils;

import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link SeekableInMemoryByteChannel}.
 */
public class SeekableInMemoryByteChannelTest {

    /**
     * Tests that writing a large amount of data to the channel succeeds.
     * <p>
     * This test forces the channel's internal buffer to resize and verifies that the
     * channel's state (size, position) and the return value are correct after the write.
     * The buffer size is chosen to be Integer.MAX_VALUE / 2, which corresponds
     * to an internal resize threshold in the class under test.
     */
    @Test(timeout = 4000)
    public void writeShouldCorrectlyHandleLargeBuffer() throws IOException {
        // Arrange
        // Using a large buffer size that triggers internal resizing logic.
        final int largeBufferSize = Integer.MAX_VALUE / 2; // 1,073,741,823 bytes (~1 GB)
        final SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel();
        final ByteBuffer sourceBuffer = ByteBuffer.allocateDirect(largeBufferSize);

        // Act
        final int bytesWritten = channel.write(sourceBuffer);

        // Assert
        // 1. Verify the number of bytes written matches the buffer size.
        assertEquals("The number of bytes written should match the buffer size.",
                largeBufferSize, bytesWritten);

        // 2. Verify the channel's internal state is updated correctly.
        assertEquals("Channel size should be updated to the number of bytes written.",
                largeBufferSize, channel.size());
        assertEquals("Channel position should advance to the end of the written data.",
                largeBufferSize, channel.position());

        // 3. Verify the source buffer was fully consumed.
        assertEquals("Source buffer should have no remaining bytes after the write.",
                0, sourceBuffer.remaining());
    }
}