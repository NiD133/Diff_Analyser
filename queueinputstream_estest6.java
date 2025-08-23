package org.apache.commons.io.input;

import static org.apache.commons.io.IOUtils.EOF;
import static org.junit.Assert.assertEquals;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import org.junit.Test;

/**
 * Tests for {@link QueueInputStream}.
 */
public class QueueInputStreamTest {

    /**
     * Tests that reading from a stream backed by an empty queue immediately
     * returns -1 (end-of-file), as the default timeout is zero.
     */
    @Test
    public void testReadOnEmptyQueueReturnsEof() {
        // Arrange: Create an input stream with an empty queue.
        // By default, the timeout is zero, so the read() operation should not block.
        final BlockingQueue<Integer> emptyQueue = new PriorityBlockingQueue<>();
        final QueueInputStream inputStream = new QueueInputStream(emptyQueue);

        // Act: Attempt to read a byte from the empty stream.
        final int byteRead = inputStream.read();

        // Assert: The read should return EOF (-1) because the queue is empty.
        assertEquals("Reading from an empty stream should return EOF.", EOF, byteRead);
    }
}