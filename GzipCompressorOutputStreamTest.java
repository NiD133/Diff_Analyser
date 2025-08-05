/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.commons.compress.compressors.gzip;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.zip.CRC32;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipException;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.compress.compressors.gzip.ExtraField.SubField;
import org.apache.commons.compress.compressors.gzip.GzipParameters.OS;
import org.apache.commons.lang3.ArrayFill;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import shaded.org.apache.commons.io.IOUtils;

/**
 * Tests for {@link GzipCompressorOutputStream}.
 */
class GzipCompressorOutputStreamTest {

    // A temporary directory for test files, managed by JUnit.
    @TempDir
    private Path tempDir;

    private static final String CHINESE_TEXT_SAMPLE = "\u6D4B\u8BD5\u4E2D\u6587\u540D\u79F0"; // "Test Chinese Name"
    private static final String CHINESE_FILENAME = CHINESE_TEXT_SAMPLE + ".xml";
    private static final byte[] DUMMY_CONTENT = "<text>Hello World!</text>".getBytes(StandardCharsets.ISO_8859_1);

    /**
     * Helper method to create a temporary source file with dummy content.
     */
    private Path createDummySourceFile(final String prefix) throws IOException {
        final Path sourceFile = Files.createTempFile(tempDir, prefix, ".txt");
        Files.write(sourceFile, DUMMY_CONTENT);
        return sourceFile;
    }

    @DisplayName("Should correctly write and read a filename with non-ASCII characters when a charset is specified")
    @ParameterizedTest(name = "Using charset: {0}")
    @ValueSource(strings = { "GBK", "UTF-8" })
    void shouldWriteAndReadChineseFilenameWithSpecifiedCharset(final String charsetName) throws IOException {
        // Arrange
        final Charset charset = Charset.forName(charsetName);
        assumeTrue(Charset.isSupported(charsetName), "Charset " + charsetName + " is not supported on this system.");

        final Path sourceFile = createDummySourceFile("source-chinese-");
        final Path compressedFile = tempDir.resolve("test-chinese-name.gz");

        final GzipParameters parameters = new GzipParameters();
        parameters.setFileName(CHINESE_FILENAME);
        parameters.setComment("Comment for " + CHINESE_FILENAME);
        parameters.setFileNameCharset(charset);

        // Act: Write the GZIP file with the specified filename encoding
        try (OutputStream fos = Files.newOutputStream(compressedFile);
             GzipCompressorOutputStream gos = new GzipCompressorOutputStream(fos, parameters)) {
            gos.write(sourceFile);
        }

        // Assert: Read the GZIP file and verify the filename and content
        // @formatter:off
        try (GzipCompressorInputStream gis = GzipCompressorInputStream.builder()
                .setPath(compressedFile)
                .setFileNameCharset(charset)
                .get()) {
            // @formatter:on
            final GzipParameters readParameters = gis.getMetaData();

            // The filename should be decoded correctly.
            assertEquals(CHINESE_FILENAME, readParameters.getFileName());

            // The full metadata should match, ignoring trailer values which are calculated on the fly.
            readParameters.setTrailerCrc(0);
            readParameters.setTrailerISize(0);
            assertEquals(parameters, readParameters);

            // The content should be intact.
            assertArrayEquals(DUMMY_CONTENT, IOUtils.toByteArray(gis));
        }
    }

    @Test
    @DisplayName("Should replace non-ISO-8859-1 characters in filename when no charset is specified (Gzip default)")
    void shouldReplaceNonLatin1CharsInFilenameWhenNoCharsetIsSpecified() throws IOException {
        // Arrange
        final Path sourceFile = createDummySourceFile("source-unsupported-");
        final Path compressedFile = tempDir.resolve("test-default-encoding.gz");
        final String expectedFilenameAfterEncoding = "??????.xml"; // Chinese chars replaced by '?'

        final GzipParameters parameters = new GzipParameters();
        // Set a filename with characters outside of ISO-8859-1, but do not specify a charset.
        parameters.setFilename(CHINESE_FILENAME);

        // Act
        try (OutputStream fos = Files.newOutputStream(compressedFile);
             GzipCompressorOutputStream gos = new GzipCompressorOutputStream(fos, parameters)) {
            gos.write(sourceFile);
        }

        // Assert
        try (GzipCompressorInputStream gis = new GzipCompressorInputStream(Files.newInputStream(compressedFile))) {
            // Per GZIP spec (RFC 1952), the filename is interpreted as ISO-8859-1 by default.
            assertEquals(expectedFilenameAfterEncoding, gis.getMetaData().getFileName());
            assertArrayEquals(DUMMY_CONTENT, IOUtils.toByteArray(gis));
        }
    }

