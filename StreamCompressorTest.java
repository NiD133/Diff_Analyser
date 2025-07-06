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

    @Test
    void testCreateDataOutputCompressor() throws IOException {
        // Test the creation of a StreamCompressor with a DataOutputStream and a high compression level
        final DataOutput dataOutputStream = new DataOutputStream(new ByteArrayOutputStream());
        try (StreamCompressor streamCompressor = StreamCompressor.create(dataOutputStream, new Deflater(9))) {
            assertNotNull(streamCompressor, "StreamCompressor should be successfully created");
        }
    }

    @Test
    void testDeflatedEntries() throws Exception {
        // Test deflating a string and verify the compressed output and CRC32 checksum
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (StreamCompressor streamCompressor = StreamCompressor.create(outputStream)) {
            String inputString = "AAAAAABBBBBB";
            streamCompressor.deflate(new ByteArrayInputStream(inputString.getBytes()), ZipEntry.DEFLATED);

            // Verify the number of bytes read and written
            assertEquals(inputString.length(), streamCompressor.getBytesRead(), "Bytes read should match input length");
            assertEquals(8, streamCompressor.getBytesWrittenForLastEntry(), "Bytes written should match expected compressed size");
            assertEquals(3299542, streamCompressor.getCrc32(), "CRC32 checksum should match expected value");

            // Verify the compressed output
            final byte[] actualCompressedData = outputStream.toByteArray();
            final byte[] expectedCompressedData = { 115, 116, 4, 1, 39, 48, 0, 0 };
            assertArrayEquals(expectedCompressedData, actualCompressedData, "Compressed data should match expected output");
        }
    }

    @Test
    void testStoredEntries() throws Exception {
        // Test storing entries without compression and verify the output and CRC32 checksum
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (StreamCompressor streamCompressor = StreamCompressor.create(outputStream)) {
            streamCompressor.deflate(new ByteArrayInputStream("A".getBytes()), ZipEntry.STORED);
            streamCompressor.deflate(new ByteArrayInputStream("BAD".getBytes()), ZipEntry.STORED);

            // Verify the number of bytes read and written
            assertEquals(3, streamCompressor.getBytesRead(), "Bytes read should match total input length");
            assertEquals(3, streamCompressor.getBytesWrittenForLastEntry(), "Bytes written should match last entry size");
            assertEquals(344750961, streamCompressor.getCrc32(), "CRC32 checksum should match expected value");

            // Verify the stored output
            streamCompressor.deflate(new ByteArrayInputStream("CAFE".getBytes()), ZipEntry.STORED);
            assertEquals("ABADCAFE", outputStream.toString(), "Stored output should match expected concatenated string");
        }
    }
}