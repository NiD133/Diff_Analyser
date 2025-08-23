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
     * Tests that calling finish() on a new GzipCompressorOutputStream, without writing any data,
     * produces a valid, empty GZIP stream.
     *
     * A valid empty GZIP stream consists of a 10-byte header, a 2-byte empty
     * DEFLATE block, and an 8-byte trailer.
     */
    @Test
    public void finishOnEmptyStreamProducesValidGzipFile() throws IOException {
        // Arrange: Create a GZIP output stream that writes to an in-memory buffer.
        final ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream();
        final GzipCompressorOutputStream gzipOutputStream = new GzipCompressorOutputStream(outputBuffer);

        // Act: Finish the stream to write out the GZIP header and trailer for an empty file.
        gzipOutputStream.finish();
        gzipOutputStream.close();

        // Assert: Verify the output buffer contains the correct bytes for an empty GZIP file.
        final byte[] actualBytes = outputBuffer.toByteArray();

        // The GZIP format for an empty file consists of:
        // - 10-byte header
        // - 2-byte empty DEFLATE block
        // - 8-byte trailer (CRC32 and input size, both zero)
        // Total expected size is 20 bytes.
        final byte[] expectedBytes = {
            // --- GZIP Header (10 bytes) ---
            (byte) 0x1f, (byte) 0x8b, // Magic number indicating GZIP
            (byte) 0x08,             // Compression method: DEFLATE
            (byte) 0x00,             // Flags
            0, 0, 0, 0,              // Modification time (Unix epoch)
            (byte) 0x00,             // Extra flags
            (byte) 0xff,             // Operating System: unknown
            // --- Compressed Data (2 bytes) ---
            // An empty, non-final DEFLATE block, which is standard for empty GZIP.
            (byte) 0x03, (byte) 0x00,
            // --- GZIP Trailer (8 bytes) ---
            0, 0, 0, 0,              // CRC-32 checksum of uncompressed data (0)
            0, 0, 0, 0               // Size of uncompressed data (0)
        };

        assertArrayEquals("The output byte array does not match the expected empty GZIP format.",
                          expectedBytes, actualBytes);
    }
}