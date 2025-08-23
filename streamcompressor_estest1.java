package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;

/**
 * Unit tests for the StreamCompressor class.
 */
public class StreamCompressorTest {

    /**
     * Verifies that deflating an empty input stream using the STORED method
     * results in zero bytes being read from the source and zero bytes being
     * written to the destination.
     */
    @Test
    public void deflateWithEmptyInputStreamAndStoredMethodShouldWriteZeroBytes() throws IOException {
        // Arrange: Set up the compressor and an empty input stream.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        StreamCompressor compressor = StreamCompressor.create(outputStream);
        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);

        // Act: Call the method under test.
        compressor.deflate(emptyInputStream, ZipEntry.STORED);

        // Assert: Verify the outcome.
        assertEquals("Bytes read from an empty stream should be 0", 0L, compressor.getBytesRead());
        assertEquals("Bytes written for the entry should be 0", 0L, compressor.getBytesWrittenForLastEntry());
        assertEquals("The underlying output stream should be empty", 0, outputStream.size());
    }
}