package org.apache.commons.compress.archivers.zip;

import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedOutputStream;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link StreamCompressor}.
 */
public class StreamCompressorTest {

    @Test
    public void getBytesReadShouldReturnZeroAfterClosingWithoutProcessingData() throws IOException {
        // Arrange: Create a StreamCompressor with a dummy output stream.
        // No data will be written to or read by the compressor.
        OutputStream dummyOutputStream = new PipedOutputStream();
        StreamCompressor compressor = StreamCompressor.create(dummyOutputStream);

        // Act: Close the compressor immediately after creation.
        compressor.close();

        // Assert: Verify that the number of bytes read is zero.
        assertEquals("Bytes read should be 0 for a compressor that was closed without processing any data.",
                0L, compressor.getBytesRead());
    }
}