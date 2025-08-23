package org.apache.commons.compress.compressors.gzip;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.compress.compressors.gzip.ExtraField.SubField;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests advanced features of GzipCompressorOutputStream, such as extra fields and non-ASCII metadata.
 */
class GzipCompressorOutputStreamAdvancedFeaturesTest {

    private static final byte[] TEST_CONTENT = "<text>Hello World!</text>".getBytes(StandardCharsets.ISO_8859_1);
    private static final String CHINESE_FILENAME_BASE = "\u6D4B\u8BD5\u4E2D\u6587\u540D\u79F0"; // "Test Chinese Name"
    private static final String CHINESE_FILENAME_XML = CHINESE_FILENAME_BASE + ".xml";

    @TempDir
    private Path tempDir;

    // region Extra Field Tests

    /**
     * Tests that adding a subfield with a null payload throws a NullPointerException.
     * This tests the validation logic of the ExtraField class itself.
     */
    @Test
    void addSubFieldWithNullPayload_shouldThrowNullPointerException() {
        final ExtraField extraField = new ExtraField();
        assertThrows(NullPointerException.class, () -> extraField.addSubField("id", null),
            "Should not be able to add a subfield with a null payload.");
    }

    /**
     * Tests that adding subfields whose total size exceeds the GZIP format's limit throws an IOException.
     * The total size of the extra field block cannot exceed 65535 bytes.
     */
    @Test
    void addSubFieldsWithTotalPayloadExceedingMaxSize_shouldThrowIOException() throws IOException {
        final ExtraField extraField = new ExtraField();
        // Each subfield has a 4-byte header. Total size is sum of (4 + payload.length).
        // Two subfields of 32764 bytes each: 2 * (4 + 32764) = 65536, which is > 65535.
        extraField.addSubField("z0", new byte[32764]); // First one is fine.
        assertThrows(IOException.class, () -> extraField.addSubField("z1", new byte[32764]),
            "Adding a second subfield should cause the total size to exceed the max limit.");
    }

    /**
     * Tests that extra fields are correctly written and read back during a compression/decompression roundtrip.
     *
     * @param subFieldCount The number of subfields to add.
     * @param payloadSize   The size of the payload for each subfield.
     */
    @ParameterizedTest
    @CsvSource({
        "0,   -1",    // No extra fields
        "1,    0",    // One field, empty payload
        "1, 65531",   // One field, max payload size
        "2,    0",    // Two fields, empty payloads
        "2, 32763"    // Two fields, large payloads that are valid in total
    })
    void extraFields_shouldBePreservedDuringRoundtrip(final int subFieldCount, final int payloadSize) throws IOException {
        // Arrange
        final GzipParameters parameters = new GzipParameters();
        final ExtraField sourceExtraField = new ExtraField();
        final byte[][] payloads = new byte[subFieldCount][];

        for (int i = 0; i < subFieldCount; i++) {
            payloads[i] = new byte[payloadSize];
            Arrays.fill(payloads[i], (byte) ('a' + i));
            sourceExtraField.addSubField("z" + i, payloads[i]);
        }
        parameters.setExtraField(sourceExtraField);

        final Path sourceFile = Files.createTempFile(tempDir, "source", ".txt");
        Files.write(sourceFile, TEST_CONTENT);
        final Path compressedFile = Files.createTempFile(tempDir, "target", ".gz");

        // Act
        try (OutputStream fos = Files.newOutputStream(compressedFile);
             GzipCompressorOutputStream gos = new GzipCompressorOutputStream(fos, parameters)) {
            gos.write(sourceFile);
        }

        // Assert
        try (GzipCompressorInputStream gis = new GzipCompressorInputStream(Files.newInputStream(compressedFile))) {
            final ExtraField readExtraField = gis.getMetaData().getExtraField();

            assertEquals(subFieldCount, readExtraField.size());
            final List<SubField> subFields = new ArrayList<>();
            readExtraField.forEach(subFields::add);

            for (int i = 0; i < subFieldCount; i++) {
                final SubField sf = subFields.get(i);
                assertEquals("z" + i, sf.getId());
                assertArrayEquals(payloads[i], sf.getPayload(), "Payload for subfield " + i + " should match.");
            }
            // Verify that the decompressed content is correct
            assertArrayEquals(TEST_CONTENT, gis.readAllBytes());
        }
    }

