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

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
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
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import shaded.org.apache.commons.io.IOUtils;

/**
 * Readability-focused tests for {@link GzipCompressorOutputStream}.
 *
 * The tests are organized by feature:
 * - File name handling (ASCII, non-ASCII with and without charset configuration)
 * - Extra header subfields
 * - Header CRC and metadata round-trip
 */
class GzipCompressorOutputStreamTest {

    // Common sample content
    private static final byte[] SAMPLE_XML_BYTES = "<text>Hello World!</text>".getBytes(StandardCharsets.ISO_8859_1);

    // Non-ASCII file name parts used by multiple tests
    private static final String CN_BASENAME = "\u6D4B\u8BD5\u4E2D\u6587\u540D\u79F0"; // "Test Chinese name"
    private static final String CN_FILENAME = CN_BASENAME + ".xml";

    @TempDir
    Path tempDir;

    // --------------------------------------------------------------------------------------------
    // Helpers
    // --------------------------------------------------------------------------------------------

    private Path createTempFileWithContent(final String fileName, final byte[] content) throws IOException {
        final Path file = Files.createTempFile(tempDir, stripExtension(fileName), getExtensionOrDefault(fileName));
        Files.write(file, content);
        return file;
    }

    private static String stripExtension(final String fileName) {
        final int dot = fileName.lastIndexOf('.');
        return dot < 0 ? fileName : fileName.substring(0, dot);
    }

    private static String getExtensionOrDefault(final String fileName) {
        final int dot = fileName.lastIndexOf('.');
        return dot < 0 ? ".tmp" : fileName.substring(dot);
    }

    private Path createTempGzipTarget(final String prefix) throws IOException {
        return Files.createTempFile(tempDir, prefix, ".gz");
    }

    private void writeGzip(final Path source, final Path target, final GzipParameters params) throws IOException {
        try (OutputStream fos = Files.newOutputStream(target);
             GzipCompressorOutputStream gos = new GzipCompressorOutputStream(fos, params)) {
            gos.write(source);
        }
    }

    // Reads with the "old" constructor that always decodes file name/comment as ISO-8859-1.
    private GzipCompressorInputStream openGzipOld(final Path gzipFile) throws IOException {
        return new GzipCompressorInputStream(Files.newInputStream(gzipFile));
    }

    // Reads with the builder that allows specifying the file name charset.
    private GzipCompressorInputStream openGzipWithCharset(final Path gzipFile, final Charset fileNameCharset) throws IOException {
        return GzipCompressorInputStream.builder()
                .setPath(gzipFile)
                .setFileNameCharset(fileNameCharset)
                .get();
    }

    // --------------------------------------------------------------------------------------------
    // File name handling
    // --------------------------------------------------------------------------------------------

    private void assertGzipWithFileNameCharset(final String expectedFileName,
                                               final String fileNameToWrite,
                                               final Charset fileNameCharsetToUse) throws IOException {
        // Given: a source file
        final Path source = createTempFileWithContent(fileNameToWrite, SAMPLE_XML_BYTES);
        final Path target = createTempGzipTarget("file_name_");

        final GzipParameters params = new GzipParameters();
        // For non-ASCII file names, the writer must be configured with the desired charset.
        params.setFileNameCharset(fileNameCharsetToUse);
        assertEquals(fileNameCharsetToUse, params.getFileNameCharset());
        params.setFileName(fileNameToWrite);
        params.setComment("Comment on " + fileNameToWrite);

        // When: writing a gzip member
        writeGzip(source, target, params);

        // Then: old reader (fixed ISO-8859-1) – decode manually to the target charset for comparison
        try (GzipCompressorInputStream gis = openGzipOld(target)) {
            final byte[] rawFileName = gis.getMetaData().getFileName().getBytes(StandardCharsets.ISO_8859_1);
            final String unicodeFileName = new String(rawFileName, fileNameCharsetToUse);
            assertEquals(expectedFileName, unicodeFileName);
            assertArrayEquals(SAMPLE_XML_BYTES, IOUtils.toByteArray(gis));
        }

        // And: new reader (configurable charset) – the name should round-trip as-is
        try (GzipCompressorInputStream gis = openGzipWithCharset(target, fileNameCharsetToUse)) {
            final String actual = gis.getMetaData().getFileName();
            assertEquals(expectedFileName, actual);
            assertArrayEquals(SAMPLE_XML_BYTES, IOUtils.toByteArray(gis));

            // Trailer values (CRC and ISIZE) are set during read, clear them for a simple parameters equality check
            gis.getMetaData().setTrailerCrc(0);
            gis.getMetaData().setTrailerISize(0);
            assertEquals(params, gis.getMetaData());
        }
    }

    /**
     * Windows users with a Chinese locale typically use GBK for file names.
     */
    @Test
    void fileName_chinese_GBK_roundTrip() throws IOException {
        assumeTrue(Charset.isSupported("GBK"));
        assertGzipWithFileNameCharset(CN_FILENAME, CN_FILENAME, Charset.forName("GBK"));
    }

