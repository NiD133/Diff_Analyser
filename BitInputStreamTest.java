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
package org.apache.commons.compress.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

import org.junit.jupiter.api.Test;

class BitInputStreamTest {

    /**
     * Creates a ByteArrayInputStream with a pre-defined byte array for testing.
     * The byte array represents the bit sequence 11111000 01000000 00000001 00101111.
     *
     * @return ByteArrayInputStream containing the test data.
     */
    private ByteArrayInputStream createTestInputStream() {
        return new ByteArrayInputStream(new byte[] { (byte) 0xF8, 0x40, 0x01, 0x2F });
    }

    @Test
    void testAlignWithByteBoundaryWhenAlreadyAligned() throws Exception {
        // Arrange
        try (BitInputStream bis = new BitInputStream(createTestInputStream(), ByteOrder.LITTLE_ENDIAN)) {
            // Read a full byte, placing the stream at a byte boundary.
            assertEquals(0xF8, bis.readBits(8), "Should read the first byte correctly");

            // Act
            bis.alignWithByteBoundary();

            // Assert
            // After aligning, reading 4 bits should return 0 (as we're at the byte boundary)
            assertEquals(0, bis.readBits(4), "Should return 0 after aligning to byte boundary");
        }
    }

    @Test
    void testAlignWithByteBoundaryWhenNotAligned() throws Exception {
        // Arrange
        try (BitInputStream bis = new BitInputStream(createTestInputStream(), ByteOrder.LITTLE_ENDIAN)) {
            // Read 4 bits, leaving the stream unaligned.
            assertEquals(0x08, bis.readBits(4), "Should read the first 4 bits correctly");
            assertEquals(4, bis.bitsCached(), "Should have 4 bits cached");

            // Act
            bis.alignWithByteBoundary();

            // Assert
            assertEquals(0, bis.bitsCached(), "Cache should be cleared after aligning");
            // After aligning, reading 4 bits should return 0 (the remaining bits of the current byte).
            assertEquals(0, bis.readBits(4), "Should return 0 after aligning to byte boundary");
        }
    }

    @Test
    void testBitsAvailableWithCachedBits() throws Exception {
        // Arrange
        try (BitInputStream bis = new BitInputStream(createTestInputStream(), ByteOrder.LITTLE_ENDIAN)) {
            // Read 4 bits, caching them.
            assertEquals(0x08, bis.readBits(4), "Should read the first 4 bits correctly");

            // Act & Assert
            // The number of available bits should be the initial size minus the bits already read.
            assertEquals(28, bis.bitsAvailable(), "Should have 28 bits available");
        }
    }

    @Test
    void testBitsAvailableWithoutCachedBits() throws Exception {
        // Arrange
        try (BitInputStream bis = new BitInputStream(createTestInputStream(), ByteOrder.LITTLE_ENDIAN)) {

            // Act & Assert
            // The number of available bits should be the total number of bits in the stream.
            assertEquals(32, bis.bitsAvailable(), "Should have 32 bits available");
        }
    }

    @Test
    void testBigEndianReadWithOverflow() throws Exception {
        // Arrange
        byte[] inputBytes = { 87, 45, 66, 15, 90, 29, 88, 61, 33, 74 };
        ByteArrayInputStream in = new ByteArrayInputStream(inputBytes);

        try (BitInputStream bin = new BitInputStream(in, ByteOrder.BIG_ENDIAN)) {
            // Act & Assert
            assertEquals(10, bin.readBits(5), "Should read the first 5 bits correctly");
            assertEquals(8274274654740644818L, bin.readBits(63), "Should read the next 63 bits correctly");
            assertEquals(330, bin.readBits(12), "Should read the next 12 bits correctly");
            assertEquals(-1, bin.readBits(1), "Should return -1 when reaching the end of the stream");
        }
    }

    @Test
    void testClearBitCacheResetsCachedBits() throws IOException {
        // Arrange
        try (BitInputStream bis = new BitInputStream(createTestInputStream(), ByteOrder.LITTLE_ENDIAN)) {
            // Read some bits to populate the cache.
            assertEquals(0x08, bis.readBits(4), "Should read the first 4 bits correctly");

            // Act
            bis.clearBitCache();

            // Assert
            // After clearing the cache, reading 1 bit should return the next bit from the underlying stream.
            assertEquals(0, bis.readBits(1), "Should return the next bit after clearing the cache");
        }
    }

