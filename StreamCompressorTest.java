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
import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StreamCompressorTest {

    /**
     * The expected compressed output for "AAAAAABBBBBB" using Deflater.
     * This is dependent on the specific implementation of java.util.zip.Deflater and may be brittle.
     */
    private static final byte[] EXPECTED_DEFLATED_OUTPUT = { 115, 116, 4, 1, 39, 48, 0, 0 };

    @Test
    @DisplayName("create() with a DataOutput should return a non-null StreamCompressor instance")
    void createWithDataOutputReturnsNonNullInstance() throws IOException {
        // Arrange
        final DataOutput dataOutput = new DataOutputStream(new ByteArrayOutputStream());
        final Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION);

        // Act & Assert
        try (StreamCompressor streamCompressor = StreamCompressor.create(dataOutput, deflater)) {
            assertNotNull(streamCompressor, "StreamCompressor should not be null");
        }
    }

    @Test
    @DisplayName("deflate() with DEFLATED method should write compressed data and report correct stats")
    void deflateWithDeflatedMethodWritesCompressedDataAndReportsCorrectStats() throws IOException {
        // Arrange
        final byte[] inputData = "AAAAAABBBBBB".getBytes(StandardCharsets.UTF_8);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final long expectedCrc = calculateCrc32(inputData);

        // Act
        try (StreamCompressor compressor = StreamCompressor.create(outputStream)) {
            compressor.deflate(new ByteArrayInputStream(inputData), ZipEntry.DEFLATED);

            // Assert
            assertEquals(inputData.length, compressor.getBytesRead(), "Bytes read should match input length");
            assertEquals(EXPECTED_DEFLATED_OUTPUT.length, compressor.getBytesWrittenForLastEntry(), "Bytes written should match compressed length");
            assertEquals(expectedCrc, compressor.getCrc32(), "CRC32 should match the input data");

            final byte[] actualOutput = outputStream.toByteArray();
            assertArrayEquals(EXPECTED_DEFLATED_OUTPUT, actualOutput, "Compressed output should match the expected byte array");
        }
    }

    @Test
    @DisplayName("deflate() with STORED method should append uncompressed data and track stats for the last entry")
    void deflateWithStoredMethodAppendsDataAndTracksStatsForLastEntry() throws IOException {
        // Arrange
        final byte[] data1 = "A".getBytes(StandardCharsets.UTF_8);
        final byte[] data2 = "BAD".getBytes(StandardCharsets.UTF_8);
        final byte[] data3 = "CAFE".getBytes(StandardCharsets.UTF_8);
        final long expectedCrcForData2 = calculateCrc32(data2);

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (StreamCompressor compressor = StreamCompressor.create(outputStream)) {
            // Act 1: Compress first entry
            compressor.deflate(new ByteArrayInputStream(data1), ZipEntry.STORED);

            // Act 2: Compress second entry
            compressor.deflate(new ByteArrayInputStream(data2), ZipEntry.STORED);

            // Assert 2: Stats should reflect the *last* entry ("BAD")
            assertEquals(data2.length, compressor.getBytesRead(), "Bytes read should be for the last entry");
            assertEquals(data2.length, compressor.getBytesWrittenForLastEntry(), "Bytes written should be for the last entry");
            assertEquals(expectedCrcForData2, compressor.getCrc32(), "CRC32 should be for the last entry");

            // Act 3: Compress third entry
            compressor.deflate(new ByteArrayInputStream(data3), ZipEntry.STORED);

            // Assert 3: The output stream should contain all concatenated data
            final String expectedTotalOutput = "ABADCAFE";
            assertEquals(expectedTotalOutput, outputStream.toString(StandardCharsets.UTF_8.name()),
                "Output stream should contain all appended data");
        }
    }

    /**
     * Helper method to calculate the CRC32 checksum for a given byte array.
     * @param data The input byte array.
     * @return The CRC32 checksum.
     */
    private long calculateCrc32(final byte[] data) {
        final CRC32 crc = new CRC32();
        crc.update(data);
        return crc.getValue();
    }
}