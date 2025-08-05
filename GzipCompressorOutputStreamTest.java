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
 * Tests {@link GzipCompressorOutputStream} functionality including:
 * - File name handling with different character encodings
 * - Extra fields and subfields
 * - Header CRC validation
 */
class GzipCompressorOutputStreamTest {

    // Test data constants
    private static final String CHINESE_BASE_NAME = "\u6D4B\u8BD5\u4E2D\u6587\u540D\u79F0"; // "测试中文名称"
    private static final String CHINESE_FILE_NAME = CHINESE_BASE_NAME + ".xml";
    private static final String TEST_CONTENT = "<text>Hello World!</text>";
    private static final String SIMPLE_TEST_CONTENT = "Hello World!";

    /**
     * Tests Chinese file name handling with different character encodings.
     * This is a common test pattern for both GBK and UTF-8 encodings.
     */
    private void testChineseFileNameWithEncoding(String expectedFileName, String sourceFileName, Charset fileNameCharset) throws IOException {
        // Arrange: Create test files and content
        Path sourceFile = Files.createTempFile(sourceFileName, sourceFileName);
        byte[] testContent = TEST_CONTENT.getBytes(StandardCharsets.ISO_8859_1);
        Files.write(sourceFile, testContent);
        Path compressedFile = Files.createTempFile(CHINESE_BASE_NAME, ".gz");

        // Configure gzip parameters with specific charset
        GzipParameters gzipParams = new GzipParameters();
        gzipParams.setFileNameCharset(fileNameCharset);
        gzipParams.setFileName(CHINESE_FILE_NAME);
        gzipParams.setComment("Comment on " + CHINESE_FILE_NAME);

        // Act: Compress the file
        compressFile(sourceFile, compressedFile, gzipParams);

        // Assert: Verify file name encoding with legacy constructor
        verifyFileNameWithLegacyConstructor(compressedFile, expectedFileName, fileNameCharset, testContent);

        // Assert: Verify file name encoding with builder pattern
        verifyFileNameWithBuilder(compressedFile, expectedFileName, fileNameCharset, testContent, gzipParams);
    }

    private void compressFile(Path sourceFile, Path targetFile, GzipParameters parameters) throws IOException {
        try (OutputStream fileOutput = Files.newOutputStream(targetFile);
             GzipCompressorOutputStream gzipOutput = new GzipCompressorOutputStream(fileOutput, parameters)) {
            gzipOutput.write(sourceFile);
        }
    }

    private void verifyFileNameWithLegacyConstructor(Path compressedFile, String expectedFileName, 
                                                   Charset fileNameCharset, byte[] expectedContent) throws IOException {
        // Old construction doesn't allow configuration of reading the file name and comment Charset
        try (GzipCompressorInputStream gzipInput = new GzipCompressorInputStream(Files.newInputStream(compressedFile))) {
            byte[] fileNameBytes = gzipInput.getMetaData().getFileName().getBytes(StandardCharsets.ISO_8859_1);
            String decodedFileName = new String(fileNameBytes, fileNameCharset);
            
            assertEquals(expectedFileName, decodedFileName);
            assertArrayEquals(expectedContent, IOUtils.toByteArray(gzipInput));
        }
    }

    private void verifyFileNameWithBuilder(Path compressedFile, String expectedFileName, 
                                         Charset fileNameCharset, byte[] expectedContent, 
                                         GzipParameters originalParams) throws IOException {
        // Builder pattern allows configuration of reading the file name and comment Charset
        try (GzipCompressorInputStream gzipInput = GzipCompressorInputStream.builder()
                .setPath(compressedFile)
                .setFileNameCharset(fileNameCharset)
                .get()) {
            
            byte[] fileNameBytes = gzipInput.getMetaData().getFileName().getBytes(fileNameCharset);
            String decodedFileName = new String(fileNameBytes, fileNameCharset);
            
            assertEquals(expectedFileName, decodedFileName);
            assertArrayEquals(expectedContent, IOUtils.toByteArray(gzipInput));
            
            // Reset trailer values for parameter comparison
            gzipInput.getMetaData().setTrailerCrc(0);
            gzipInput.getMetaData().setTrailerISize(0);
            assertEquals(originalParams, gzipInput.getMetaData());
        }
    }

    /**
     * Tests Chinese file name handling for Windows systems using GBK encoding.
     * GBK charset support is assumed to be available.
     */
    @Test
    void testChineseFileNameWithGBKEncoding() throws IOException {
        assumeTrue(Charset.isSupported("GBK"), "GBK charset must be supported for this test");
        testChineseFileNameWithEncoding(CHINESE_FILE_NAME, CHINESE_FILE_NAME, Charset.forName("GBK"));
    }