    @Test
    void testEndOfFileIsReachedAfterReadingAllBits() throws IOException {
        // Arrange
        try (BitInputStream bis = new BitInputStream(createTestInputStream(), ByteOrder.LITTLE_ENDIAN)) {
            // Act & Assert
            // Read all 32 bits.
            assertEquals(0x2f0140f8, bis.readBits(30), "Should read 30 bits correctly.");
            // Attempt to read more bits than available.
            assertEquals(-1, bis.readBits(3), "Should return -1 when reaching the end of the stream");
        }
    }

    @Test
    void testLittleEndianReadWithOverflow() throws Exception {
        // Arrange
        byte[] inputBytes = { 87, 45, 66, 15, 90, 29, 88, 61, 33, 74 };
        ByteArrayInputStream in = new ByteArrayInputStream(inputBytes);

        try (BitInputStream bin = new BitInputStream(in, ByteOrder.LITTLE_ENDIAN)) {
            // Act & Assert
            assertEquals(23, bin.readBits(5), "Should read the first 5 bits correctly");
            assertEquals(714595605644185962L, bin.readBits(63), "Should read the next 63 bits correctly");
            assertEquals(1186, bin.readBits(12), "Should read the next 12 bits correctly");
            assertEquals(-1, bin.readBits(1), "Should return -1 when reaching the end of the stream");
        }
    }

    @Test
    void testReading17BitsInBigEndian() throws IOException {
        // Arrange
        try (BitInputStream bis = new BitInputStream(createTestInputStream(), ByteOrder.BIG_ENDIAN)) {
            // Act & Assert
            // 1-11110000-10000000
            assertEquals(0x0001f080, bis.readBits(17), "Should read 17 bits correctly in Big Endian");
        }
    }

    @Test
    void testReading17BitsInLittleEndian() throws IOException {
        // Arrange
        try (BitInputStream bis = new BitInputStream(createTestInputStream(), ByteOrder.LITTLE_ENDIAN)) {
            // Act & Assert
            assertEquals(0x000140f8, bis.readBits(17), "Should read 17 bits correctly in Little Endian");
        }
    }

    @Test
    void testReading24BitsInBigEndian() throws IOException {
        // Arrange
        try (BitInputStream bis = new BitInputStream(createTestInputStream(), ByteOrder.BIG_ENDIAN)) {
            // Act & Assert
            assertEquals(0x00f84001, bis.readBits(24), "Should read 24 bits correctly in Big Endian");
        }
    }

    @Test
    void testReading24BitsInLittleEndian() throws IOException {
        // Arrange
        try (BitInputStream bis = new BitInputStream(createTestInputStream(), ByteOrder.LITTLE_ENDIAN)) {
            // Act & Assert
            assertEquals(0x000140f8, bis.readBits(24), "Should read 24 bits correctly in Little Endian");
        }
    }

    @Test
    void testReading30BitsInBigEndian() throws IOException {
        // Arrange
        try (BitInputStream bis = new BitInputStream(createTestInputStream(), ByteOrder.BIG_ENDIAN)) {
            // Act & Assert
            // 111110-00010000-00000000-01001011
            assertEquals(0x3e10004b, bis.readBits(30), "Should read 30 bits correctly in Big Endian");
        }
    }

    @Test
    void testReading30BitsInLittleEndian() throws IOException {
        // Arrange
        try (BitInputStream bis = new BitInputStream(createTestInputStream(), ByteOrder.LITTLE_ENDIAN)) {
            // Act & Assert
            assertEquals(0x2f0140f8, bis.readBits(30), "Should read 30 bits correctly in Little Endian");
        }
    }

    @Test
    void testReading31BitsInBigEndian() throws IOException {
        // Arrange
        try (BitInputStream bis = new BitInputStream(createTestInputStream(), ByteOrder.BIG_ENDIAN)) {
            // Act & Assert
            // 1111100-00100000-00000000-10010111
            assertEquals(0x7c200097, bis.readBits(31), "Should read 31 bits correctly in Big Endian");
        }
    }

    @Test
    void testReading31BitsInLittleEndian() throws IOException {
        // Arrange
        try (BitInputStream bis = new BitInputStream(createTestInputStream(), ByteOrder.LITTLE_ENDIAN)) {
            // Act & Assert
            assertEquals(0x2f0140f8, bis.readBits(31), "Should read 31 bits correctly in Little Endian");
        }
    }

