package org.apache.commons.io.input;

import org.junit.Test;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link QueueInputStream} class.
 */
public class QueueInputStreamTest {

    /**
     * Tests that the read() method correctly retrieves a single byte
     * from a pre-populated underlying queue.
     */
    @Test
    public void readShouldReturnSingleByteFromQueue() {
        // Arrange
        final BlockingQueue<Integer> queue = new PriorityBlockingQueue<>();
        final int expectedByte = 0;
        queue.add(expectedByte);

        final QueueInputStream inputStream = new QueueInputStream(queue);

        // Act
        final int actualByte = inputStream.read();

        // Assert
        assertEquals("The byte read from the stream should match the byte added to the queue.",
                expectedByte, actualByte);
    }
}