    /**
     * Tests Chinese file name handling using UTF-8 encoding.
     * UTF-8 is always available in Java.
     */
    @Test
    void testChineseFileNameWithUTF8Encoding() throws IOException {
        testChineseFileNameWithEncoding(CHINESE_FILE_NAME, CHINESE_FILE_NAME, StandardCharsets.UTF_8);
    }

    /**
     * Tests gzip extra header fields containing subfields with various configurations.
     * 
     * @param subFieldCount Number of subfields to create
     * @param payloadSize Size of payload for each subfield (null means no payload)
     * @param shouldFail Whether the operation is expected to fail
     */
    @ParameterizedTest
    @CsvSource({
        "0,    42, false",  // No subfields, should succeed
        "1,      , true",   // One subfield with null payload, should fail
        "1,     0, false",  // One subfield with empty payload, should succeed
        "1, 65531, false",  // One subfield with large payload, should succeed
        "1, 65532, true",   // One subfield with too large payload, should fail
        "2,     0, false",  // Two subfields with empty payloads, should succeed
        "2, 32764, true",   // Two subfields with large payloads, should fail (total too large)
        "2, 32763, false"   // Two subfields with acceptable payloads, should succeed
    })
    void testGzipExtraSubfields(int subFieldCount, Integer payloadSize, boolean shouldFail) throws IOException {
        // Arrange: Create test files
        Path sourceFile = createTestFile("test_gzip_extra_", ".txt", SIMPLE_TEST_CONTENT);
        Path compressedFile = Files.createTempFile("test_gzip_extra_", ".txt.gz");

        // Arrange: Create gzip parameters with extra fields
        GzipParameters gzipParams = new GzipParameters();
        ExtraField extraField = new ExtraField();
        
        // Act: Try to add subfields and check if it fails as expected
        boolean actuallyFailed = tryAddingSubfields(extraField, subFieldCount, payloadSize);
        
        // Assert: Verify failure expectation
        assertEquals(shouldFail, actuallyFailed, 
            String.format("Expected subfield addition to %s but it %s", 
                shouldFail ? "fail" : "succeed", 
                actuallyFailed ? "failed" : "succeeded"));

        if (shouldFail) {
            return; // Test complete for failure cases
        }

        // Continue testing for success cases
        verifyExtraFieldIteratorImmutability(extraField, subFieldCount);
        
        // Act: Compress file with extra fields
        gzipParams.setExtraField(extraField);
        compressFileAndVerifyExtraFields(sourceFile, compressedFile, gzipParams, subFieldCount, payloadSize);
    }

    private Path createTestFile(String prefix, String suffix, String content) throws IOException {
        Path file = Files.createTempFile(prefix, suffix);
        Files.write(file, content.getBytes(StandardCharsets.ISO_8859_1));
        return file;
    }

    private boolean tryAddingSubfields(ExtraField extraField, int subFieldCount, Integer payloadSize) {
        byte[][] payloads = new byte[subFieldCount][];
        
        for (int i = 0; i < subFieldCount; i++) {
            if (payloadSize != null) {
                payloads[i] = ArrayFill.fill(new byte[payloadSize], (byte) ('a' + i));
            }
            
            try {
                extraField.addSubField("z" + i, payloads[i]);
            } catch (NullPointerException | IOException e) {
                return true; // Failed as expected
            }
        }
        return false; // Succeeded
    }

    private void verifyExtraFieldIteratorImmutability(ExtraField extraField, int subFieldCount) {
        if (subFieldCount > 0) {
            assertThrows(UnsupportedOperationException.class, 
                () -> extraField.iterator().remove(),
                "ExtraField iterator should not allow removal");
        }
    }

    private void compressFileAndVerifyExtraFields(Path sourceFile, Path compressedFile, 
                                                GzipParameters gzipParams, int subFieldCount, 
                                                Integer payloadSize) throws IOException {
        // Compress the file
        try (OutputStream fileOutput = Files.newOutputStream(compressedFile);
             GzipCompressorOutputStream gzipOutput = new GzipCompressorOutputStream(fileOutput, gzipParams)) {
            gzipOutput.write(sourceFile);
            gzipOutput.close();
            assertTrue(gzipOutput.isClosed(), "GzipCompressorOutputStream should be closed");
        }

        // Verify the compressed file and its extra fields
        try (GzipCompressorInputStream gzipInput = new GzipCompressorInputStream(Files.newInputStream(compressedFile))) {
            ExtraField readExtraField = gzipInput.getMetaData().getExtraField();
            
            assertEquals(gzipParams, gzipInput.getMetaData(), "Gzip parameters should match");
            assertEquals(subFieldCount == 0, readExtraField.isEmpty(), "ExtraField empty state should match expectation");
            assertEquals(subFieldCount, readExtraField.size(), "ExtraField size should match");
            
            int expectedEncodedSize = calculateExpectedEncodedSize(subFieldCount, payloadSize);
            assertEquals(expectedEncodedSize, readExtraField.getEncodedSize(), "Encoded size should match");
            
            verifySubFields(readExtraField, subFieldCount, payloadSize);
            
            // Test clearing extra fields
            readExtraField.clear();
            assertTrue(readExtraField.isEmpty(), "ExtraField should be empty after clear");
        }
    }

