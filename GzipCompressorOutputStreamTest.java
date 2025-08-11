/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.commons.compress.compressors.gzip;

import static org.junit.jupiter.api.Assertions.*;

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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import shaded.org.apache.commons.io.IOUtils;

/**
 * Tests {@link GzipCompressorOutputStream}.
 */
class GzipCompressorOutputStreamTest {

    private static final String CHINESE_BASE_NAME = "\u6D4B\u8BD5\u4E2D\u6587\u540D\u79F0";
    private static final String CHINESE_FILE_NAME = CHINESE_BASE_NAME + ".xml";

    /**
     * Tests the correct handling of Chinese file names with different charsets.
     */
    private void verifyChineseFileNameHandling(final String expectedFileName, final String sourceFileName, final Charset charset) throws IOException {
        final Path tempSourceFile = createTempFileWithContent(sourceFileName, "<text>Hello World!</text>");
        final Path compressedFile = Files.createTempFile(CHINESE_BASE_NAME, ".gz");

        GzipParameters parameters = createGzipParametersWithCharset(charset);
        compressFile(tempSourceFile, compressedFile, parameters);

        verifyCompressedFileContent(compressedFile, expectedFileName, charset, "<text>Hello World!</text>");
    }

    /**
     * Creates a temporary file with the specified content.
     */
    private Path createTempFileWithContent(String fileName, String content) throws IOException {
        Path tempFile = Files.createTempFile(fileName, fileName);
        Files.write(tempFile, content.getBytes(StandardCharsets.ISO_8859_1));
        return tempFile;
    }

    /**
     * Creates GzipParameters with the specified charset.
     */
    private GzipParameters createGzipParametersWithCharset(Charset charset) {
        GzipParameters parameters = new GzipParameters();
        parameters.setFileNameCharset(charset);
        parameters.setFileName(CHINESE_FILE_NAME);
        parameters.setComment("Comment on " + CHINESE_FILE_NAME);
        return parameters;
    }

    /**
     * Compresses the source file into the target file using the specified parameters.
     */
    private void compressFile(Path sourceFile, Path targetFile, GzipParameters parameters) throws IOException {
        try (OutputStream fos = Files.newOutputStream(targetFile);
             GzipCompressorOutputStream gos = new GzipCompressorOutputStream(fos, parameters)) {
            gos.write(sourceFile);
        }
    }

    /**
     * Verifies the content of the compressed file.
     */
    private void verifyCompressedFileContent(Path compressedFile, String expectedFileName, Charset charset, String expectedContent) throws IOException {
        try (GzipCompressorInputStream gis = new GzipCompressorInputStream(Files.newInputStream(compressedFile))) {
            String actualFileName = new String(gis.getMetaData().getFileName().getBytes(StandardCharsets.ISO_8859_1), charset);
            assertEquals(expectedFileName, actualFileName);
            assertArrayEquals(expectedContent.getBytes(StandardCharsets.ISO_8859_1), IOUtils.toByteArray(gis));
        }
    }

    /**
     * Tests Chinese file name handling on Windows with GBK charset.
     */
    @Test
    void testChineseFileNameGBK() throws IOException {
        assumeTrue(Charset.isSupported("GBK"));
        verifyChineseFileNameHandling(CHINESE_FILE_NAME, CHINESE_FILE_NAME, Charset.forName("GBK"));
    }

    /**
     * Tests Chinese file name handling with UTF-8 charset.
     */
    @Test
    void testChineseFileNameUTF8() throws IOException {
        verifyChineseFileNameHandling(CHINESE_FILE_NAME, CHINESE_FILE_NAME, StandardCharsets.UTF_8);
    }

    /**
     * Tests the gzip extra header containing subfields.
     */
    @ParameterizedTest
    @CsvSource({
        "0,    42, false",
        "1,      , true",
        "1,     0, false",
        "1, 65531, false",
        "1, 65532, true",
        "2,     0, false",
        "2, 32764, true",
        "2, 32763, false"
    })
    void testExtraSubfields(final int subFieldCount, final Integer payloadSize, final boolean shouldFail) throws IOException {
        final Path tempSourceFile = createTempFileWithContent("test_gzip_extra_", "Hello World!");
        final Path compressedFile = Files.createTempFile("test_gzip_extra_", ".txt.gz");

        GzipParameters parameters = new GzipParameters();
        ExtraField extraField = createExtraFieldWithSubfields(subFieldCount, payloadSize, shouldFail);

        if (shouldFail) {
            return;
        }

        parameters.setExtraField(extraField);
        compressFile(tempSourceFile, compressedFile, parameters);
        verifyExtraFieldContent(compressedFile, parameters, subFieldCount, payloadSize);
    }

    /**
     * Creates an ExtraField with the specified subfields.
     */
    private ExtraField createExtraFieldWithSubfields(int subFieldCount, Integer payloadSize, boolean shouldFail) {
        ExtraField extraField = new ExtraField();
        boolean failed = false;
        final byte[][] payloads = new byte[subFieldCount][];

        for (int i = 0; i < subFieldCount; i++) {
            if (payloadSize != null) {
                payloads[i] = ArrayFill.fill(new byte[payloadSize], (byte) ('a' + i));
            }
            try {
                extraField.addSubField("z" + i, payloads[i]);
            } catch (final NullPointerException | IOException e) {
                failed = true;
                break;
            }
        }
        assertEquals(shouldFail, failed, "add subfield " + (shouldFail ? "success" : "failure") + " was not expected.");
        return extraField;
    }

