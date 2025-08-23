package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;

/**
 * Test for {@link StreamCompressor}.
 */
public class StreamCompressorTest {

    @Test
    public void getCrc32ShouldReturnCorrectValueAfterWritingOneByte() throws IOException {
        // Arrange
        // The single byte of data we will write to the compressor.
        byte[] dataToWrite = { 0 };

        // Calculate the expected CRC32 value for the data programmatically.
        // This is much clearer than asserting against a "magic number".
        CRC32 expectedCrcCalculator = new CRC32();
        expectedCrcCalculator.update(dataToWrite);
        long expectedCrc = expectedCrcCalculator.getValue();

        // Use a simple ByteArrayOutputStream as a sink for the compressor's output.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        StreamCompressor compressor = StreamCompressor.create(outputStream);

        // Act
        // Write the data using the STORED method (no compression).
        compressor.write(dataToWrite, 0, dataToWrite.length, ZipEntry.STORED);
        long actualCrc = compressor.getCrc32();

        // Assert
        assertEquals("The calculated CRC32 should match the expected value for the written data.",
                expectedCrc, actualCrc);
    }
}