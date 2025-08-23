package org.apache.commons.compress.compressors.gzip;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.compress.compressors.gzip.ExtraField.SubField;
import org.apache.commons.compress.compressors.gzip.GzipParameters.OS;
import org.apache.commons.lang3.ArrayFill;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import shaded.org.apache.commons.io.IOUtils;

/**
 * Tests for GZIP header metadata in {@link GzipCompressorOutputStream}.
 */
public class GzipCompressorOutputStreamHeaderTest {

    /**
     * Tests creating an ExtraField with subfields, covering both valid and invalid scenarios,
     * and verifies a successful write/read roundtrip.
     *
     * @param subFieldCount The number of subfields to add.
     * @param payloadSize   The size of the payload for each subfield. A null value tests null payload handling.
     * @param shouldFail    True if adding the subfields is expected to throw an exception.
     * @throws IOException If a file I/O error occurs.
     */
    @ParameterizedTest
    // @formatter:off
    @CsvSource({
        // subFieldCount, payloadSize, shouldFail | Description
        "0,    42,      false",     // No subfields, should succeed.
        "1,    ,       true",      // Null payload, should fail.
        "1,    0,       false",     // Zero-length payload, should succeed.
        "1,    65531,   false",     // Max payload size for one subfield (65531 + 4 bytes header = 65535).
        "1,    65532,   true",      // Payload size too large for one subfield.
        "2,    0,       false",     // Two zero-length payloads, should succeed.
        "2,    32763,   false",     // Two subfields, total size is 2 * (4 + 32763) = 65534 < 65535.
        "2,    32764,   true"       // Two subfields, total size is 2 * (4 + 32764) = 65536 > 65535.
    })
    // @formatter:on
    void testExtraFieldSubfieldsRoundtrip(final int subFieldCount, final Integer payloadSize, final boolean shouldFail) throws IOException {
        // Arrange: Prepare subfields, expecting failure or success during creation.
        final ExtraField extra = new ExtraField();
        final byte[][] payloads = new byte[subFieldCount][];
        Exception thrown = null;
        try {
            for (int i = 0; i < subFieldCount; i++) {
                if (payloadSize != null) {
                    payloads[i] = ArrayFill.fill(new byte[payloadSize], (byte) ('a' + i));
                }
                extra.addSubField("z" + i, payloads[i]);
            }
        } catch (final NullPointerException | IOException e) {
            thrown = e;
        }

        // Assert: Check if subfield creation failed as expected.
        if (shouldFail) {
            assertNotNull(thrown, "Expected an exception when adding subfields, but none was thrown.");
            return; // Test is complete for failure cases.
        }
        assertNull(thrown, "Unexpected exception when adding subfields: " + thrown);

        // For success cases, perform a write/read roundtrip.
        final Path tempSourceFile = Files.createTempFile("test_gzip_extra_", ".txt");
        Files.write(tempSourceFile, "Hello World!".getBytes(StandardCharsets.ISO_8851));
        final Path targetFile = Files.createTempFile("test_gzip_extra_", ".txt.gz");

        final GzipParameters parameters = new GzipParameters();
        parameters.setExtraField(extra);

        // Act: Write the GZIP file with the extra field.
        try (OutputStream fos = Files.newOutputStream(targetFile);
            GzipCompressorOutputStream gos = new GzipCompressorOutputStream(fos, parameters)) {
            gos.write(tempSourceFile);
            gos.close();
            assertTrue(gos.isClosed());
        }

        // Assert: Read the GZIP file and verify the extra field and its subfields.
        try (GzipCompressorInputStream gis = new GzipCompressorInputStream(Files.newInputStream(targetFile))) {
            final ExtraField extra2 = gis.getMetaData().getExtraField();

            assertEquals(parameters, gis.getMetaData());
            assertEquals(subFieldCount == 0, extra2.isEmpty());
            assertEquals(subFieldCount, extra2.size());
            if (subFieldCount > 0) {
                assertEquals(4 * subFieldCount + subFieldCount * payloadSize, extra2.getEncodedSize());
                assertThrows(UnsupportedOperationException.class, () -> extra2.iterator().remove());
            }

            final ArrayList<SubField> listCopy = new ArrayList<>();
            extra2.forEach(listCopy::add);
            assertEquals(subFieldCount, listCopy.size());

            for (int i = 0; i < subFieldCount; i++) {
                final SubField sf = extra2.getSubField(i);
                assertSame(sf, listCopy.get(i));
                assertSame(sf, extra2.findFirstSubField("z" + i));
                assertEquals("z" + i, sf.getId(), "Subfield ID should be preserved.");
                assertArrayEquals(payloads[i], sf.getPayload(), "Subfield " + i + " has wrong payload.");
            }

            extra2.clear();
            assertTrue(extra2.isEmpty());
        }
    }

