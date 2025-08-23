package org.apache.commons.compress.compressors.gzip;

import static org.junit.Assert.assertArrayEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.junit.Test;

/**
 * Tests for {@link GzipCompressorOutputStream}.
 */
public class GzipCompressorOutputStreamTest {

    /**
     * Verifies that the GZIP header, including a header CRC, is written to the
     * stream immediately upon instantiation when the corresponding GzipParameter is set.
     *
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc1952#page-5">GZIP Header Spec</a>
     */
    @Test
    public void constructorWritesHeaderWithCrcWhenEnabled() throws IOException {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GzipParameters parameters = new GzipParameters();
        parameters.setHeaderCRC(true);

        // Act: The GZIP header is written by the constructor.
        new GzipCompressorOutputStream(outputStream, parameters);

        // Assert
        // The expected header consists of:
        // 2 bytes: magic number (0x1f, 0x8b)
        // 1 byte: compression method (0x08 for DEFLATE)
        // 1 byte: flags (0x02 for FHCRC, indicating a header CRC is present)
        // 4 bytes: modification time (not set, defaults to 0)
        // 1 byte: extra flags (0)
        // 1 byte: operating system (255 for unknown)
        // 2 bytes: header CRC-16 (calculated over the preceding 10 bytes)
        byte[] expectedHeader = {
            (byte) 0x1f, (byte) 0x8b, // Magic number
            (byte) 0x08,              // Compression method: DEFLATE
            (byte) 0x02,              // Flags: FHCRC
            0, 0, 0, 0,               // Modification time
            0,                        // Extra flags
            (byte) 0xff,              // Operating System: unknown
            (byte) 0x26, (byte) 0x9a  // Header CRC
        };

        assertArrayEquals("The GZIP header with CRC was not written correctly.",
            expectedHeader, outputStream.toByteArray());
    }
}