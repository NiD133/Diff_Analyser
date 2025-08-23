package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link QueueInputStream}.
 */
public class QueueInputStreamTest {

    /**
     * Tests that attempting to read from an empty QueueInputStream into a byte array
     * immediately returns -1, indicating the end of the stream.
     */
    @Test
    public void readIntoByteArrayFromEmptyStreamShouldReturnEOF() {
        // Arrange: Create an empty input stream and a buffer to read into.
        final QueueInputStream inputStream = new QueueInputStream();
        final byte[] buffer = new byte[10];

        // Act: Attempt to read from the empty stream.
        final int bytesRead = inputStream.read(buffer, 0, buffer.length);

        // Assert: Verify that the read operation returns -1 (EOF).
        assertEquals("Reading from an empty stream should return -1.", -1, bytesRead);
    }
}