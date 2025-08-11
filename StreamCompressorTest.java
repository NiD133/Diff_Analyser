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
package org.apache.commons.compress.archivers.zip;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;

import org.junit.jupiter.api.Test;

/**
 * Test suite for StreamCompressor functionality including creation, 
 * deflated compression, and stored (uncompressed) entries.
 */
class StreamCompressorTest {

    // Test data constants
    private static final String REPEATED_CHARS_DATA = "AAAAAABBBBBB";
    private static final String SINGLE_CHAR_DATA = "A";
    private static final String MULTI_CHAR_DATA_1 = "BAD";
    private static final String MULTI_CHAR_DATA_2 = "CAFE";
    
    // Expected values for deflated compression test
    private static final int EXPECTED_BYTES_READ_DEFLATED = 12;
    private static final int EXPECTED_BYTES_WRITTEN_DEFLATED = 8;
    private static final long EXPECTED_CRC32_DEFLATED = 3299542L;
    private static final byte[] EXPECTED_DEFLATED_OUTPUT = { 115, 116, 4, 1, 39, 48, 0, 0 };
    
    // Expected values for stored entries test
    private static final int EXPECTED_BYTES_READ_STORED = 3; // "A" + "BAD" = 4 chars, but only counting last entry
    private static final int EXPECTED_BYTES_WRITTEN_STORED = 3; // "BAD" length
    private static final long EXPECTED_CRC32_STORED = 344750961L;
    private static final String EXPECTED_STORED_OUTPUT = "ABADCAFE";

    @Test
    void shouldCreateStreamCompressorWithDataOutput() throws IOException {
        // Given: A DataOutput stream and a high compression level deflater
        final DataOutput dataOutputStream = new DataOutputStream(new ByteArrayOutputStream());
        final Deflater highCompressionDeflater = new Deflater(9);
        
        // When: Creating a StreamCompressor with DataOutput
        try (StreamCompressor streamCompressor = StreamCompressor.create(dataOutputStream, highCompressionDeflater)) {
            
            // Then: StreamCompressor should be successfully created
            assertNotNull(streamCompressor, "StreamCompressor should be created successfully");
        }
    }

    @Test
    void shouldCompressDataUsingDeflatedMethod() throws Exception {
        // Given: An output stream and test data with repeated characters
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ByteArrayInputStream inputData = new ByteArrayInputStream(REPEATED_CHARS_DATA.getBytes());
        
        // When: Compressing data using DEFLATED method
        try (StreamCompressor compressor = StreamCompressor.create(outputStream)) {
            compressor.deflate(inputData, ZipEntry.DEFLATED);
            
            // Then: Verify compression statistics
            assertEquals(EXPECTED_BYTES_READ_DEFLATED, compressor.getBytesRead(), 
                "Should read all input bytes");
            assertEquals(EXPECTED_BYTES_WRITTEN_DEFLATED, compressor.getBytesWrittenForLastEntry(), 
                "Should write compressed bytes (less than input due to compression)");
            assertEquals(EXPECTED_CRC32_DEFLATED, compressor.getCrc32(), 
                "Should calculate correct CRC32 checksum");

            // And: Verify the actual compressed output matches expected deflated data
            final byte[] actualCompressedOutput = outputStream.toByteArray();
            assertArrayEquals(EXPECTED_DEFLATED_OUTPUT, actualCompressedOutput, 
                "Compressed output should match expected deflated bytes");
        }
    }

    @Test
    void shouldStoreMultipleEntriesWithoutCompression() throws Exception {
        // Given: An output stream for storing uncompressed data
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        try (StreamCompressor compressor = StreamCompressor.create(outputStream)) {
            
            // When: Adding multiple entries using STORED method (no compression)
            compressor.deflate(createInputStream(SINGLE_CHAR_DATA), ZipEntry.STORED);
            compressor.deflate(createInputStream(MULTI_CHAR_DATA_1), ZipEntry.STORED);
            
            // Then: Verify statistics after second entry
            assertEquals(EXPECTED_BYTES_READ_STORED, compressor.getBytesRead(), 
                "Should track bytes read from last entry only");
            assertEquals(EXPECTED_BYTES_WRITTEN_STORED, compressor.getBytesWrittenForLastEntry(), 
                "Should write same number of bytes as input (no compression)");
            assertEquals(EXPECTED_CRC32_STORED, compressor.getCrc32(), 
                "Should calculate correct CRC32 for last entry");
            
            // When: Adding a third entry
            compressor.deflate(createInputStream(MULTI_CHAR_DATA_2), ZipEntry.STORED);
            
            // Then: All data should be concatenated in output stream without compression
            assertEquals(EXPECTED_STORED_OUTPUT, outputStream.toString(), 
                "All entries should be concatenated without compression");
        }
    }
    
    /**
     * Helper method to create ByteArrayInputStream from string data.
     * Improves readability by reducing repetitive code.
     */
    private ByteArrayInputStream createInputStream(String data) {
        return new ByteArrayInputStream(data.getBytes());
    }
}