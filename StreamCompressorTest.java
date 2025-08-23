package org.apache.commons.compress.archivers.zip;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for StreamCompressor.
 *
 * The tests prefer explicit names, constants, and helper methods to avoid magic numbers
 * and to make intent clear. Where exact byte sequences are asserted, a note explains why.
 */
class StreamCompressorTest {

    // Test data constants
    private static final String TEXT_DEFLATE = "AAAAAABBBBBB";
    private static final String STORED_1 = "A";
    private static final String STORED_2 = "BAD";
    private static final String STORED_3 = "CAFE";

    /**
     * This expected byte sequence depends on the JDK Deflater's current implementation
     * and default settings used by StreamCompressor.create(OutputStream).
     * If the JDK's deflate output changes, this assertion may need to be updated.
     */
    private static final byte[] EXPECTED_DEFLATE_OF_AAAAAABBBBBB = new byte[] { 115, 116, 4, 1, 39, 48, 0, 0 };

    @Test
    @DisplayName("create(DataOutput, Deflater) returns a usable, closable compressor")
    void createDataOutputCompressor_returnsClosableInstance() throws IOException {
        final DataOutput dataOutput = new DataOutputStream(new ByteArrayOutputStream());

        try (StreamCompressor compressor = StreamCompressor.create(dataOutput, new Deflater(9))) {
            assertNotNull(compressor, "Factory must return a compressor instance");
        }
    }

    @Test
    @DisplayName("DEFLATED: tracks bytes read/written and CRC; produces expected JDK-deflate bytes")
    void deflatedEntries_trackersAndExpectedBytes() throws Exception {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        try (StreamCompressor compressor = StreamCompressor.create(out)) {
            // Act: write a single DEFLATED entry
            compressor.deflate(asciiInput(TEXT_DEFLATE), ZipEntry.DEFLATED);

            // Assert: counters reflect the input and output sizes
            assertEquals(TEXT_DEFLATE.length(), compressor.getBytesRead(), "Bytes read should match input length");
            assertEquals(EXPECTED_DEFLATE_OF_AAAAAABBBBBB.length, compressor.getBytesWrittenForLastEntry(),
                    "Bytes written should match compressed output length");

            // Assert: CRC matches the input payload
            assertEquals(crc32OfAscii(TEXT_DEFLATE), compressor.getCrc32(), "CRC32 should match payload");

            // Assert: exact bytes (ties to current JDK Deflater behavior; see note above)
            assertArrayEquals(EXPECTED_DEFLATE_OF_AAAAAABBBBBB, out.toByteArray(),
                    "Compressed bytes should match the expected JDK Deflater output");
        }
    }

    @Test
    @DisplayName("STORED: writes bytes verbatim and tracks counters/CRC per entry")
    void storedEntries_areWrittenVerbatimAndCountersTracked() throws Exception {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        try (StreamCompressor compressor = StreamCompressor.create(out)) {
            // Write three STORED entries, verifying state after the second
            compressor.deflate(asciiInput(STORED_1), ZipEntry.STORED);
            compressor.deflate(asciiInput(STORED_2), ZipEntry.STORED);

            // After writing "BAD"
            assertEquals(STORED_2.length(), compressor.getBytesRead(), "Bytes read should match last STORED entry");
            assertEquals(STORED_2.length(), compressor.getBytesWrittenForLastEntry(),
                    "Bytes written should equal input length for STORED");
            assertEquals(crc32OfAscii(STORED_2), compressor.getCrc32(), "CRC32 should match last STORED entry");

            // Write final STORED entry and verify concatenated output
            compressor.deflate(asciiInput(STORED_3), ZipEntry.STORED);
            final String allWritten =
                    new String(out.toByteArray(), StandardCharsets.US_ASCII);

            assertEquals(STORED_1 + STORED_2 + STORED_3, allWritten, "STORED entries must be written verbatim");
        }
    }

    // Helpers

    private static ByteArrayInputStream asciiInput(final String s) {
        return new ByteArrayInputStream(s.getBytes(StandardCharsets.US_ASCII));
    }

    private static long crc32OfAscii(final String s) {
        final CRC32 crc = new CRC32();
        crc.update(s.getBytes(StandardCharsets.US_ASCII));
        return crc.getValue();
    }
}