    /**
     * On Linux and many modern systems UTF-8 should be used.
     */
    @Test
    void fileName_chinese_UTF8_roundTrip() throws IOException {
        assertGzipWithFileNameCharset(CN_FILENAME, CN_FILENAME, StandardCharsets.UTF_8);
    }

    private void assertAsciiFileNameRoundTrip(final String fileName) throws IOException {
        // Given
        final Path source = createTempFileWithContent(fileName, SAMPLE_XML_BYTES);
        final Path target = createTempGzipTarget("ascii_");

        final GzipParameters params = new GzipParameters();
        // Historical aliases: setFilename() and setFileName() are equivalent
        params.setFilename(fileName);
        assertEquals(params.getFilename(), params.getFileName());
        params.setFileName(fileName);
        assertEquals(params.getFilename(), params.getFileName());

        // When
        writeGzip(source, target, params);

        // Then
        try (GzipCompressorInputStream gis = openGzipOld(target)) {
            assertEquals(fileName, gis.getMetaData().getFileName());
            assertEquals(fileName, gis.getMetaData().getFilename());
            assertArrayEquals(SAMPLE_XML_BYTES, IOUtils.toByteArray(gis));
        }
    }

    @Test
    void fileName_ascii_roundTrip() throws IOException {
        assertAsciiFileNameRoundTrip("ASCII.xml");
    }

    /**
     * COMPRESS-638: Without configuring a charset, non-ISO-8859-1 characters are percent-encoded (seen as '?')
     * because the GZip header uses ISO-8859-1 by default. Use setFileNameCharset for non-ASCII names.
     */
    @Test
    void fileName_chinese_percentEncoded_withoutCharset() throws IOException {
        final String expectedAsSeenByDefaultDecoder = "??????.xml";
        final String fileNameToWrite = CN_FILENAME;

        // Given
        final Path source = createTempFileWithContent(fileNameToWrite, SAMPLE_XML_BYTES);
        final Path target = createTempGzipTarget("cn_default_charset_");

        final GzipParameters params = new GzipParameters();
        // Intentionally do not set file name charset here
        params.setFileName(fileNameToWrite);

        // When
        writeGzip(source, target, params);

        // Then
        try (GzipCompressorInputStream gis = openGzipOld(target)) {
            assertEquals(expectedAsSeenByDefaultDecoder, gis.getMetaData().getFileName());
            assertEquals(expectedAsSeenByDefaultDecoder, gis.getMetaData().getFilename());
            assertArrayEquals(SAMPLE_XML_BYTES, IOUtils.toByteArray(gis));
        }
    }

    // --------------------------------------------------------------------------------------------
    // Extra subfields
    // --------------------------------------------------------------------------------------------

    @ParameterizedTest(name = "subFields={0}, payloadSize={1}, expectFailure={2}")
    @CsvSource({
            // count, payloadSize, shouldFail
            "0,    42, false",
            "1,      , true",
            "1,     0, false",
            "1, 65531, false",
            "1, 65532, true",
            "2,     0, false",
            "2, 32764, true",
            "2, 32763, false"
    })
    void extraSubfields_add_encode_decode(final int subFieldCount, final Integer payloadSize, final boolean shouldFail)
            throws IOException {
        // Given
        final Path source = createTempFileWithContent("extra.txt", "Hello World!".getBytes(StandardCharsets.ISO_8859_1));
        final Path target = createTempGzipTarget("extra_");

        final GzipParameters params = new GzipParameters();
        final ExtraField extra = new ExtraField();
        boolean failedDuringAdd = false;

        final byte[][] payloads = new byte[subFieldCount][];
        for (int i = 0; i < subFieldCount; i++) {
            if (payloadSize != null) {
                payloads[i] = ArrayFill.fill(new byte[payloadSize], (byte) ('a' + i));
            }
            try {
                extra.addSubField("z" + i, payloads[i]);
            } catch (final NullPointerException | IOException e) {
                failedDuringAdd = true;
                break;
            }
        }

        assertEquals(shouldFail, failedDuringAdd,
                "Subfield addition " + (shouldFail ? "should have failed" : "should have succeeded"));

        if (shouldFail) {
            // Nothing else to verify
            return;
        }

        if (subFieldCount > 0) {
            assertThrows(UnsupportedOperationException.class, () -> extra.iterator().remove(),
                    "Iterator must be read-only");
        }

        params.setExtraField(extra);

        // When: write and close
        try (OutputStream fos = Files.newOutputStream(target);
             GzipCompressorOutputStream gos = new GzipCompressorOutputStream(fos, params)) {
            gos.write(source);
            gos.close(); // explicit close to assert closed state
            assertTrue(gos.isClosed());
        }

        // Then: read and verify
        try (GzipCompressorInputStream gis = openGzipOld(target)) {
            final ExtraField readExtra = gis.getMetaData().getExtraField();

            assertEquals(params, gis.getMetaData());
            assertEquals(subFieldCount == 0, readExtra.isEmpty());
            assertEquals(subFieldCount, readExtra.size());

            final int expectedEncodedSize = subFieldCount * 4 + subFieldCount * payloadSize;
            assertEquals(expectedEncodedSize, readExtra.getEncodedSize());

            final ArrayList<SubField> copy = new ArrayList<>();
            readExtra.forEach(copy::add);
            assertEquals(subFieldCount, copy.size());

            for (int i = 0; i < subFieldCount; i++) {
                final SubField sf = readExtra.getSubField(i);
                assertSame(sf, copy.get(i));
                assertSame(sf, readExtra.findFirstSubField("z" + i));
                assertEquals("z" + i, sf.getId(), "Subfield id must round-trip");
                assertArrayEquals(payloads[i], sf.getPayload(), "Subfield payload mismatch at index " + i);
            }

            readExtra.clear();
            assertTrue(readExtra.isEmpty());
        }
    }

