package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Tests for the byte counting functionality of {@link StreamCompressor}.
 */
public class StreamCompressorTest {

    /**
     * Verifies that writeCounted() correctly updates the total number of bytes written.
     */
    @Test
    public void writeCountedShouldUpdateTotalBytesWritten() throws IOException {
        // Arrange: Create a StreamCompressor with a simple in-memory output stream.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        StreamCompressor streamCompressor = StreamCompressor.create(outputStream);
        byte[] dataToWrite = new byte[1];

        // Act: Write a single-byte array to the compressor.
        streamCompressor.writeCounted(dataToWrite);

        // Assert: Check if the total bytes written counter reflects the write operation.
        long expectedBytesWritten = 1L;
        assertEquals("The total bytes written counter should be incremented by the number of bytes written.",
                expectedBytesWritten, streamCompressor.getTotalBytesWritten());
    }
}