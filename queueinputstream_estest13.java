package org.apache.commons.io.input;

import org.junit.Test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link QueueInputStream} class.
 */
public class QueueInputStreamTest {

    /**
     * Tests that the read() method correctly retrieves a single byte value
     * that has been placed into the backing queue.
     */
    @Test(timeout = 4000)
    public void readShouldReturnByteFromQueue() {
        // Arrange: Create a queue and add a single byte value to it.
        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
        final int expectedByte = 42;
        queue.add(expectedByte);

        final QueueInputStream inputStream = new QueueInputStream(queue);

        // Act: Read the byte from the input stream.
        final int actualByte = inputStream.read();

        // Assert: The read byte should match the one added to the queue.
        assertEquals(expectedByte, actualByte);
    }
}