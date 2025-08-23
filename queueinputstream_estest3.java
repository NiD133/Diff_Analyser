package org.apache.commons.io.input;

import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link QueueInputStream} class, focusing on read operations.
 */
public class QueueInputStreamTest {

    /**
     * Tests that {@link QueueInputStream#read(byte[], int, int)} correctly reads a single
     * byte into a buffer at a specified offset.
     */
    @Test
    public void testReadIntoBufferWithOffsetReadsSingleByte() throws IOException {
        // Arrange: Set up an input stream with a single byte available to be read.
        final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
        final int byteToWrite = 123;
        queue.add(byteToWrite);

        // Use the modern builder pattern as recommended by the class documentation.
        final QueueInputStream inputStream = QueueInputStream.builder()
                .setBlockingQueue(queue)
                .get();

        final byte[] buffer = new byte[8];
        final int offset = 1;
        final int lengthToRead = 1;

        // Act: Attempt to read one byte into the buffer at the specified offset.
        final int bytesRead = inputStream.read(buffer, offset, lengthToRead);

        // Assert: Verify that exactly one byte was read and the buffer content is correct.
        assertEquals("Should have read exactly one byte.", 1, bytesRead);

        // Create the expected buffer content for a clear and robust comparison.
        final byte[] expectedBuffer = new byte[8];
        expectedBuffer[offset] = (byte) byteToWrite;

        assertArrayEquals("The buffer should contain the read byte at the correct offset.",
                expectedBuffer, buffer);
    }
}