    @Test
    @DisplayName("Should correctly write and read a simple ASCII filename")
    void shouldHandleAsciiFilename() throws IOException {
        // Arrange
        final String asciiFilename = "test-ascii.xml";
        final Path sourceFile = createDummySourceFile("source-ascii-");
        final Path compressedFile = tempDir.resolve("test-ascii.gz");

        final GzipParameters parameters = new GzipParameters();
        parameters.setFilename(asciiFilename);

        // Act
        try (OutputStream fos = Files.newOutputStream(compressedFile);
             GzipCompressorOutputStream gos = new GzipCompressorOutputStream(fos, parameters)) {
            gos.write(sourceFile);
        }

        // Assert
        try (GzipCompressorInputStream gis = new GzipCompressorInputStream(Files.newInputStream(compressedFile))) {
            assertEquals(asciiFilename, gis.getMetaData().getFileName());
            assertArrayEquals(DUMMY_CONTENT, IOUtils.toByteArray(gis));
        }
    }

    @Test
    @DisplayName("Should correctly handle an empty ExtraField")
    void shouldHandleEmptyExtraField() {
        // Arrange
        final ExtraField extraField = new ExtraField();

        // Act & Assert
        assertEquals(0, extraField.toByteArray().length, "Byte array of an empty ExtraField should be empty.");
        assertFalse(extraField.iterator().hasNext(), "Iterator of an empty ExtraField should not have a next element.");
        assertThrows(IndexOutOfBoundsException.class, () -> extraField.getSubField(0), "Accessing subfield by index should fail for an empty ExtraField.");
        extraField.forEach(e -> fail("forEach should not be called on an empty ExtraField."));
    }

    @DisplayName("Should reject invalid subfields added to ExtraField")
    @ParameterizedTest(name = "Reject when total payload size exceeds limits with payload of size {0}")
    @ValueSource(ints = { 65531, 32764 }) // Values that, with a second field, exceed total length limits
    void shouldThrowExceptionWhenAddingInvalidExtraSubfield(final int payloadSize) {
        // Arrange
        final ExtraField extraField = new ExtraField();
        final byte[] largePayload = new byte[payloadSize];

        // Act & Assert
        // Adding one large field is fine.
        assertDoesNotThrow(() -> extraField.addSubField("z0", largePayload));
        // Adding a second one should throw, as the total length would exceed the 65535 limit for the 'XLEN' GZIP header field.
        assertThrows(IOException.class, () -> extraField.addSubField("z1", new byte[4]), "Adding a subfield that exceeds total size limit should fail.");
    }

    @DisplayName("Should correctly write and read GZIP streams with extra header subfields")
    @ParameterizedTest(name = "With {0} subfield(s) and payload size {1}")
    @CsvSource({ "0, 0", "1, 42", "2, 100" })
    void shouldWriteAndReadStreamWithExtraSubfields(final int subFieldCount, final int payloadSize) throws IOException {
        // Arrange
        final Path sourceFile = createDummySourceFile("source-extra-");
        final Path compressedFile = tempDir.resolve("test-extra-fields.gz");
        final GzipParameters parameters = new GzipParameters();
        final ExtraField sourceExtraField = new ExtraField();
        final byte[][] payloads = new byte[subFieldCount][];

        for (int i = 0; i < subFieldCount; i++) {
            payloads[i] = ArrayFill.fill(new byte[payloadSize], (byte) ('a' + i));
            sourceExtraField.addSubField("z" + i, payloads[i]);
        }
        parameters.setExtraField(sourceExtraField);

        // Act: Write GZIP with extra fields
        try (OutputStream fos = Files.newOutputStream(compressedFile);
             GzipCompressorOutputStream gos = new GzipCompressorOutputStream(fos, parameters)) {
            gos.write(sourceFile);
            gos.close();
            assertTrue(gos.isClosed(), "Stream should be closed after use.");
        }

        // Assert: Read GZIP and verify extra fields
        try (GzipCompressorInputStream gis = new GzipCompressorInputStream(Files.newInputStream(compressedFile))) {
            final ExtraField readExtraField = gis.getMetaData().getExtraField();

            assertEquals(sourceExtraField, readExtraField);
            assertEquals(subFieldCount, readExtraField.size());

            for (int i = 0; i < subFieldCount; i++) {
                final SubField subField = readExtraField.findFirstSubField("z" + i);
                assertArrayEquals(payloads[i], subField.getPayload(), "Payload for subfield " + i + " should match.");
            }
        }
    }


