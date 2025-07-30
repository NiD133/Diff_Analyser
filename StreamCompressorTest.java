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

    /**
     * Tests the creation of a StreamCompressor with a DataOutput stream and a custom Deflater.
     */
    @Test
    void testCreateDataOutputCompressor() throws IOException {
        // Create a DataOutputStream to pass to the StreamCompressor
        final DataOutput dataOutputStream = new DataOutputStream(new ByteArrayOutputStream());
        
        // Create a StreamCompressor with maximum compression level
        try (StreamCompressor streamCompressor = StreamCompressor.create(dataOutputStream, new Deflater(9))) {
            // Verify that the StreamCompressor is not null
            assertNotNull(streamCompressor);
        }
    }

    /**
     * Tests the deflation of entries using the DEFLATED method.
     */
    @Test
    void testDeflatedEntries() throws Exception {
        // Create a ByteArrayOutputStream to capture the compressed output
        final ByteArrayOutputStream compressedOutput = new ByteArrayOutputStream();
        
        // Create a StreamCompressor with default compression level
        try (StreamCompressor streamCompressor = StreamCompressor.create(compressedOutput)) {
            // Deflate the input data using the DEFLATED method
            streamCompressor.deflate(new ByteArrayInputStream("AAAAAABBBBBB".getBytes()), ZipEntry.DEFLATED);
            
            // Verify the number of bytes read and written
            assertEquals(12, streamCompressor.getBytesRead());
            assertEquals(8, streamCompressor.getBytesWrittenForLastEntry());
            
            // Verify the CRC32 checksum
            assertEquals(3299542, streamCompressor.getCrc32());

            // Verify the compressed output bytes
            final byte[] actualCompressedBytes = compressedOutput.toByteArray();
            final byte[] expectedCompressedBytes = { 115, 116, 4, 1, 39, 48, 0, 0 };
            assertArrayEquals(expectedCompressedBytes, actualCompressedBytes);
        }
    }

    /**
     * Tests the deflation of entries using the STORED method.
     */
    @Test
    void testStoredEntries() throws Exception {
        // Create a ByteArrayOutputStream to capture the compressed output
        final ByteArrayOutputStream compressedOutput = new ByteArrayOutputStream();
        
        // Create a StreamCompressor with default compression level
        try (StreamCompressor streamCompressor = StreamCompressor.create(compressedOutput)) {
            // Deflate the input data using the STORED method
            streamCompressor.deflate(new ByteArrayInputStream("A".getBytes()), ZipEntry.STORED);
            streamCompressor.deflate(new ByteArrayInputStream("BAD".getBytes()), ZipEntry.STORED);
            
            // Verify the number of bytes read and written
            assertEquals(3, streamCompressor.getBytesRead());
            assertEquals(3, streamCompressor.getBytesWrittenForLastEntry());
            
            // Verify the CRC32 checksum
            assertEquals(344750961, streamCompressor.getCrc32());
            
            // Deflate additional input data using the STORED method
            streamCompressor.deflate(new ByteArrayInputStream("CAFE".getBytes()), ZipEntry.STORED);
            
            // Verify the final output string
            assertEquals("ABADCAFE", compressedOutput.toString());
        }
    }
}