package org.apache.commons.compress.archivers.zip;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import org.junit.jupiter.api.Test;

/**
 * This test class has been improved for better understandability.
 * It tests the behavior of {@link StreamCompressor} when handling multiple
 * entries with the STORED compression method.
 */
public class StreamCompressorTest {

    /**
     * Tests that when multiple entries are processed with the STORED method,
     * the StreamCompressor correctly writes the uncompressed data for each entry
     * and updates its internal statistics (bytes read, bytes written, CRC32)
     * on a per-entry basis.
     */
    @Test
    void deflateWithStoredMethod_writesDataAndUpdatesStats() throws IOException {
        // Arrange
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final byte[] entry1Data = "A".getBytes(StandardCharsets.UTF_8);
        final byte[] entry2Data = "BAD".getBytes(StandardCharsets.UTF_8);
        final byte[] entry3Data = "CAFE".getBytes(StandardCharsets.UTF_8);

        try (StreamCompressor streamCompressor = StreamCompressor.create(outputStream)) {
            // Act & Assert: Process the first entry and verify its stats.
            // The compressor's stats should reflect only the most recent entry.
            streamCompressor.deflate(new ByteArrayInputStream(entry1Data), ZipEntry.STORED);
            assertEquals(entry1Data.length, streamCompressor.getBytesRead(), "Bytes read for first entry should match its length");
            assertEquals(entry1Data.length, streamCompressor.getBytesWrittenForLastEntry(), "Bytes written for first entry should match its length");
            assertEquals(calculateCrc32(entry1Data), streamCompressor.getCrc32(), "CRC32 should be correct for the first entry");

            // Act & Assert: Process the second entry and verify its stats are updated.
            streamCompressor.deflate(new ByteArrayInputStream(entry2Data), ZipEntry.STORED);
            assertEquals(entry2Data.length, streamCompressor.getBytesRead(), "Bytes read for second entry should match its length");
            assertEquals(entry2Data.length, streamCompressor.getBytesWrittenForLastEntry(), "Bytes written for second entry should match its length");
            assertEquals(calculateCrc32(entry2Data), streamCompressor.getCrc32(), "CRC32 should be correct for the second entry");

            // Act & Assert: Process the third entry.
            streamCompressor.deflate(new ByteArrayInputStream(entry3Data), ZipEntry.STORED);

            // Final Assert: Verify the concatenated output of all entries.
            final String expectedOutput = "ABADCAFE";
            assertEquals(expectedOutput, outputStream.toString(StandardCharsets.UTF_8.name()), "The final output stream should contain all entry data concatenated");
        }
    }

    /**
     * Helper method to calculate the CRC32 checksum for a given byte array.
     * This makes the test self-contained and avoids magic numbers.
     * @param data The byte array to checksum.
     * @return The CRC32 value.
     */
    private long calculateCrc32(final byte[] data) {
        final CRC32 crc = new CRC32();
        crc.update(data);
        return crc.getValue();
    }
}