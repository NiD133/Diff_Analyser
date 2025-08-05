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

class StreamCompressorTest {

    // Constants for testCreateWithDataOutputAndDeflater
    private static final int DEFLATER_COMPRESSION_LEVEL = 9;

    @Test
    void testCreateWithDataOutputAndDeflater() throws IOException {
        final DataOutput dataOutputStream = new DataOutputStream(new ByteArrayOutputStream());
        try (StreamCompressor streamCompressor = StreamCompressor.create(dataOutputStream, new Deflater(DEFLATER_COMPRESSION_LEVEL))) {
            assertNotNull(streamCompressor, "StreamCompressor instance should be created successfully");
        }
    }

    // Constants for testDeflateWithDeflatedMethod
    private static final String INPUT_DEFLATED = "AAAAAABBBBBB";
    private static final long EXPECTED_DEFLATED_BYTES_READ = 12;
    private static final long EXPECTED_DEFLATED_BYTES_WRITTEN = 8;
    private static final long EXPECTED_DEFLATED_CRC = 3299542;
    private static final byte[] EXPECTED_DEFLATED_OUTPUT = {115, 116, 4, 1, 39, 48, 0, 0};

    @Test
    void testDeflateWithDeflatedMethod() throws Exception {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (StreamCompressor compressor = StreamCompressor.create(outputStream)) {
            // Compress test data using DEFLATED method
            compressor.deflate(new ByteArrayInputStream(INPUT_DEFLATED.getBytes()), ZipEntry.DEFLATED);

            // Validate compression metrics
            assertEquals(EXPECTED_DEFLATED_BYTES_READ, compressor.getBytesRead(), 
                "Bytes read should match input length");
            assertEquals(EXPECTED_DEFLATED_BYTES_WRITTEN, compressor.getBytesWrittenForLastEntry(),
                "Compressed bytes written should match expected value");
            assertEquals(EXPECTED_DEFLATED_CRC, compressor.getCrc32(),
                "CRC32 checksum should match expected value");

            // Validate compressed output
            final byte[] compressedData = outputStream.toByteArray();
            /* 
             * WARNING: This test relies on the compression behavior of the specific Deflater implementation.
             * If the compression algorithm changes, these expected values might need updating.
             */
            assertArrayEquals(EXPECTED_DEFLATED_OUTPUT, compressedData,
                "Compressed output should match expected byte pattern");
        }
    }

    // Constants for stored compression tests
    private static final String ENTRY_A = "A";
    private static final String ENTRY_BAD = "BAD";
    private static final String ENTRY_CAFE = "CAFE";
    private static final long EXPECTED_BYTES_READ_AFTER_TWO_ENTRIES = 4; // 1 (A) + 3 (BAD)
    private static final long EXPECTED_LAST_ENTRY_SIZE = 3; // BAD entry size
    private static final long EXPECTED_CRC_AFTER_TWO_ENTRIES = 344750961;
    private static final String EXPECTED_FULL_OUTPUT = "ABADCAFE";

    @Test
    void testStoredCompressionMethod() throws Exception {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (StreamCompressor compressor = StreamCompressor.create(outputStream)) {
            
            // First entry: Single byte
            compressor.deflate(new ByteArrayInputStream(ENTRY_A.getBytes()), ZipEntry.STORED);
            
            // Second entry: Three bytes
            compressor.deflate(new ByteArrayInputStream(ENTRY_BAD.getBytes()), ZipEntry.STORED);
            
            // Validate cumulative state after two entries
            assertEquals(EXPECTED_BYTES_READ_AFTER_TWO_ENTRIES, compressor.getBytesRead(),
                "Total bytes read should include all entries");
            assertEquals(EXPECTED_LAST_ENTRY_SIZE, compressor.getBytesWrittenForLastEntry(),
                "Last entry size should match BAD input length");
            assertEquals(EXPECTED_CRC_AFTER_TWO_ENTRIES, compressor.getCrc32(),
                "Cumulative CRC should include all entries");
            
            // Third entry: Four bytes
            compressor.deflate(new ByteArrayInputStream(ENTRY_CAFE.getBytes()), ZipEntry.STORED);
            
            // Validate final output
            assertEquals(EXPECTED_FULL_OUTPUT, outputStream.toString(),
                "Combined output should match concatenated stored entries");
        }
    }
}