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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import shaded.org.apache.commons.io.IOUtils;

/**
 * Tests for {@link GzipCompressorOutputStream}.
 * <p>
 * Verifies functionality including:
 * <ul>
 *   <li>Handling of file names with different character sets</li>
 *   <li>Processing of extra fields and subfields in gzip headers</li>
 *   <li>Header CRC validation</li>
 *   <li>Basic compression functionality</li>
 * </ul>
 * </p>
 */
class GzipCompressorOutputStreamTest {

    private static final String EXPECTED_BASE_NAME = "\u6D4B\u8BD5\u4E2D\u6587\u540D\u79F0";
    private static final String EXPECTED_FILE_NAME = EXPECTED_BASE_NAME + ".xml";
    private static final String SAMPLE_TEXT = "<text>Hello World!</text>";
    private static final Charset SAMPLE_TEXT_CHARSET = StandardCharsets.ISO_8859_1;
    private static final byte[] SAMPLE_BYTES = SAMPLE_TEXT.getBytes(SAMPLE_TEXT_CHARSET);

    /**
     * Tests Chinese file name handling with specified charset.
     * 
     * @param expectedFileName Expected file name after compression
     * @param sourceFileName   Original source file name
     * @param fileNameCharset  Charset used for file name encoding
     */
    private void testChineseFileName(final String expectedFileName, final String sourceFileName, 
                                    final Charset fileNameCharset) throws IOException {
        // Setup: Create temporary source file
        final Path tempSourceFile = Files.createTempFile(sourceFileName, sourceFileName);
        Files.write(tempSourceFile, SAMPLE_BYTES);
        
        // Setup: Create target compressed file
        final Path targetFile = Files.createTempFile(EXPECTED_BASE_NAME, ".gz");
        
        // Configure gzip parameters with specified charset
        final GzipParameters parameters = new GzipParameters();
        parameters.setFileNameCharset(fileNameCharset);
        parameters.setFileName(EXPECTED_FILE_NAME);
        parameters.setComment("Comment on " + EXPECTED_FILE_NAME);

        // Compress the file
        try (OutputStream fos = Files.newOutputStream(targetFile);
                GzipCompressorOutputStream gos = new GzipCompressorOutputStream(fos, parameters)) {
            gos.write(tempSourceFile);
        }

        // Test 1: Verify using default reader (without charset configuration)
        try (GzipCompressorInputStream gis = new GzipCompressorInputStream(Files.newInputStream(targetFile))) {
            final byte[] fileNameBytes = gis.getMetaData().getFileName().getBytes(SAMPLE_TEXT_CHARSET);
            final String decodedFileName = new String(fileNameBytes, fileNameCharset);
            assertEquals(expectedFileName, decodedFileName);
            assertArrayEquals(SAMPLE_BYTES, IOUtils.toByteArray(gis));
        }

        // Test 2: Verify using builder with explicit charset configuration
        try (GzipCompressorInputStream gis = GzipCompressorInputStream.builder()
                .setPath(targetFile)
                .setFileNameCharset(fileNameCharset)
                .get()) {
            final byte[] fileNameBytes = gis.getMetaData().getFileName().getBytes(fileNameCharset);
            final String decodedFileName = new String(fileNameBytes, fileNameCharset);
            assertEquals(expectedFileName, decodedFileName);
            assertArrayEquals(SAMPLE_BYTES, IOUtils.toByteArray(gis));
            
            // Prepare parameters for equality check
            gis.getMetaData().setTrailerCrc(0);
            gis.getMetaData().setTrailerISize(0);
            assertEquals(parameters, gis.getMetaData());
        }
    }

    /**
     * Tests Chinese file name handling with GBK charset (typical Windows configuration).
     */
    @Test
    void testChineseFileNameGBK() throws IOException {
        assumeTrue(Charset.isSupported("GBK"), "GBK charset not supported");
        testChineseFileName(EXPECTED_FILE_NAME, EXPECTED_FILE_NAME, Charset.forName("GBK"));
    }

    /**
     * Tests Chinese file name handling with UTF-8 charset (typical Linux configuration).
     */
    @Test
    void testChineseFileNameUTF8() throws IOException {
        testChineseFileName(EXPECTED_FILE_NAME, EXPECTED_FILE_NAME, StandardCharsets.UTF_8);
    }

