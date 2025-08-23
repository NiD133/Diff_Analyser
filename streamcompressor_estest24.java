package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;

public class StreamCompressor_ESTestTest24 extends StreamCompressor_ESTest_scaffolding {

    /**
     * Verifies that calling deflate() with an empty input stream results in zero bytes
     * being read from the source and zero bytes being written to the output.
     */
    @Test(timeout = 4000)
    public void deflateWithEmptyInputStreamShouldReadAndWriteZeroBytes() throws IOException {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        StreamCompressor streamCompressor = StreamCompressor.create(outputStream);
        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);

        // Act
        // Deflate the empty stream. The compression method (STORED) is incidental here,
        // as no data is processed.
        streamCompressor.deflate(emptyInputStream, ZipEntry.STORED);

        // Assert
        assertEquals("Bytes read from the source should be 0 for an empty stream.",
                0L, streamCompressor.getBytesRead());
        assertEquals("Bytes written for the entry should be 0 for an empty stream.",
                0L, streamCompressor.getBytesWrittenForLastEntry());
        assertEquals("The underlying output stream should remain empty.",
                0, outputStream.size());
    }
}