    private int calculateExpectedEncodedSize(int subFieldCount, Integer payloadSize) {
        if (subFieldCount == 0 || payloadSize == null) {
            return 0;
        }
        return 4 * subFieldCount + subFieldCount * payloadSize;
    }

    private void verifySubFields(ExtraField extraField, int subFieldCount, Integer payloadSize) {
        ArrayList<SubField> subFieldList = new ArrayList<>();
        extraField.forEach(subFieldList::add);
        assertEquals(subFieldCount, subFieldList.size(), "SubField list size should match");

        for (int i = 0; i < subFieldCount; i++) {
            SubField subField = extraField.getSubField(i);
            String expectedId = "z" + i;
            
            assertSame(subField, subFieldList.get(i), "SubField reference should be the same");
            assertSame(subField, extraField.findFirstSubField(expectedId), "Found subfield should be the same reference");
            assertEquals(expectedId, subField.getId(), "SubField ID should match");
            
            if (payloadSize != null) {
                byte[] expectedPayload = ArrayFill.fill(new byte[payloadSize], (byte) ('a' + i));
                assertArrayEquals(expectedPayload, subField.getPayload(), 
                    String.format("SubField %d payload should match", i));
            }
        }
    }

    /**
     * Tests empty extra fields behavior.
     */
    @Test
    void testEmptyExtraFields() {
        ExtraField emptyExtraField = new ExtraField();
        
        assertEquals(0, emptyExtraField.toByteArray().length, "Empty ExtraField should have zero byte array length");
        assertFalse(emptyExtraField.iterator().hasNext(), "Empty ExtraField iterator should have no elements");
        
        // Verify forEach doesn't execute on empty field
        emptyExtraField.forEach(subField -> fail("Empty ExtraField should not execute forEach"));
        
        // Verify accessing non-existent subfield throws exception
        assertThrows(IndexOutOfBoundsException.class, 
            () -> emptyExtraField.getSubField(0),
            "Accessing subfield in empty ExtraField should throw IndexOutOfBoundsException");
    }

    /**
     * Tests file name handling with different character sets.
     * This is a common test pattern for ASCII and Chinese file names.
     */
    private void testFileNameHandling(String expectedFileName, String sourceFileName) throws IOException {
        // Arrange: Create test file
        Path sourceFile = Files.createTempFile(sourceFileName, sourceFileName);
        byte[] testContent = TEST_CONTENT.getBytes(StandardCharsets.ISO_8859_1);
        Files.write(sourceFile, testContent);
        Path compressedFile = Files.createTempFile("test", ".gz");

        // Arrange: Configure gzip parameters
        GzipParameters gzipParams = new GzipParameters();
        gzipParams.setFilename(sourceFileName);
        
        // Verify filename getter/setter consistency
        assertEquals(gzipParams.getFilename(), gzipParams.getFileName(), "Filename getters should return same value");
        
        gzipParams.setFileName(sourceFileName);
        assertEquals(gzipParams.getFilename(), gzipParams.getFileName(), "Filename should be consistent after setting");

        // Act: Compress the file
        compressFile(sourceFile, compressedFile, gzipParams);

        // Assert: Verify file name in compressed file
        try (GzipCompressorInputStream gzipInput = new GzipCompressorInputStream(Files.newInputStream(compressedFile))) {
            assertEquals(expectedFileName, gzipInput.getMetaData().getFileName(), "File name should match expected");
            assertEquals(expectedFileName, gzipInput.getMetaData().getFilename(), "Filename should match expected");
            assertArrayEquals(testContent, IOUtils.toByteArray(gzipInput), "File content should match");
        }
    }

    /**
     * Tests ASCII file name handling.
     */
    @Test
    void testAsciiFileName() throws IOException {
        testFileNameHandling("ASCII.xml", "ASCII.xml");
    }

    /**
     * Tests Chinese file name handling with percent encoding.
     * 
     * This test demonstrates COMPRESS-638 behavior.
     * GZip RFC requires ISO 8859-1 (LATIN-1), so non-Latin characters get percent-encoded.
     * Use {@link GzipParameters#setFileNameCharset(Charset)} for proper non-ISO-8859-1 character support.
     */
    @Test
    void testChineseFileNameWithPercentEncoding() throws IOException {
        // Chinese characters get percent-encoded when using default ISO-8859-1
        testFileNameHandling("??????.xml", CHINESE_FILE_NAME);
    }

