package org.apache.commons.compress.compressors.gzip;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.compress.compressors.gzip.ExtraField.SubField;
import org.apache.commons.lang3.ArrayFill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import shaded.org.apache.commons.io.IOUtils;

/**
 * Tests for advanced features of {@link GzipCompressorOutputStream}, such as custom filenames and extra fields.
 */
public class GzipCompressorOutputStreamAdvancedFeaturesTest {

    private static final String CHINESE_BASE_NAME = "\u6D4B\u8BD5\u4E2D\u6587\u540D\u79F0"; // "TestChineseName"
    private static final String CHINESE_FILE_NAME = CHINESE_BASE_NAME + ".xml";
    private static final byte[] CONTENT = "<text>Hello World!</text>".getBytes(StandardCharsets.UTF_8);

    @TempDir
    private Path tempDir;
    private Path sourceFile;
    private Path targetFile;

    @BeforeEach
    void setUp() throws IOException {
        sourceFile = Files.createTempFile(tempDir, "source", ".txt");
        Files.write(sourceFile, CONTENT);
        targetFile = Files.createTempFile(tempDir, "target", ".gz");
    }

    // =================================================================
    // Tests for Filename Metadata
    // =================================================================

    /**
     * Verifies that a simple ASCII filename is correctly written to and read from a GZIP stream.
     * Also tests the alias methods setFilename/getFilename.
     */
    @Test
    void writeAndReadWithSimpleAsciiFileName() throws IOException {
        verifyGzipFileWithSimpleFileName("test.txt", "test.txt");
    }

    /**
     * Verifies that a Chinese filename, encoded with UTF-8, is correctly handled.
     * Note: Proper handling requires specifying the charset on both the writing and reading streams.
     */
    @Test
    void writeAndReadWithChineseFileNameInUtf8() throws IOException {
        verifyGzipFileWithChineseFileName(CHINESE_FILE_NAME, StandardCharsets.UTF_8);
    }

    // =================================================================
    // Tests for ExtraField Metadata
    // =================================================================

    /**
     * Contains unit tests for the validation logic of the {@link ExtraField} class.
     */
    @Nested
    class ExtraFieldValidationTest {
        private final ExtraField extraField = new ExtraField();

        @Test
        void addSubFieldWithNullPayload_shouldThrowException() {
            assertThrows(NullPointerException.class, () -> extraField.addSubField("z0", null),
                "Adding a subfield with a null payload should fail.");
        }

        @Test
        void addSubFieldWithTooLargePayload_shouldThrowException() {
            // A subfield's data (4-byte header + payload) cannot exceed 65535 bytes.
            final byte[] payload = new byte[65532];
            assertThrows(IOException.class, () -> extraField.addSubField("z0", payload),
                "Adding a subfield with a payload > 65531 bytes should fail.");
        }

        @Test
        void addMultipleSubFieldsExceedingTotalSize_shouldThrowException() {
            // The total size of all subfields cannot exceed 65535 bytes.
            final byte[] payload = new byte[32764]; // (4 + 32764) * 2 = 65536 > 65535
            assertDoesNotThrow(() -> extraField.addSubField("z0", payload));
            assertThrows(IOException.class, () -> extraField.addSubField("z1", payload),
                "Adding subfields whose total size exceeds 65535 bytes should fail.");
        }
    }

    /**
     * Tests the end-to-end process of writing and reading GZIP files with valid ExtraField data.
     *
     * @param subFieldCount The number of subfields to add.
     * @param payloadSize   The size of the payload for each subfield.
     */
    @ParameterizedTest
    @CsvSource({
        "0, 0",      // No extra fields
        "1, 0",      // One subfield with an empty payload
        "1, 65531",  // One subfield with a max-sized payload
        "2, 32763"   // Two large subfields that fit within the total size limit
    })
    void writeAndReadWithValidExtraSubfields(final int subFieldCount, final int payloadSize) throws IOException {
        // Arrange
        final GzipParameters parameters = new GzipParameters();
        final ExtraField extra = new ExtraField();
        final byte[][] payloads = new byte[subFieldCount][];
        int expectedEncodedSize = 0;

        for (int i = 0; i < subFieldCount; i++) {
            payloads[i] = ArrayFill.fill(new byte[payloadSize], (byte) ('a' + i));
            extra.addSubField("z" + i, payloads[i]);
            expectedEncodedSize += 4 + payloadSize; // 2 bytes for ID, 2 for length
        }
        parameters.setExtraField(extra);

        // Act
        try (OutputStream fos = Files.newOutputStream(targetFile);
            GzipCompressorOutputStream gos = new GzipCompressorOutputStream(fos, parameters)) {
            gos.write(sourceFile);
        }

        // Assert
        try (GzipCompressorInputStream gis = new GzipCompressorInputStream(Files.newInputStream(targetFile))) {
            final ExtraField readExtra = gis.getMetaData().getExtraField();
            assertEquals(subFieldCount == 0, readExtra.isEmpty());
            assertEquals(subFieldCount, readExtra.size());
            assertEquals(expectedEncodedSize, readExtra.getEncodedSize());

            final List<SubField> subFieldList = new ArrayList<>();
            readExtra.forEach(subFieldList::add);
            assertEquals(subFieldCount, subFieldList.size());

            for (int i = 0; i < subFieldCount; i++) {
                final SubField sf = readExtra.getSubField(i);
                assertSame(sf, subFieldList.get(i));
                assertEquals("z" + i, sf.getId());
                assertArrayEquals(payloads[i], sf.getPayload(), "Payload for subfield " + i + " should match.");
            }
        }
    }

