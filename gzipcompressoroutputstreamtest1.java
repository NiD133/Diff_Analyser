package org.apache.commons.compress.compressors.gzip;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import shaded.org.apache.commons.io.IOUtils;

/**
 * Tests for GZIP header metadata in {@link GzipCompressorOutputStream},
 * focusing on filenames with special characters and extra fields.
 */
@DisplayName("GzipCompressorOutputStream Header Tests")
class GzipCompressorOutputStreamHeaderTest {

    private static final String CHINESE_BASE_NAME = "\u6D4B\u8BD5\u4E2D\u6587\u540D\u79F0"; // "Test Chinese Name"
    private static final String CHINESE_FILE_NAME = CHINESE_BASE_NAME + ".xml";

    @TempDir
    private Path tempDir;

    /**
     * Tests that a filename with Chinese characters can be correctly written and read back
     * when using the GBK charset, which is common on Windows systems in China.
     */
    @Test
    @DisplayName("Should correctly handle Chinese filename with GBK encoding")
    void testChineseFileNameWithGbkEncoding() throws IOException {
        final String gbkCharsetName = "GBK";
        assumeTrue(Charset.isSupported(gbkCharsetName), "GBK charset is not supported on this system.");

        assertCanRoundtripGzipWithFileName(CHINESE_FILE_NAME, Charset.forName(gbkCharsetName));
    }

    /**
     * Verifies that a GZIP file created with a specific filename and charset can be read back correctly,
     * testing both legacy and modern (builder) GZIP input stream APIs.
     *
     * @param expectedFileName The filename to test.
     * @param fileNameCharset  The charset for encoding the filename.
     */
    private void assertCanRoundtripGzipWithFileName(final String expectedFileName, final Charset fileNameCharset) throws IOException {
        // Arrange
        final byte[] fileContent = "<text>Hello World!</text>".getBytes(StandardCharsets.UTF_8);
        final Path sourceFile = createTempSourceFile(fileContent);
        final Path compressedFile = Files.createTempFile(tempDir, "test", ".gz");

        final GzipParameters parameters = new GzipParameters();
        parameters.setFileName(expectedFileName);
        parameters.setComment("Comment on " + expectedFileName);
        parameters.setFileNameCharset(fileNameCharset);

        // Act: Compress the file
        try (OutputStream fos = Files.newOutputStream(compressedFile);
             GzipCompressorOutputStream gos = new GzipCompressorOutputStream(fos, parameters)) {
            gos.write(sourceFile);
        }

        // Assert: Verify the compressed file can be read back correctly

        // Scenario 1: Reading with the legacy GzipCompressorInputStream constructor.
        // This constructor does not allow specifying a charset for the filename. The GZIP spec mandates
        // ISO-8859-1 for the filename field. We must manually reverse the encoding process to verify.
        try (GzipCompressorInputStream gis = new GzipCompressorInputStream(Files.newInputStream(compressedFile))) {
            final byte[] fileNameBytes = gis.getMetaData().getFileName().getBytes(StandardCharsets.ISO_8859_1);
            final String actualFileName = new String(fileNameBytes, fileNameCharset);

            assertEquals(expectedFileName, actualFileName, "Filename read with legacy API should match");
            assertArrayEquals(fileContent, IOUtils.toByteArray(gis), "File content should match");
        }

        // Scenario 2: Reading with the GzipCompressorInputStream.Builder.
        // This modern API allows specifying the charset, simplifying verification.
        try (GzipCompressorInputStream gis = GzipCompressorInputStream.builder()
                .setPath(compressedFile)
                .setFileNameCharset(fileNameCharset)
                .get()) {
            assertEquals(expectedFileName, gis.getMetaData().getFileName(), "Filename read with Builder API should match");
            assertArrayEquals(fileContent, IOUtils.toByteArray(gis), "File content should match");

            // Verify all metadata was preserved
            gis.getMetaData().setTrailerCrc(0);
            gis.getMetaData().setTrailerISize(0);
            assertEquals(parameters, gis.getMetaData());
        }
    }

