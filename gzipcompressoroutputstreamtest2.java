package org.apache.commons.compress.compressors.gzip;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import org.apache.commons.compress.compressors.gzip.ExtraField.SubField;
import org.apache.commons.lang3.ArrayFill;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import shaded.org.apache.commons.io.IOUtils;

/**
 * Tests for advanced features of {@link GzipCompressorOutputStream} configured via {@link GzipParameters}.
 */
@DisplayName("GzipCompressorOutputStream with custom parameters")
public class GzipCompressorOutputStreamParametersTest {

    @TempDir
    private Path tempDir;

    private static final String CHINESE_FILE_NAME = "\u6D4B\u8BD5\u4E2D\u6587\u540D\u79F0.xml"; // "测试中文名称.xml"

    @Test
    @DisplayName("Should correctly write and read a filename with Chinese characters using UTF-8")
    void testFileNameWithChineseCharactersUsingUtf8() throws IOException {
        // Arrange
        final Charset fileNameCharset = StandardCharsets.UTF_8;
        final byte[] fileContent = "<text>Hello World!</text>".getBytes(StandardCharsets.UTF_8);
        final Path compressedFile = Files.createTempFile(tempDir, "test-", ".gz");

        final GzipParameters writeParameters = new GzipParameters();
        writeParameters.setFileName(CHINESE_FILE_NAME);
        writeParameters.setComment("Comment on " + CHINESE_FILE_NAME);
        writeParameters.setFileNameCharset(fileNameCharset);

        // Act: Write the GZIP file with the specified parameters.
        try (OutputStream fos = Files.newOutputStream(compressedFile);
             GzipCompressorOutputStream gos = new GzipCompressorOutputStream(fos, writeParameters)) {
            gos.write(fileContent);
        }

        // Assert

        // 1. Verify reading with the legacy GzipCompressorInputStream constructor.
        // This constructor does not allow specifying a charset for the filename, so we must manually decode.
        try (GzipCompressorInputStream gis = new GzipCompressorInputStream(Files.newInputStream(compressedFile))) {
            // The GZIP spec mandates that the filename is stored as ISO-8859-1 bytes.
            // To recover the original UTF-8 string, we must reverse the process:
            // 1. Get the filename as a Java String (wrongly decoded by the stream).
            // 2. Get the raw bytes of that string using the same incorrect encoding (ISO-8859-1).
            // 3. Construct a new String from these raw bytes using the correct, original encoding (UTF-8).
            final byte[] rawFileNameBytes = gis.getMetaData().getFileName().getBytes(StandardCharsets.ISO_8859_1);
            final String recoveredFileName = new String(rawFileNameBytes, fileNameCharset);

            assertEquals(CHINESE_FILE_NAME, recoveredFileName);
            assertArrayEquals(fileContent, IOUtils.toByteArray(gis));
        }

        // 2. Verify reading with the modern GzipCompressorInputStream.Builder, which handles charsets correctly.
        try (GzipCompressorInputStream gis = GzipCompressorInputStream.builder()
            .setPath(compressedFile)
            .setFileNameCharset(fileNameCharset)
            .get()) {

            // With the correct charset provided, the filename should be decoded correctly.
            assertEquals(CHINESE_FILE_NAME, gis.getMetaData().getFileName());
            assertArrayEquals(fileContent, IOUtils.toByteArray(gis));

            // To compare the full metadata object, we must align the trailer CRC and size,
            // as these are computed during the write process and not part of the initial parameters.
            final GzipParameters readParameters = gis.getMetaData();
            readParameters.setTrailerCrc(0);
            readParameters.setTrailerISize(0);
            assertEquals(writeParameters, readParameters);
        }
    }