    /**
     * Parameterized test for extra field subfield handling.
     * 
     * @param subFieldCount Number of subfields to test
     * @param payloadSize   Size of payload for each subfield (null indicates test failure scenario)
     * @param shouldFail    Whether the test case is expected to fail
     */
    @ParameterizedTest(name = "subfields={0}, payloadSize={1}, shouldFail={2}")
    @CsvSource({
        "0,    42, false",   // No subfields
        "1,      , true",    // Null payload (invalid)
        "1,     0, false",   // Empty payload
        "1, 65531, false",   // Valid payload (under limit)
        "1, 65532, true",    // Payload too large (65532 > max 65531)
        "2,     0, false",   // Multiple empty payloads
        "2, 32764, true",    // Combined size exceeds limit (2*(4+32764)=65536 > 65535
        "2, 32763, false"    // Combined size within limit (2*(4+32763)=65534
    })
    void testExtraSubfields(final int subFieldCount, final Integer payloadSize, final boolean shouldFail)
            throws IOException {
        // Setup: Create temporary source file
        final Path tempSourceFile = Files.createTempFile("test_gzip_extra_", ".txt");
        Files.write(tempSourceFile, "Hello World!".getBytes(SAMPLE_TEXT_CHARSET));
        final Path targetFile = Files.createTempFile("test_gzip_extra_", ".txt.gz");
        
        // Configure extra fields
        final GzipParameters parameters = new GzipParameters();
        final ExtraField extraField = new ExtraField();
        boolean failedDuringSetup = false;
        final byte[][] payloads = new byte[subFieldCount][];
        
        // Create subfields with specified payload sizes
        for (int i = 0; i < subFieldCount; i++) {
            if (payloadSize != null) {
                payloads[i] = ArrayFill.fill(new byte[payloadSize], (byte) ('a' + i));
            }
            try {
                extraField.addSubField("z" + i, payloads[i]);
            } catch (final NullPointerException | IOException e) {
                failedDuringSetup = true;
                break;
            }
        }
        
        // Verify setup failure matches expectation
        assertEquals(shouldFail, failedDuringSetup, "Subfield addition should fail: " + shouldFail);
        if (shouldFail) {
            return; // Skip compression for invalid cases
        }
        
        // Verify iterator immutability
        if (subFieldCount > 0) {
            assertThrows(UnsupportedOperationException.class, () -> extraField.iterator().remove());
        }
        
        // Compress with extra fields
        parameters.setExtraField(extraField);
        try (OutputStream fos = Files.newOutputStream(targetFile);
                GzipCompressorOutputStream gos = new GzipCompressorOutputStream(fos, parameters)) {
            gos.write(tempSourceFile);
            assertTrue(gos.isClosed());
        }
        
        // Verify compressed data and metadata
        try (GzipCompressorInputStream gis = new GzipCompressorInputStream(Files.newInputStream(targetFile))) {
            final ExtraField decompressedExtra = gis.getMetaData().getExtraField();
            
            // Validate basic properties
            assertEquals(subFieldCount == 0, decompressedExtra.isEmpty());
            assertEquals(subFieldCount, decompressedExtra.size());
            
            // Calculate expected encoded size (4 bytes per subfield header + payload)
            final int expectedEncodedSize = 4 * subFieldCount + (payloadSize != null ? payloadSize * subFieldCount : 0);
            assertEquals(expectedEncodedSize, decompressedExtra.getEncodedSize());
            
            // Verify subfield contents
            final ArrayList<SubField> subFields = new ArrayList<>();
            decompressedExtra.forEach(subFields::add);
            assertEquals(subFieldCount, subFields.size());
            
            for (int i = 0; i < subFieldCount; i++) {
                final SubField subField = decompressedExtra.getSubField(i);
                assertSame(subField, subFields.get(i));
                assertSame(subField, decompressedExtra.findFirstSubField("z" + i));
                assertEquals("z" + i, subField.getId());
                assertArrayEquals(payloads[i], subField.getPayload(), "Payload mismatch for subfield " + i);
            }
            
            // Test clear operation
            decompressedExtra.clear();
            assertTrue(decompressedExtra.isEmpty());
        }
    }

    /** Tests behavior with empty extra fields. */
    @Test
    void testExtraSubfieldsEmpty() {
        final ExtraField extra = new ExtraField();
        
        // Validate empty field properties
        assertEquals(0, extra.toByteArray().length);
        assertFalse(extra.iterator().hasNext());
        extra.forEach(e -> fail("Extra field should be empty"));
        assertThrows(IndexOutOfBoundsException.class, () -> extra.getSubField(0));
    }

