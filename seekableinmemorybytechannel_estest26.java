package org.apache.commons.compress.utils;

import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the SeekableInMemoryByteChannel class.
 */
public class SeekableInMemoryByteChannelTest {

    /**
     * Tests that writing data past the current end of the channel correctly
     * expands the channel's internal buffer and updates its size.
     */
    @Test
    public void writeShouldExpandChannelWhenWritingPastEnd() throws IOException {
        // Arrange: Create a channel with one byte of initial data.
        byte[] initialData = { 10 };
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(initialData);

        // Move the position to the end of the channel, ready for appending.
        channel.position(1);
        assertEquals("Initial position should be at the end", 1, channel.position());
        assertEquals("Initial size should be 1", 1, channel.size());

        // Prepare a buffer with one byte to write.
        byte[] dataToWrite = { 42 };
        ByteBuffer buffer = ByteBuffer.wrap(dataToWrite);

        // Act: Write the buffer's content to the channel.
        int bytesWritten = channel.write(buffer);

        // Assert: Verify the write operation was successful and the channel state is correct.
        assertEquals("Should have written 1 byte", 1, bytesWritten);
        assertEquals("Buffer should be fully drained", 0, buffer.remaining());

        // Verify the channel expanded and the position was updated.
        assertEquals("Channel size should expand to accommodate new data", 2, channel.size());
        assertEquals("Position should be at the new end of the channel", 2, channel.position());

        // Verify the final content of the channel's buffer.
        byte[] expectedChannelContent = { 10, 42 };
        byte[] actualChannelContent = Arrays.copyOf(channel.array(), (int) channel.size());
        assertArrayEquals("Channel content is incorrect after write", expectedChannelContent, actualChannelContent);
    }
}