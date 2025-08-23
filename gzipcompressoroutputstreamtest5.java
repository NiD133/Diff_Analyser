package org.apache.commons.compress.compressors.gzip;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

import org.apache.commons.compress.compressors.gzip.ExtraField.SubField;
import org.apache.commons.lang3.ArrayFill;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import shaded.org.apache.commons.io.IOUtils;

/**
 * Tests for GzipCompressorOutputStream focusing on metadata like filenames and extra fields.
 */
class GzipCompressorOutputStreamMetadataTest {

    private static final String CHINESE_BASE_NAME = "\u6D4B\u8BD5\u4E2D\u6587\u540D\u79F0"; // "Test Chinese Name"
    private static final String CHINESE_FILE_NAME = CHINESE_BASE_NAME + ".xml";
    private static final byte[] DUMMY_CONTENT = "<text>Hello World!</text>".getBytes(StandardCharsets.ISO_8859_1);

    @TempDir
    private Path tempDir;

    /**
     * GZip RFC 1952 requires filenames to be encoded using ISO-8859-1. This test verifies that multi-byte characters
     * are garbled when the default encoding is used, which is the expected behavior.
     */
    @Test
    void fileNameWithMultiByteCharsIsGarbledWhenUsingDefaultIso88591Charset() throws IOException {
        // The expected result is a string of question marks, as the multi-byte characters
        // cannot be represented in ISO-8859-1.
        final String expectedGarbledFileName = "??????.xml";
        assertRoundTripWithIso88591FileName(CHINESE_FILE_NAME, expectedGarbledFileName);
    }

    /**
     * Tests that a filename with UTF-8 characters is correctly written and read back when the UTF-8 charset is specified.
     */
    @Test
    void testUtf8FileName() throws IOException {
        assumeTrue(Charset.isSupported("UTF-8"), "UTF-8 charset is not supported on this system.");
        assertRoundTripWithNonStandardCharsetFileName(CHINESE_FILE_NAME, StandardCharsets.UTF_8);
    }

    /**
     * Tests the creation and handling of GZIP extra fields with various valid and invalid configurations.
     *
     * @param subFieldCount The number of subfields to add.
     * @param payloadSize   The size of the payload for each subfield.
     * @param shouldFail    Whether adding the subfields is expected to throw an exception.
     */
    @ParameterizedTest(name = "subfields={0}, payloadSize={1}, shouldFail={2}")
    // @formatter:off
    @CsvSource({
        "0,    42, false", // No subfields
        "1,      , true",  // Null payload
        "1,     0, false", // Zero-size payload
        "1, 65531, false", // Max valid payload size for one field
        "1, 65532, true",  // Payload too large for one field
        "2,     0, false", // Two zero-size payloads
        "2, 32764, true",  // Total payload too large for two fields
        "2, 32763, false"  // Max valid payload for two fields
    })
    // @formatter:on
    void testExtraFieldSubfieldsWithVariousPayloads(final int subFieldCount, final Integer payloadSize, final boolean shouldFail) throws IOException {
        // Arrange: Create ExtraField with parameterized subfields
        final ExtraField extraField = new ExtraField();
        final byte[][] payloads = new byte[subFieldCount][];
        boolean creationFailed = false;
        try {
            for (int i = 0; i < subFieldCount; i++) {
                if (payloadSize != null) {
                    payloads[i] = ArrayFill.fill(new byte[payloadSize], (byte) ('a' + i));
                }
                extraField.addSubField("z" + i, payloads[i]);
            }
        } catch (final NullPointerException | IOException e) {
            creationFailed = true;
        }

        // Assert: Check if creation failed as expected
        if (shouldFail) {
            assertTrue(creationFailed, "Expected adding subfield to fail, but it succeeded.");
            return; // Test is complete if failure was expected and occurred.
        }
        assertFalse(creationFailed, "Expected adding subfield to succeed, but it failed.");

        // Arrange: Set up files and GzipParameters for compression
        final Path sourceFile = Files.createFile(tempDir.resolve("source.txt"));
        Files.write(sourceFile, DUMMY_CONTENT);
        final Path compressedFile = tempDir.resolve("source.txt.gz");

        final GzipParameters parameters = new GzipParameters();
        parameters.setExtraField(extraField);

        // Act: Compress the file
        try (OutputStream fos = Files.newOutputStream(compressedFile);
             GzipCompressorOutputStream gos = new GzipCompressorOutputStream(fos, parameters)) {
            gos.write(sourceFile);
        }

        // Assert: Decompress and verify the extra fields and content
        try (GzipCompressorInputStream gis = new GzipCompressorInputStream(Files.newInputStream(compressedFile))) {
            final ExtraField readExtraField = gis.getMetaData().getExtraField();

            // Verify general properties of the read extra field
            assertEquals(subFieldCount == 0, readExtraField.isEmpty());
            assertEquals(subFieldCount, readExtraField.size());
            if (subFieldCount > 0) {
                assertThrows(UnsupportedOperationException.class, () -> readExtraField.iterator().remove());
            }

            // Verify individual subfields
            final ArrayList<SubField> subfieldList = new ArrayList<>();
            readExtraField.forEach(subfieldList::add);
            assertEquals(subFieldCount, subfieldList.size());

            for (int i = 0; i < subFieldCount; i++) {
                final SubField sf = readExtraField.getSubField(i);
                assertEquals("z" + i, sf.getId());
                assertArrayEquals(payloads[i], sf.getPayload(), "Payload for subfield " + i + " does not match.");
            }
        }
    }