    @Test
    void testReadingOneBitFromEmptyStreamReturnsMinusOne() throws Exception {
        // Arrange
        try (BitInputStream bis = new BitInputStream(new ByteArrayInputStream(ByteUtils.EMPTY_BYTE_ARRAY), ByteOrder.LITTLE_ENDIAN)) {
            // Act & Assert
            assertEquals(-1, bis.readBit(), "First bit should be -1 (EOF)");
            assertEquals(-1, bis.readBit(), "Second bit should be -1 (EOF)");
            assertEquals(-1, bis.readBit(), "Third bit should be -1 (EOF)");
        }
    }

    @Test
    void testReadingOneBitInBigEndianOrder() throws Exception {
        // Arrange
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0xEA, 0x03 }); // 0xEA = 11101010, 0x03 = 00000011
        try (BitInputStream bis = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN)) {
            // Act & Assert
            assertEquals(1, bis.readBit(), "bit 0 should be 1");
            assertEquals(1, bis.readBit(), "bit 1 should be 1");
            assertEquals(1, bis.readBit(), "bit 2 should be 1");
            assertEquals(0, bis.readBit(), "bit 3 should be 0");
            assertEquals(1, bis.readBit(), "bit 4 should be 1");
            assertEquals(0, bis.readBit(), "bit 5 should be 0");
            assertEquals(1, bis.readBit(), "bit 6 should be 1");
            assertEquals(0, bis.readBit(), "bit 7 should be 0");

            assertEquals(0, bis.readBit(), "bit 8 should be 0");
            assertEquals(0, bis.readBit(), "bit 9 should be 0");
            assertEquals(0, bis.readBit(), "bit 10 should be 0");
            assertEquals(0, bis.readBit(), "bit 11 should be 0");
            assertEquals(0, bis.readBit(), "bit 12 should be 0");
            assertEquals(0, bis.readBit(), "bit 13 should be 0");
            assertEquals(1, bis.readBit(), "bit 14 should be 1");
            assertEquals(1, bis.readBit(), "bit 15 should be 1");

            assertEquals(-1, bis.readBit(), "next bit should be -1 (EOF)");
        }
    }

    @Test
    void testReadingOneBitInLittleEndianOrder() throws Exception {
        // Arrange
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0xEA, 0x03 }); // 0xEA = 11101010, 0x03 = 00000011
        try (BitInputStream bis = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN)) {
            // Act & Assert
            assertEquals(0, bis.readBit(), "bit 0 should be 0");
            assertEquals(1, bis.readBit(), "bit 1 should be 1");
            assertEquals(0, bis.readBit(), "bit 2 should be 0");
            assertEquals(1, bis.readBit(), "bit 3 should be 1");
            assertEquals(0, bis.readBit(), "bit 4 should be 0");
            assertEquals(1, bis.readBit(), "bit 5 should be 1");
            assertEquals(1, bis.readBit(), "bit 6 should be 1");
            assertEquals(1, bis.readBit(), "bit 7 should be 1");

            assertEquals(1, bis.readBit(), "bit 8 should be 1");
            assertEquals(1, bis.readBit(), "bit 9 should be 1");
            assertEquals(0, bis.readBit(), "bit 10 should be 0");
            assertEquals(0, bis.readBit(), "bit 11 should be 0");
            assertEquals(0, bis.readBit(), "bit 12 should be 0");
            assertEquals(0, bis.readBit(), "bit 13 should be 0");
            assertEquals(0, bis.readBit(), "bit 14 should be 0");
            assertEquals(0, bis.readBit(), "bit 15 should be 0");

            assertEquals(-1, bis.readBit(), "next bit should be -1 (EOF)");
        }
    }

    @Test
    void testThrowsExceptionWhenReadingNegativeAmountOfBits() throws IOException {
        // Arrange
        try (BitInputStream bis = new BitInputStream(createTestInputStream(), ByteOrder.LITTLE_ENDIAN)) {
            // Act & Assert
            assertThrows(IOException.class, () -> bis.readBits(-1), "Should throw IOException when reading a negative number of bits");
        }
    }

    @Test
    void testThrowsExceptionWhenReadingMoreThan63BitsAtATime() throws IOException {
        // Arrange
        try (BitInputStream bis = new BitInputStream(createTestInputStream(), ByteOrder.LITTLE_ENDIAN)) {
            // Act & Assert
            assertThrows(IOException.class, () -> bis.readBits(64), "Should throw IOException when reading more than 63 bits");
        }
    }
}