    /**
     * Verifies the content of the extra field in the compressed file.
     */
    private void verifyExtraFieldContent(Path compressedFile, GzipParameters parameters, int subFieldCount, Integer payloadSize) throws IOException {
        try (GzipCompressorInputStream gis = new GzipCompressorInputStream(Files.newInputStream(compressedFile))) {
            ExtraField extraField = gis.getMetaData().getExtraField();
            assertEquals(parameters, gis.getMetaData());
            assertEquals(subFieldCount == 0, extraField.isEmpty());
            assertEquals(subFieldCount, extraField.size());
            assertEquals(4 * subFieldCount + subFieldCount * payloadSize, extraField.getEncodedSize());

            ArrayList<SubField> subFields = new ArrayList<>();
            extraField.forEach(subFields::add);
            assertEquals(subFieldCount, subFields.size());

            for (int i = 0; i < subFieldCount; i++) {
                SubField subField = extraField.getSubField(i);
                assertSame(subField, subFields.get(i));
                assertSame(subField, extraField.findFirstSubField("z" + i));
                assertEquals("z" + i, subField.getId());
                assertArrayEquals(payloads[i], subField.getPayload(), "field " + i + " has wrong payload");
            }
            extraField.clear();
            assertTrue(extraField.isEmpty());
        }
    }

    @Test
    void testExtraSubfieldsEmpty() {
        final ExtraField extra = new ExtraField();
        assertEquals(0, extra.toByteArray().length);
        assertFalse(extra.iterator().hasNext());
        extra.forEach(e -> fail("Not empty."));
        assertThrows(IndexOutOfBoundsException.class, () -> extra.getSubField(0));
    }

    private void verifyFileNameHandling(final String expectedFileName, final String sourceFileName) throws IOException {
        final Path tempSourceFile = createTempFileWithContent(sourceFileName, "<text>Hello World!</text>");
        final Path compressedFile = Files.createTempFile("test", ".gz");

        GzipParameters parameters = new GzipParameters();
        parameters.setFilename(sourceFileName);
        parameters.setFileName(sourceFileName);

        compressFile(tempSourceFile, compressedFile, parameters);
        verifyCompressedFileContent(compressedFile, expectedFileName, StandardCharsets.ISO_8859_1, "<text>Hello World!</text>");
    }

    @Test
    void testFileNameAscii() throws IOException {
        verifyFileNameHandling("ASCII.xml", "ASCII.xml");
    }

    @Test
    void testFileNameChinesePercentEncoded() throws IOException {
        verifyFileNameHandling("??????.xml", CHINESE_FILE_NAME);
    }

    @Test
    void testHeaderCrc() throws IOException, DecoderException {
        final GzipParameters parameters = createGzipParametersWithHeaderCrc();
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (GzipCompressorOutputStream gos = new GzipCompressorOutputStream(baos, parameters)) {
            // nothing to write for this test.
        }

        final byte[] result = baos.toByteArray();
        final byte[] expected = Hex.decodeHex("1f8b" // id1 id2
                + "08" // cm
                + "1e" // flg(FEXTRA|FNAME|FCOMMENT|FHCRC)
                + "33445566" // mtime little endian
                + "00" + "03" // xfl os
                + "0800" + "4242" + "0400" + "43434343" // xlen sfid sflen "CCCC"
                + "4141414100" // "AAAA" with \0
                + "5a5a5a5a00" // "ZZZZ" with \0
                + "d842" // crc32 = 839242d8
                + "0300" // empty deflate stream
                + "00000000" // crs32
                + "00000000" // isize
        );

        assertArrayEquals(expected, result);
        assertDoesNotThrow(() -> {
            try (GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(result))) {
                // if it does not fail, the hcrc is good.
            }
        });

        try (GzipCompressorInputStream gis = new GzipCompressorInputStream(new ByteArrayInputStream(result))) {
            final GzipParameters metaData = gis.getMetaData();
            assertTrue(metaData.getHeaderCRC());
            assertEquals(0x66554433, metaData.getModificationTime());
            assertEquals(1, metaData.getExtraField().size());
            final SubField sf = metaData.getExtraField().iterator().next();
            assertEquals("BB", sf.getId());
            assertEquals("CCCC", new String(sf.getPayload(), StandardCharsets.ISO_8859_1));
            assertEquals("AAAA", metaData.getFileName());
            assertEquals("ZZZZ", metaData.getComment());
            assertEquals(OS.UNIX, metaData.getOS());
            assertEquals(parameters, metaData);
        }

        assertThrows(ZipException.class, () -> {
            result[30] = 0x77; // corrupt the low byte of header CRC
            try (GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(result))) {
                // if it does not fail, the hcrc is good.
            }
        }, "Header CRC verification is no longer feasible with JDK classes. The earlier assertion would have passed despite a bad header CRC.");
    }

    /**
     * Creates GzipParameters with header CRC enabled.
     */
    private GzipParameters createGzipParametersWithHeaderCrc() {
        final GzipParameters parameters = new GzipParameters();
        parameters.setHeaderCRC(true);
        parameters.setModificationTime(0x66554433); // avoid changing time
        parameters.setFileName("AAAA");
        parameters.setComment("ZZZZ");
        parameters.setOS(OS.UNIX);
        final ExtraField extra = new ExtraField();
        extra.addSubField("BB", "CCCC".getBytes(StandardCharsets.ISO_8859_1));
        parameters.setExtraField(extra);
        return parameters;
    }
}