    /**
     * Helper to test a compression/decompression round trip where the filename is expected to be ISO-8859-1.
     *
     * @param sourceFileName   The filename to store in the GZIP header.
     * @param expectedFileName The filename expected after reading it back.
     */
    private void assertRoundTripWithIso88591FileName(final String sourceFileName, final String expectedFileName) throws IOException {
        // Arrange
        final Path sourceFile = Files.createFile(tempDir.resolve("source.txt"));
        Files.write(sourceFile, DUMMY_CONTENT);
        final Path compressedFile = tempDir.resolve("source.txt.gz");

        final GzipParameters parameters = new GzipParameters();
        parameters.setFileName(sourceFileName); // setFilename is an alias

        // Act
        try (OutputStream fos = Files.newOutputStream(compressedFile);
             GzipCompressorOutputStream gos = new GzipCompressorOutputStream(fos, parameters)) {
            gos.write(sourceFile);
        }

        // Assert
        try (GzipCompressorInputStream gis = new GzipCompressorInputStream(Files.newInputStream(compressedFile))) {
            assertEquals(expectedFileName, gis.getMetaData().getFileName());
            assertArrayEquals(DUMMY_CONTENT, IOUtils.toByteArray(gis));
        }
    }

    /**
     * Helper to test a compression/decompression round trip with a specified, non-default charset for the filename.
     *
     * @param fileName        The filename to test.
     * @param fileNameCharset The charset to use for encoding and decoding the filename.
     */
    private void assertRoundTripWithNonStandardCharsetFileName(final String fileName, final Charset fileNameCharset) throws IOException {
        // Arrange
        final Path sourceFile = Files.createFile(tempDir.resolve(fileName));
        Files.write(sourceFile, DUMMY_CONTENT);
        final Path compressedFile = tempDir.resolve("compressed.gz");

        final GzipParameters parameters = new GzipParameters();
        parameters.setFileName(fileName);
        parameters.setComment("Comment on " + fileName);
        parameters.setFileNameCharset(fileNameCharset);

        // Act
        try (OutputStream fos = Files.newOutputStream(compressedFile);
             GzipCompressorOutputStream gos = new GzipCompressorOutputStream(fos, parameters)) {
            gos.write(sourceFile);
        }

        // Assert: Verify with legacy GzipCompressorInputStream (which assumes ISO-8859-1)
        try (GzipCompressorInputStream gis = new GzipCompressorInputStream(Files.newInputStream(compressedFile))) {
            // The legacy reader decodes the filename using ISO-8859-1. To verify, we must
            // reverse this incorrect decoding and then apply the correct one.
            final String readFileName = gis.getMetaData().getFileName();
            final byte[] originalBytes = readFileName.getBytes(StandardCharsets.ISO_8859_1);
            final String correctlyDecodedFileName = new String(originalBytes, fileNameCharset);

            assertEquals(fileName, correctlyDecodedFileName);
            assertArrayEquals(DUMMY_CONTENT, IOUtils.toByteArray(gis));
        }

        // Assert: Verify with modern GzipCompressorInputStream builder (which allows specifying charset)
        try (GzipCompressorInputStream gis = GzipCompressorInputStream.builder()
            .setPath(compressedFile)
            .setFileNameCharset(fileNameCharset)
            .get()) {
            // The builder-configured reader should decode the filename and comment correctly.
            assertEquals(fileName, gis.getMetaData().getFileName());
            assertArrayEquals(DUMMY_CONTENT, IOUtils.toByteArray(gis));
        }
    }
}