    // endregion

    // region Filename Tests

    @Test
    void shouldPreserveAsciiFileNameDuringRoundtrip() throws IOException {
        assertFileNameIsPreserved("ASCII.xml", "ASCII.xml", StandardCharsets.ISO_8859_1);
    }

    @Test
    void shouldPreserveUtf8FileNameDuringRoundtrip() throws IOException {
        assertFileNameIsPreserved(CHINESE_FILENAME_XML, CHINESE_FILENAME_XML, StandardCharsets.UTF_8);
    }

    @Test
    void shouldPreserveGbkFileNameDuringRoundtrip() throws IOException {
        assumeTrue(Charset.isSupported("GBK"), "GBK charset is not supported on this platform.");
        final Charset gbk = Charset.forName("GBK");
        assertFileNameIsPreserved(CHINESE_FILENAME_XML, CHINESE_FILENAME_XML, gbk);
    }

    /**
     * Helper method to verify that a filename, potentially with non-ASCII characters,
     * is correctly handled during a compression/decompression roundtrip.
     *
     * @param fileNameToSet The filename to embed in the GZIP header.
     * @param expectedFileName The expected filename after reading.
     * @param fileNameCharset The charset to use for encoding/decoding the filename.
     */
    private void assertFileNameIsPreserved(final String fileNameToSet, final String expectedFileName, final Charset fileNameCharset) throws IOException {
        // Arrange
        final Path sourceFile = Files.createTempFile(tempDir, "source", ".tmp");
        Files.write(sourceFile, TEST_CONTENT);

        final Path compressedFile = Files.createTempFile(tempDir, "target", ".gz");

        final GzipParameters parameters = new GzipParameters();
        parameters.setFileName(fileNameToSet);
        parameters.setFileNameCharset(fileNameCharset);
        parameters.setComment("Comment on " + fileNameToSet);

        // Act: Compress the file
        try (OutputStream fos = Files.newOutputStream(compressedFile);
             GzipCompressorOutputStream gos = new GzipCompressorOutputStream(fos, parameters)) {
            gos.write(sourceFile);
        }

        // Assert using legacy GzipCompressorInputStream (pre-builder)
        try (GzipCompressorInputStream gis = new GzipCompressorInputStream(Files.newInputStream(compressedFile))) {
            // The legacy constructor decodes metadata using ISO-8859-1 by default.
            // To verify non-ASCII names, we must reverse this incorrect decoding and then apply the correct one.
            final String rawFileName = gis.getMetaData().getFileName();
            final byte[] rawBytes = rawFileName.getBytes(StandardCharsets.ISO_8859_1);
            final String correctlyDecodedFileName = new String(rawBytes, fileNameCharset);

            assertEquals(expectedFileName, correctlyDecodedFileName);
            assertArrayEquals(TEST_CONTENT, gis.readAllBytes());
        }

        // Assert using modern, builder-based GzipCompressorInputStream
        try (InputStream fis = Files.newInputStream(compressedFile);
             GzipCompressorInputStream gis = GzipCompressorInputStream.builder()
                 .setInputStream(fis)
                 .setFileNameCharset(fileNameCharset)
                 .get()) {
            // The builder correctly decodes the filename using the provided charset.
            assertEquals(expectedFileName, gis.getMetaData().getFileName());
            assertArrayEquals(TEST_CONTENT, gis.readAllBytes());

            // reset trailer values for a simple assertion of the whole metadata object.
            gis.getMetaData().setTrailerCrc(0);
            gis.getMetaData().setTrailerISize(0);
            assertEquals(parameters, gis.getMetaData());
        }
    }

    // endregion
}