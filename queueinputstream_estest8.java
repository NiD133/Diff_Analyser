package org.apache.commons.io.input;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.junit.Test;

/**
 * Tests for {@link QueueInputStream}.
 */
public class QueueInputStreamTest {

    @Test
    public void readToByteArrayWithZeroLengthShouldReturnZero() throws IOException {
        // Arrange
        // An empty queue and an input stream reading from it.
        final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
        final QueueInputStream inputStream = new QueueInputStream(queue);
        final byte[] buffer = new byte[8];

        // Act
        // Attempt to read 0 bytes into the buffer.
        final int bytesRead = inputStream.read(buffer, 0, 0);

        // Assert
        // The read method should return 0, as per the InputStream contract.
        assertEquals("Reading zero bytes should return 0", 0, bytesRead);
    }
}