    /**
     * Tests file name handling during compression.
     * 
     * @param expectedFileName Expected file name after decompression
     * @param sourceFileName   Original file name to compress
     */
    private void testFileName(final String expectedFileName, final String sourceFileName) throws IOException {
        // Setup: Create temporary source file
        final Path tempSourceFile = Files.createTempFile(sourceFileName, sourceFileName);
        Files.write(tempSourceFile, SAMPLE_BYTES);
        
        // Setup: Create target compressed file
        final Path targetFile = Files.createTempFile("test", ".gz");
        
        // Configure gzip parameters
        final GzipParameters parameters = new GzipParameters();
        parameters.setFileName(sourceFileName);
        
        // Compress the file
        try (OutputStream fos = Files.newOutputStream(targetFile);
                GzipCompressorOutputStream gos = new GzipCompressorOutputStream(fos, parameters)) {
            gos.write(tempSourceFile);
        }
        
        // Verify decompressed file name and content
        try (GzipCompressorInputStream gis = new GzipCompressorInputStream(Files.newInputStream(targetFile))) {
            assertEquals(expectedFileName, gis.getMetaData().getFileName());
            assertArrayEquals(SAMPLE_BYTES, IOUtils.toByteArray(gis));
        }
    }

    /** Tests ASCII file name handling. */
    @Test
    void testFileNameAscii() throws IOException {
        testFileName("ASCII.xml", "ASCII.xml");
    }

    /**
     * Tests Chinese file name without charset configuration (fallback to ISO-8859-1).
     * Verifies COMPRESS-638 - non-ISO-8859-1 characters get replaced.
     */
    @Test
    void testFileNameChinesePercentEncoded() throws IOException {
        testFileName("??????.xml", EXPECTED_FILE_NAME);
    }

    /** Tests header CRC generation and validation. */
    @Test
    void testHeaderCrc() throws IOException, DecoderException {
        // Configure gzip parameters with header CRC
        final GzipParameters parameters = new GzipParameters();
        parameters.setHeaderCRC(true);
        parameters.setModificationTime(0x66554433); // Fixed timestamp for test stability
        parameters.setFileName("AAAA");
        parameters.setComment("ZZZZ");
        parameters.setOS(OS.UNIX);
        
        // Add extra field
        final ExtraField extra = new ExtraField();
        extra.addSubField("BB", "CCCC".getBytes(SAMPLE_TEXT_CHARSET));
        parameters.setExtraField(extra);
        
        // Compress empty content
        final ByteArrayOutputStream compressedStream = new ByteArrayOutputStream();
        try (GzipCompressorOutputStream gos = new GzipCompressorOutputStream(compressedStream, parameters)) {
            // Intentionally empty
        }
        final byte[] compressedData = compressedStream.toByteArray();
        
        // Expected hex representation of gzip header
        final byte[] expectedHeader = Hex.decodeHex(
            "1f8b" +       // GZIP magic number
            "08" +         // Compression method (DEFLATE)
            "1e" +         // Flags (FEXTRA|FNAME|FCOMMENT|FHCRC)
            "33445566" +   // Modification time (little-endian)
            "00" +         // Extra flags
            "03" +         // OS (UNIX)
            "0800" +       // Extra field length (8 bytes)
            "4242" +       // Subfield ID "BB"
            "0400" +       // Subfield length (4 bytes)
            "43434343" +   // Subfield payload "CCCC"
            "4141414100" + // File name "AAAA" + null terminator
            "5a5a5a5a00" + // Comment "ZZZZ" + null terminator
            "d842" +       // Header CRC (little-endian 0x42d8)
            "0300" +       // Empty deflate block
            "00000000" +   // CRC32 of uncompressed data (0 for empty)
            "00000000"    // ISIZE (input size mod 2^32, 0 for empty)
        );
        assertArrayEquals(expectedHeader, compressedData, "Header mismatch");
        
        // Verify JDK GZIPInputStream accepts the header CRC
        assertDoesNotThrow(() -> {
            try (GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(compressedData))) {
                gis.read();
            }
        }, "JDK GZIPInputStream should accept valid header CRC");
        
        // Verify our implementation reads the metadata correctly
        try (GzipCompressorInputStream gis = new GzipCompressorInputStream(new ByteArrayInputStream(compressedData))) {
            final GzipParameters metaData = gis.getMetaData();
            
            // Validate metadata fields
            assertTrue(metaData.getHeaderCRC());
            assertEquals(0x66554433, metaData.getModificationTime());
            assertEquals("AAAA", metaData.getFileName());
            assertEquals("ZZZZ", metaData.getComment());
            assertEquals(OS.UNIX, metaData.getOS());
            
            // Validate extra field
            assertEquals(1, metaData.getExtraField().size());
            final SubField sf = metaData.getExtraField().iterator().next();
            assertEquals("BB", sf.getId());
            assertArrayEquals("CCCC".getBytes(SAMPLE_TEXT_CHARSET), sf.getPayload());
            assertEquals(parameters, metaData);
        }
        
        // Corrupt header CRC and verify detection
        final byte[] corruptedData = compressedData.clone();
        corruptedData[30] = 0x77; // Corrupt CRC byte
        assertThrows(ZipException.class, () -> {
            try (GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(corruptedData))) {
                gis.read();
            }
        }, "Should detect corrupted header CRC");
    }
}