    /**
     * Verifies the behavior of an empty {@link ExtraField}.
     */
    @Test
    void emptyExtraField_shouldBehaveAsExpected() {
        // Arrange
        final ExtraField extra = new ExtraField();

        // Act & Assert
        assertEquals(0, extra.toByteArray().length, "Byte array of an empty ExtraField should be empty.");
        assertFalse(extra.iterator().hasNext(), "Iterator of an empty ExtraField should be empty.");
        extra.forEach(e -> fail("forEach should not be called on an empty ExtraField."));
        assertThrows(IndexOutOfBoundsException.class, () -> extra.getSubField(0),
            "Accessing a subfield by index should fail for an empty ExtraField.");
    }

    // =================================================================
    // Private Test Helpers
    // =================================================================

    /**
     * Helper to verify writing and reading a GZIP file with a specified filename.
     */
    private void verifyGzipFileWithSimpleFileName(final String sourceFileName, final String expectedFileName) throws IOException {
        // Arrange
        final GzipParameters parameters = new GzipParameters();
        parameters.setFilename(sourceFileName); // Test setFilename alias
        assertEquals(sourceFileName, parameters.getFileName());
        parameters.setFileName(sourceFileName); // Test setFileName
        assertEquals(sourceFileName, parameters.getFilename()); // Test getFilename alias

        // Act
        try (OutputStream fos = Files.newOutputStream(targetFile);
            GzipCompressorOutputStream gos = new GzipCompressorOutputStream(fos, parameters)) {
            gos.write(sourceFile);
        }

        // Assert
        try (GzipCompressorInputStream gis = new GzipCompressorInputStream(Files.newInputStream(targetFile))) {
            assertEquals(expectedFileName, gis.getMetaData().getFileName());
            assertArrayEquals(CONTENT, IOUtils.toByteArray(gis));
        }
    }

    /**
     * Helper to verify writing and reading a GZIP file with a multi-byte character filename.
     */
    private void verifyGzipFileWithChineseFileName(final String expectedFileName, final Charset fileNameCharset) throws IOException {
        // Arrange
        final GzipParameters parameters = new GzipParameters();
        parameters.setFileName(expectedFileName);
        parameters.setFileNameCharset(fileNameCharset);
        parameters.setComment("Comment on " + expectedFileName);

        // Act
        try (OutputStream fos = Files.newOutputStream(targetFile);
            GzipCompressorOutputStream gos = new GzipCompressorOutputStream(fos, parameters)) {
            gos.write(sourceFile);
        }

        // Assert: Test with legacy reader (no charset specified)
        // This block verifies a workaround for older clients. The filename is read using the
        // default ISO-8859-1 charset, which corrupts the string. We then get the raw bytes
        // of that corrupted string and re-interpret them using the correct, original charset.
        try (GzipCompressorInputStream gis = new GzipCompressorInputStream(Files.newInputStream(targetFile))) {
            final String rawFileName = gis.getMetaData().getFileName();
            final byte[] rawBytes = rawFileName.getBytes(StandardCharsets.ISO_8859_1);
            final String recoveredFileName = new String(rawBytes, fileNameCharset);

            assertEquals(expectedFileName, recoveredFileName);
            assertArrayEquals(CONTENT, IOUtils.toByteArray(gis));
        }

        // Assert: Test with modern builder, which allows specifying the charset for reading.
        try (GzipCompressorInputStream gis = GzipCompressorInputStream.builder()
                .setPath(targetFile)
                .setFileNameCharset(fileNameCharset)
                .get()) {
            assertEquals(expectedFileName, gis.getMetaData().getFileName());
            assertArrayEquals(CONTENT, IOUtils.toByteArray(gis));
        }
    }
}