    @Test
    void extraSubfields_empty_hasNoBytesOrElements() {
        final ExtraField extra = new ExtraField();
        assertEquals(0, extra.toByteArray().length, "Empty extra field should encode to zero bytes");
        assertFalse(extra.iterator().hasNext(), "Empty iterator for empty extra field");
        extra.forEach(e -> fail("Should not iterate any element for an empty extra field"));
        assertThrows(IndexOutOfBoundsException.class, () -> extra.getSubField(0));
    }

    // --------------------------------------------------------------------------------------------
    // Header CRC and metadata round-trip
    // --------------------------------------------------------------------------------------------

    @Test
    void headerCrc_and_metadata_roundTrip() throws IOException, DecoderException {
        // Given: parameters with all header fields and HCRC enabled
        final GzipParameters params = new GzipParameters();
        params.setHeaderCRC(true);
        params.setModificationTime(0x66554433); // fixed for repeatability
        params.setFileName("AAAA");
        params.setComment("ZZZZ");
        params.setOS(OS.UNIX);

        final ExtraField extra = new ExtraField();
        extra.addSubField("BB", "CCCC".getBytes(StandardCharsets.ISO_8859_1));
        params.setExtraField(extra);

        // When: write header and finish (no body payload needed)
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (GzipCompressorOutputStream gos = new GzipCompressorOutputStream(baos, params)) {
            // no body
        }
        final byte[] actual = baos.toByteArray();

        // Then: exact bytes (header + empty deflate stream + trailer)
        final byte[] expected = Hex.decodeHex(
                "1f8b"         // id1 id2
              + "08"           // cm
              + "1e"           // flg (FEXTRA|FNAME|FCOMMENT|FHCRC)
              + "33445566"     // mtime (LE)
              + "00" + "03"    // xfl os
              + "0800"         // xlen
              + "4242" + "0400" + "43434343" // extra: id=BB, len=4, payload="CCCC"
              + "4141414100"   // file name "AAAA" + NUL
              + "5a5a5a5a00"   // comment "ZZZZ" + NUL
              + "d842"         // header CRC16 (LE)
              + "0300"         // empty deflate stream
              + "00000000"     // data CRC32
              + "00000000"     // ISIZE
        );
        assertArrayEquals(expected, actual, "Header bytes must match");

        // And: JDK GZIPInputStream should accept it (valid HCRC)
        assertDoesNotThrow(() -> {
            try (GZIPInputStream ignored = new GZIPInputStream(new ByteArrayInputStream(actual))) {
                // no read needed
            }
        });

        // And: metadata should match what we wrote
        try (GzipCompressorInputStream gis = new GzipCompressorInputStream(new ByteArrayInputStream(actual))) {
            final GzipParameters meta = gis.getMetaData();
            assertTrue(meta.getHeaderCRC());
            assertEquals(0x66554433, meta.getModificationTime());
            assertEquals(1, meta.getExtraField().size());
            final SubField sf = meta.getExtraField().iterator().next();
            assertEquals("BB", sf.getId());
            assertEquals("CCCC", new String(sf.getPayload(), StandardCharsets.ISO_8859_1));
            assertEquals("AAAA", meta.getFileName());
            assertEquals("ZZZZ", meta.getComment());
            assertEquals(OS.UNIX, meta.getOS());
            assertEquals(params, meta);
        }

        // And: a corrupted HCRC should make JDK GZIPInputStream fail during header processing
        assertThrows(ZipException.class, () -> {
            actual[30] = 0x77; // flip low byte of header CRC
            try (GZIPInputStream ignored = new GZIPInputStream(new ByteArrayInputStream(actual))) {
                // should fail before any read
            }
        }, "JDK should reject streams with a bad header CRC");
    }
}