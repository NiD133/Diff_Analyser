package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link StreamCompressor} class.
 */
public class StreamCompressorTest {

    /**
     * Verifies that a newly created StreamCompressor reports zero total bytes written
     * before any data has been processed.
     */
    @Test
    public void getTotalBytesWrittenShouldReturnZeroForNewCompressor() {
        // Arrange: Create a StreamCompressor with a simple in-memory output stream.
        OutputStream dummyOutputStream = new ByteArrayOutputStream();
        StreamCompressor compressor = StreamCompressor.create(dummyOutputStream);

        // Act: Get the total number of bytes written immediately after creation.
        long totalBytesWritten = compressor.getTotalBytesWritten();

        // Assert: The count of written bytes should be zero.
        assertEquals("A new compressor should have zero bytes written", 0L, totalBytesWritten);
    }
}