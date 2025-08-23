package org.apache.commons.compress.compressors.gzip;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.junit.Test;

/**
 * Tests for {@link GzipCompressorOutputStream}.
 */
public class GzipCompressorOutputStreamTest {

    /**
     * Tests that calling write() with a length of zero is a no-op.
     * The GZIP header is written upon stream creation, and a zero-length write
     * should not add any more data to the stream.
     */
    @Test
    public void writeWithZeroLengthDoesNotWriteAnyData() throws IOException {
        // Arrange
        // The GZIP header is 10 bytes long (ID1, ID2, CM, FLG, MTIME, XFL, OS).
        final int GZIP_HEADER_SIZE = 10;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // The GzipCompressorOutputStream writes the header to the underlying stream upon instantiation.
        GzipCompressorOutputStream gzipOutputStream = new GzipCompressorOutputStream(byteArrayOutputStream);

        byte[] buffer = new byte[8]; // A dummy buffer for the write operation.
        int offset = 0;
        int lengthToWrite = 0; // The key aspect of this test: writing zero bytes.

        // Act
        // This call should not write any bytes from the buffer.
        gzipOutputStream.write(buffer, offset, lengthToWrite);

        // Assert
        // We verify that the only data in the stream is the 10-byte header written by the constructor.
        // We do not close the stream, as that would write the GZIP trailer.
        assertEquals("The stream should only contain the 10-byte GZIP header.",
                     GZIP_HEADER_SIZE, byteArrayOutputStream.size());
    }
}