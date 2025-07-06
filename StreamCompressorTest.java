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
        // Given
        final DataOutput dataOutputStream = new DataOutputStream(new ByteArrayOutputStream());

        // When
        try (StreamCompressor streamCompressor = StreamCompressor.create(dataOutputStream, new Deflater(9))) {

            // Then
            assertNotNull(streamCompressor, "StreamCompressor should be created successfully.");
        }
    }

    @Test
    void testDeflatedEntries() throws Exception {
        // Given
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final String inputData = "AAAAAABBBBBB";
        final byte[] inputBytes = inputData.getBytes();

        // When
        try (StreamCompressor streamCompressor = StreamCompressor.create(outputStream)) {
            streamCompressor.deflate(new ByteArrayInputStream(inputBytes), ZipEntry.DEFLATED);

            // Then
            assertEquals(inputBytes.length, streamCompressor.getBytesRead(), "Bytes read should match input length.");
            assertEquals(8, streamCompressor.getBytesWrittenForLastEntry(), "Bytes written for last entry should be 8."); //implementation detail
            assertEquals(3299542, streamCompressor.getCrc32(), "CRC32 checksum should match."); //implementation detail

            final byte[] actualBytes = outputStream.toByteArray();
            final byte[] expectedBytes = { 115, 116, 4, 1, 39, 48, 0, 0 }; //This is dependent on the Deflater
            assertArrayEquals(expectedBytes, actualBytes, "Deflated output should match expected byte array.");
        }
    }

    @Test
    void testStoredEntries() throws Exception {
        // Given
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // When
        try (StreamCompressor streamCompressor = StreamCompressor.create(outputStream)) {
            streamCompressor.deflate(new ByteArrayInputStream("A".getBytes()), ZipEntry.STORED);
            streamCompressor.deflate(new ByteArrayInputStream("BAD".getBytes()), ZipEntry.STORED);

            // Then
            assertEquals(3, streamCompressor.getBytesRead(), "Bytes read should be 3 after first two deflates.");
            assertEquals(3, streamCompressor.getBytesWrittenForLastEntry(), "Bytes written for last entry should be 3 after second deflate.");
            assertEquals(344750961, streamCompressor.getCrc32(), "CRC32 checksum should match after second deflate.");

            // When
            streamCompressor.deflate(new ByteArrayInputStream("CAFE".getBytes()), ZipEntry.STORED);

            // Then
            assertEquals("ABADCAFE", outputStream.toString(), "Output stream should contain concatenated stored data.");
        }
    }
}