    @Test
    @DisplayName("Should generate a valid GZIP header with Header CRC enabled")
    void shouldGenerateValidHeaderWithCrc() throws IOException, DecoderException {
        // Arrange
        final GzipParameters parameters = new GzipParameters();
        parameters.setHeaderCRC(true);
        parameters.setModificationTime(0x66554433); // Fixed timestamp for reproducible output
        parameters.setFileName("TestFile");
        parameters.setComment("TestComment");
        parameters.setOS(OS.UNIX);

        final ExtraField extraField = new ExtraField();
        extraField.addSubField("EX", "Data".getBytes(StandardCharsets.ISO_8859_1));
        parameters.setExtraField(extraField);

        // This hand-crafted byte array ensures byte-level compatibility with GZIP spec (RFC 1952).
        // The HCRC value (e0b5) is pre-calculated from the header fields that precede it.
        final byte[] expectedOutput = Hex.decodeHex(
                "1f8b"       // ID1, ID2: GZIP magic number
                        + "08"         // CM: Deflate compression method
                        + "1e"         // FLG: FEXTRA | FNAME | FCOMMENT | FHCRC
                        + "33445566"   // MTIME: 0x66554433 (little-endian)
                        + "00"         // XFL: 0 (no extra flags)
                        + "03"         // OS: 3 (Unix)
                        + "0800"       // XLEN: 8 bytes for extra field (little-endian)
                        + "4558"       //   Subfield ID: "EX"
                        + "0400"       //   Subfield Length: 4
                        + "44617461"   //   Subfield Data: "Data"
                        + "5465737446696c6500" // FNAME: "TestFile" (NUL-terminated)
                        + "54657374436f6d6d656e7400" // FCOMMENT: "TestComment" (NUL-terminated)
                        + "e0b5"       // HCRC: Header CRC16
                        + "0300"       // Empty deflate block
                        + "00000000"   // CRC32 of uncompressed data (empty)
                        + "00000000"   // ISIZE of uncompressed data (empty)
        );

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // Act
        try (GzipCompressorOutputStream gos = new GzipCompressorOutputStream(baos, parameters)) {
            // Write no content; we are only testing the header and trailer.
        }
        final byte[] actualOutput = baos.toByteArray();

        // Assert
        assertArrayEquals(expectedOutput, actualOutput, "Generated GZIP stream with HCRC should match the expected byte pattern.");

        // Verify that the standard JDK GZIPInputStream can read the stream, which validates the HCRC.
        assertDoesNotThrow(() -> {
            try (GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(actualOutput))) {
                assertEquals(-1, gis.read()); // Read to end to trigger header checks.
            }
        }, "Standard GZIPInputStream should parse the HCRC-enabled header without errors.");

        // Verify our own GzipCompressorInputStream can parse it and extract metadata correctly.
        try (GzipCompressorInputStream gis = new GzipCompressorInputStream(new ByteArrayInputStream(actualOutput))) {
            final GzipParameters readMetaData = gis.getMetaData();
            assertTrue(readMetaData.getHeaderCRC());
            assertEquals(parameters, readMetaData);
        }

        // Verify that a corrupted header CRC is detected.
        actualOutput[30] = 0x77; // Corrupt the low byte of the header CRC.
        assertThrows(ZipException.class, () -> {
            try (GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(actualOutput))) {
                gis.read();
            }
        }, "A corrupted header CRC should cause a ZipException.");
    }

}