    @DisplayName("Adding subfields that exceed size limits or have null payload should fail")
    @ParameterizedTest(name = "[{index}] subFieldCount={0}, payloadSize={1}")
    @MethodSource("invalidExtraFieldParameters")
    void testAddingInvalidExtraSubfieldsThrowsException(final int subFieldCount, final Integer payloadSize, final Class<? extends Throwable> expectedException) {
        // Arrange
        final ExtraField extraField = new ExtraField();

        // Act & Assert
        assertThrows(expectedException, () -> {
            for (int i = 0; i < subFieldCount; i++) {
                final byte[] payload = payloadSize != null ? new byte[payloadSize] : null;
                extraField.addSubField("z" + i, payload);
            }
        });
    }

    private static Stream<Arguments> invalidExtraFieldParameters() {
        return Stream.of(
            // A null payload should throw NullPointerException
            Arguments.of(1, null, NullPointerException.class),
            // A single subfield whose payload makes the total extra field size exceed 65535 bytes
            Arguments.of(1, 65532, IOException.class), // 4 (header) + 65532 (payload) = 65536
            // Two subfields whose combined size exceeds 65535 bytes
            Arguments.of(2, 32764, IOException.class)  // (4 + 32764) * 2 = 65536
        );
    }

    @DisplayName("Gzip file with valid extra fields should be written and read correctly")
    @ParameterizedTest(name = "[{index}] subFieldCount={0}, payloadSize={1}")
    @MethodSource("validExtraFieldParameters")
    void testExtraSubfieldsAreWrittenAndReadCorrectly(final int subFieldCount, final int payloadSize) throws IOException {
        // Arrange
        final byte[] content = "Hello World!".getBytes(StandardCharsets.UTF_8);
        final Path compressedFile = Files.createTempFile(tempDir, "gzip-extra-dst", ".gz");

        final GzipParameters parameters = new GzipParameters();
        final ExtraField sourceExtraField = createExtraFieldWithSubfields(subFieldCount, payloadSize);
        parameters.setExtraField(sourceExtraField);

        // Act
        try (OutputStream fos = Files.newOutputStream(compressedFile);
             GzipCompressorOutputStream gos = new GzipCompressorOutputStream(fos, parameters)) {
            gos.write(content);
        }

        // Assert
        try (GzipCompressorInputStream gis = new GzipCompressorInputStream(Files.newInputStream(compressedFile))) {
            final ExtraField readExtraField = gis.getMetaData().getExtraField();

            // Verify properties of the read ExtraField
            assertEquals(subFieldCount, readExtraField.size());
            assertEquals(sourceExtraField.getEncodedSize(), readExtraField.getEncodedSize());
            if (subFieldCount > 0) {
                assertThrows(UnsupportedOperationException.class, () -> readExtraField.iterator().remove());
            }

            // Verify each subfield was read correctly
            for (int i = 0; i < subFieldCount; i++) {
                final SubField sourceSubField = sourceExtraField.getSubField(i);
                final SubField readSubField = readExtraField.getSubField(i);
                assertEquals(sourceSubField.getId(), readSubField.getId());
                assertArrayEquals(sourceSubField.getPayload(), readSubField.getPayload(), "Payload for subfield " + i + " differs");
            }

            // Verify clear() works as expected
            readExtraField.clear();
            assertTrue(readExtraField.isEmpty());

            // Verify the decompressed content is correct
            assertArrayEquals(content, IOUtils.toByteArray(gis));
        }
    }

    private static Stream<Arguments> validExtraFieldParameters() {
        return Stream.of(
            Arguments.of(0, 0),      // No extra fields
            Arguments.of(1, 0),      // One subfield, empty payload
            Arguments.of(1, 65531),  // One subfield, max valid payload size (4 + 65531 = 65535)
            Arguments.of(2, 0),      // Two subfields, empty payloads
            Arguments.of(2, 32763)   // Two subfields, large payloads ((4 + 32763) * 2 = 65534)
        );
    }

    private ExtraField createExtraFieldWithSubfields(final int subFieldCount, final int payloadSize) throws IOException {
        final ExtraField extraField = new ExtraField();
        for (int i = 0; i < subFieldCount; i++) {
            final byte[] payload = ArrayFill.fill(new byte[payloadSize], (byte) ('a' + i));
            extraField.addSubField("z" + i, payload);
        }
        return extraField;
    }
}