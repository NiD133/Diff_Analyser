package org.apache.commons.compress.archivers.zip;

import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedOutputStream;
import java.util.zip.ZipEntry;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link StreamCompressor} class.
 * The original test class was auto-generated and has been refactored for clarity.
 */
public class StreamCompressorTest {

    /**
     * Tests that writing a single byte using the STORED method correctly updates
     * the total number of bytes written and the CRC32 checksum.
     */
    @Test(timeout = 4000)
    public void writeSingleByteAsStoredUpdatesBytesWrittenAndCrc() throws IOException {
        // Arrange
        // The data to be written is a single byte with value 0.
        // We write one byte from the sourceData array, starting at offset 1.
        byte[] sourceData = {0x00, 0x00};
        int offset = 1;
        int length = 1;

        // The expected CRC32 for a single null byte (0x00).
        final long expectedCrc = 3523407757L;
        final long expectedBytesWritten = 1L;

        OutputStream outputStream = new PipedOutputStream();
        StreamCompressor streamCompressor = StreamCompressor.create(outputStream);

        // Act
        // The original test used a magic number (1871) for the compression method.
        // Its behavior indicates it was treated as STORED, so we use the explicit constant.
        streamCompressor.write(sourceData, offset, length, ZipEntry.STORED);
        long actualTotalBytesWritten = streamCompressor.getTotalBytesWritten();
        long actualCrc = streamCompressor.getCrc32();

        // Assert
        assertEquals("The total bytes written count should be 1.",
            expectedBytesWritten, actualTotalBytesWritten);
        assertEquals("The CRC32 checksum should be calculated for the single written byte.",
            expectedCrc, actualCrc);
    }
}