    /**
     * Tests gzip header CRC functionality.
     * Verifies that header CRC is correctly calculated and validated.
     */
    @Test
    void testGzipHeaderCRC() throws IOException, DecoderException {
        // Arrange: Create gzip parameters with header CRC enabled
        GzipParameters gzipParams = createParametersForHeaderCrcTest();
        
        // Act: Create gzip stream with header CRC
        byte[] compressedData = createGzipStreamWithHeaderCrc(gzipParams);
        
        // Assert: Verify the exact byte structure matches expected
        byte[] expectedBytes = getExpectedHeaderCrcBytes();
        assertArrayEquals(expectedBytes, compressedData, "Compressed data should match expected byte structure");
        
        // Assert: Verify standard GZIPInputStream can read it without errors
        assertDoesNotThrow(() -> {
            try (GZIPInputStream standardGzipInput = new GZIPInputStream(new ByteArrayInputStream(compressedData))) {
                // If no exception is thrown, the header CRC is valid
            }
        }, "Standard GZIPInputStream should be able to read the stream without errors");

        // Assert: Verify our implementation can read back the parameters correctly
        verifyHeaderCrcParameters(compressedData, gzipParams);
        
        // Assert: Verify header CRC validation catches corruption
        verifyHeaderCrcValidation(compressedData);
    }

    private GzipParameters createParametersForHeaderCrcTest() throws IOException {
        GzipParameters params = new GzipParameters();
        params.setHeaderCRC(true);
        params.setModificationTime(0x66554433); // Fixed time to avoid test variability
        params.setFileName("AAAA");
        params.setComment("ZZZZ");
        params.setOS(OS.UNIX);
        
        // Add extra field
        ExtraField extraField = new ExtraField();
        extraField.addSubField("BB", "CCCC".getBytes(StandardCharsets.ISO_8859_1));
        params.setExtraField(extraField);
        
        return params;
    }

    private byte[] createGzipStreamWithHeaderCrc(GzipParameters parameters) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (GzipCompressorOutputStream gzipOutput = new GzipCompressorOutputStream(outputStream, parameters)) {
            // Create empty gzip stream for this test
        }
        return outputStream.toByteArray();
    }

    private byte[] getExpectedHeaderCrcBytes() throws DecoderException {
        return Hex.decodeHex(
            "1f8b" +        // Magic number (id1 id2)
            "08" +          // Compression method
            "1e" +          // Flags (FEXTRA|FNAME|FCOMMENT|FHCRC)
            "33445566" +    // Modification time (little endian)
            "00" + "03" +   // Extra flags + OS
            "0800" +        // Extra field length
            "4242" +        // Subfield ID "BB"
            "0400" +        // Subfield length
            "43434343" +    // Subfield data "CCCC"
            "4141414100" +  // File name "AAAA" with null terminator
            "5a5a5a5a00" +  // Comment "ZZZZ" with null terminator
            "d842" +        // Header CRC32 = 839242d8
            "0300" +        // Empty deflate stream
            "00000000" +    // Data CRC32
            "00000000"      // Input size
        );
    }

    private void verifyHeaderCrcParameters(byte[] compressedData, GzipParameters originalParams) throws IOException {
        try (GzipCompressorInputStream gzipInput = new GzipCompressorInputStream(new ByteArrayInputStream(compressedData))) {
            GzipParameters readParams = gzipInput.getMetaData();
            
            assertTrue(readParams.getHeaderCRC(), "Header CRC flag should be set");
            assertEquals(0x66554433, readParams.getModificationTime(), "Modification time should match");
            assertEquals(1, readParams.getExtraField().size(), "Should have one extra field");
            
            SubField subField = readParams.getExtraField().iterator().next();
            assertEquals("BB", subField.getId(), "SubField ID should match");
            assertEquals("CCCC", new String(subField.getPayload(), StandardCharsets.ISO_8859_1), "SubField payload should match");
            
            assertEquals("AAAA", readParams.getFileName(), "File name should match");
            assertEquals("ZZZZ", readParams.getComment(), "Comment should match");
            assertEquals(OS.UNIX, readParams.getOS(), "OS should match");
            assertEquals(originalParams, readParams, "All parameters should match");
        }
    }

    private void verifyHeaderCrcValidation(byte[] compressedData) {
        // Test that corrupted header CRC is detected
        assertThrows(ZipException.class, () -> {
            byte[] corruptedData = compressedData.clone();
            corruptedData[30] = 0x77; // Corrupt the low byte of header CRC
            
            try (GZIPInputStream standardGzipInput = new GZIPInputStream(new ByteArrayInputStream(corruptedData))) {
                // Should throw ZipException due to bad header CRC
            }
        }, "Corrupted header CRC should be detected and cause ZipException");
    }
}