    /**
     * Tests that the Gzip stream correctly writes and reads GZIP files containing valid extra header fields.
     */
    @DisplayName("Gzip stream should correctly write and read valid extra header subfields")
    @ParameterizedTest(name = "subFieldCount={0}, payloadSize={1}")
    @CsvSource({
        "0,     0", // No subfields
        "1,     0", // One empty subfield
        "1, 65531", // One subfield with max payload
        "2,     0", // Two empty subfields
        "2, 32763"  // Two subfields, total size near max
    })
    void shouldWriteAndReadValidExtraSubfields(final int subFieldCount, final int payloadSize) throws IOException {
        // Arrange
        final Path sourceFile = createTempSourceFile("Hello World!".getBytes(StandardCharsets.ISO_8859_1));
        final Path compressedFile = Files.createTempFile(tempDir, "test_extra_", ".gz");

        final GzipParameters parameters = new GzipParameters();
        final List<byte[]> payloads = new ArrayList<>();
        final ExtraField extraField = createExtraField(subFieldCount, payloadSize, payloads);
        parameters.setExtraField(extraField);

        // Act
        try (OutputStream fos = Files.newOutputStream(compressedFile);
             GzipCompressorOutputStream gos = new GzipCompressorOutputStream(fos, parameters)) {
            gos.write(sourceFile);
            gos.close();
            assertTrue(gos.isClosed(), "Stream should be closed");
        }

        // Assert
        verifyCompressedFileWithExtraFields(compressedFile, parameters, subFieldCount, payloadSize, payloads);
    }

    /**
     * Verifies the contents and metadata of a GZIP file that was written with extra fields.
     */
    private void verifyCompressedFileWithExtraFields(final Path compressedFile, final GzipParameters originalParameters,
                                                     final int expectedSubFieldCount, final int expectedPayloadSize,
                                                     final List<byte[]> expectedPayloads) throws IOException {
        try (GzipCompressorInputStream gis = new GzipCompressorInputStream(Files.newInputStream(compressedFile))) {
            final GzipParameters readParameters = gis.getMetaData();
            final ExtraField readExtraField = readParameters.getExtraField();

            assertEquals(originalParameters, readParameters);
            assertEquals(expectedSubFieldCount == 0, readExtraField.isEmpty());
            assertEquals(expectedSubFieldCount, readExtraField.size());

            if (expectedSubFieldCount > 0) {
                final int expectedEncodedSize = 4 * expectedSubFieldCount + expectedSubFieldCount * expectedPayloadSize;
                assertEquals(expectedEncodedSize, readExtraField.getEncodedSize());
            }

            final List<SubField> subFields = new ArrayList<>();
            readExtraField.forEach(subFields::add);
            assertEquals(expectedSubFieldCount, subFields.size());

            for (int i = 0; i < expectedSubFieldCount; i++) {
                final SubField sf = subFields.get(i);
                assertEquals("z" + i, sf.getId());
                assertArrayEquals(expectedPayloads.get(i), sf.getPayload(), "Payload for subfield " + i + " should match");
            }
        }
    }

    /**
     * Creates an {@link ExtraField} with a specified number of subfields.
     */
    private ExtraField createExtraField(final int subFieldCount, final int payloadSize, final List<byte[]> outPayloads) throws IOException {
        final ExtraField extra = new ExtraField();
        for (int i = 0; i < subFieldCount; i++) {
            final byte[] payload = ArrayFill.fill(new byte[payloadSize], (byte) ('a' + i));
            outPayloads.add(payload);
            extra.addSubField("z" + i, payload);
        }
        return extra;
    }

    private Path createTempSourceFile(final byte[] content) throws IOException {
        final Path tempSourceFile = Files.createTempFile(tempDir, "test_source", ".txt");
        Files.write(tempSourceFile, content);
        return tempSourceFile;
    }
}