    /**
     * Tests that a GZIP header with all optional fields and a header CRC is written correctly,
     * can be read by the standard {@link GZIPInputStream}, and that all metadata is
     * preserved in a roundtrip.
     *
     * @throws IOException      If a stream I/O error occurs.
     * @throws DecoderException If the hex string for the expected header is invalid.
     */
    @Test
    void testHeaderWithCrcRoundtripAndJdkCompatibility() throws IOException, DecoderException {
        // 1. Arrange: Set up GzipParameters with all header fields and CRC enabled.
        final GzipParameters parameters = new GzipParameters();
        parameters.setHeaderCRC(true);
        parameters.setModificationTime(0x66554433); // Use a fixed time for a predictable header.
        parameters.setFileName("AAAA");
        parameters.setComment("ZZZZ");
        parameters.setOS(OS.UNIX);

        final ExtraField extra = new ExtraField();
        extra.addSubField("BB", "CCCC".getBytes(StandardCharsets.ISO_8859_1));
        parameters.setExtraField(extra);

        // 2. Act: Write an empty GZIP stream to capture the header and trailer.
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (GzipCompressorOutputStream gos = new GzipCompressorOutputStream(baos, parameters)) {
            // No content is written; we are only testing the header and trailer.
        }
        final byte[] result = baos.toByteArray();

        // 3. Assert: Verify the written byte stream against a known-good representation.
        final byte[] expected = Hex.decodeHex(
            // --- GZIP Member Header (10+ bytes) ---
            "1f8b" +       // ID1, ID2: Magic number
            "08"   +       // CM: Compression Method = Deflate
            "1e"   +       // FLG: Flags = FEXTRA | FNAME | FCOMMENT | FHCRC
            "33445566" +   // MTIME: Modification Time (little-endian)
            "00"   +       // XFL: Extra Flags
            "03"   +       // OS: Operating System = Unix
            // --- Optional Fields ---
            "0800" +       // XLEN: Extra Field Length = 8 (little-endian)
            "4242" +       //   Subfield ID = "BB"
            "0400" +       //   Subfield Length = 4 (little-endian)
            "43434343" +   //   Subfield Data = "CCCC"
            "4141414100" + // FNAME: File Name = "AAAA" (NUL-terminated)
            "5a5a5a5a00" + // FCOMMENT: Comment = "ZZZZ" (NUL-terminated)
            "d842" +       // FHCRC: Header CRC16 (little-endian)
            // --- Body (empty compressed block) ---
            "0300" +
            // --- GZIP Member Trailer (8 bytes) ---
            "00000000" +   // CRC32 of uncompressed data
            "00000000"     // ISIZE: Input size modulo 2^32
        );
        assertArrayEquals(expected, result, "The generated GZIP stream bytes do not match the expected value.");

        // 4. Assert: Verify that the standard Java GZIPInputStream can read the stream (validates header CRC).
        assertDoesNotThrow(() -> {
            try (GZIPInputStream ignored = new GZIPInputStream(new ByteArrayInputStream(result))) {
                // Reading the stream implicitly validates the header.
            }
        }, "Standard GZIPInputStream should read the header without errors.");

        // 5. Assert: Verify a full roundtrip with GzipCompressorInputStream, checking all metadata.
        try (GzipCompressorInputStream gis = new GzipCompressorInputStream(new ByteArrayInputStream(result))) {
            final GzipParameters metaData = gis.getMetaData();
            assertTrue(metaData.getHeaderCRC());
            assertEquals(0x66554433, metaData.getModificationTime());
            assertEquals("AAAA", metaData.getFileName());
            assertEquals("ZZZZ", metaData.getComment());
            assertEquals(OS.UNIX, metaData.getOS());

            assertEquals(1, metaData.getExtraField().size());
            final SubField sf = metaData.getExtraField().iterator().next();
            assertEquals("BB", sf.getId());
            assertEquals("CCCC", new String(sf.getPayload(), StandardCharsets.ISO_8859_1));

            assertEquals(parameters, metaData, "Metadata should be equal after a roundtrip.");
        }

        // 6. Assert: Verify that corrupting the header CRC causes the JDK GZIPInputStream to fail.
        result[20] = (byte) 0xFF; // Corrupt the header CRC field.
        final ZipException e = assertThrows(ZipException.class, () -> {
            try (GZIPInputStream ignored = new GZIPInputStream(new ByteArrayInputStream(result))) {
                // Reading the stream should trigger the header CRC check and fail.
            }
        }, "A corrupt GZIP header CRC should cause a ZipException.");
        assertEquals("Corrupt GZIP header", e.getMessage(), "Exception message should indicate a corrupt header.");
    }
}