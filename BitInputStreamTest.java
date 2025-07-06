package org.apache.commons.compress.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

import org.junit.jupiter.api.Test;

class BitInputStreamTest {

    /**
     * Helper method to create a ByteArrayInputStream with predefined bytes.
     */
    private ByteArrayInputStream createTestStream() {
        return new ByteArrayInputStream(new byte[] {
            (byte) 0xF8, // 11111000
            0x40,        // 01000000
            0x01,        // 00000001
            0x2F         // 00101111
        });
    }

    @Test
    void testAlignWithByteBoundaryAtBoundary() throws Exception {
        try (BitInputStream bis = new BitInputStream(createTestStream(), ByteOrder.LITTLE_ENDIAN)) {
            assertEquals(0xF8, bis.readBits(8), "Initial 8 bits should match");
            bis.alignWithByteBoundary();
            assertEquals(0, bis.readBits(4), "Next 4 bits should be zero after alignment");
        }
    }

    @Test
    void testAlignWithByteBoundaryNotAtBoundary() throws Exception {
        try (BitInputStream bis = new BitInputStream(createTestStream(), ByteOrder.LITTLE_ENDIAN)) {
            assertEquals(0x08, bis.readBits(4), "Initial 4 bits should match");
            assertEquals(4, bis.bitsCached(), "4 bits should be cached");
            bis.alignWithByteBoundary();
            assertEquals(0, bis.bitsCached(), "Cache should be cleared after alignment");
            assertEquals(0, bis.readBits(4), "Next 4 bits should be zero after alignment");
        }
    }

    @Test
    void testAvailableBitsWithCache() throws Exception {
        try (BitInputStream bis = new BitInputStream(createTestStream(), ByteOrder.LITTLE_ENDIAN)) {
            assertEquals(0x08, bis.readBits(4), "Initial 4 bits should match");
            assertEquals(28, bis.bitsAvailable(), "28 bits should be available after reading 4 bits");
        }
    }

    @Test
    void testAvailableBitsWithoutCache() throws Exception {
        try (BitInputStream bis = new BitInputStream(createTestStream(), ByteOrder.LITTLE_ENDIAN)) {
            assertEquals(32, bis.bitsAvailable(), "All 32 bits should be available initially");
        }
    }

    @Test
    void testBigEndianOverflow() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[] {
            87, 45, 66, 15, 90, 29, 88, 61, 33, 74
        });
        try (BitInputStream bin = new BitInputStream(in, ByteOrder.BIG_ENDIAN)) {
            assertEquals(10, bin.readBits(5), "First 5 bits should match");
            assertEquals(8274274654740644818L, bin.readBits(63), "Next 63 bits should match");
            assertEquals(330, bin.readBits(12), "Next 12 bits should match");
            assertEquals(-1, bin.readBits(1), "Reading beyond available bits should return -1");
        }
    }

    @Test
    void testClearBitCache() throws IOException {
        try (BitInputStream bis = new BitInputStream(createTestStream(), ByteOrder.LITTLE_ENDIAN)) {
            assertEquals(0x08, bis.readBits(4), "Initial 4 bits should match");
            bis.clearBitCache();
            assertEquals(0, bis.readBits(1), "Cache should be cleared, next bit should be zero");
        }
    }

    @Test
    void testEndOfFile() throws IOException {
        try (BitInputStream bis = new BitInputStream(createTestStream(), ByteOrder.LITTLE_ENDIAN)) {
            assertEquals(0x2f0140f8, bis.readBits(30), "First 30 bits should match");
            assertEquals(-1, bis.readBits(3), "Reading beyond available bits should return -1");
        }
    }

    @Test
    void testLittleEndianOverflow() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[] {
            87, 45, 66, 15, 90, 29, 88, 61, 33, 74
        });
        try (BitInputStream bin = new BitInputStream(in, ByteOrder.LITTLE_ENDIAN)) {
            assertEquals(23, bin.readBits(5), "First 5 bits should match");
            assertEquals(714595605644185962L, bin.readBits(63), "Next 63 bits should match");
            assertEquals(1186, bin.readBits(12), "Next 12 bits should match");
            assertEquals(-1, bin.readBits(1), "Reading beyond available bits should return -1");
        }
    }

    @Test
    void testReading17BitsBigEndian() throws IOException {
        try (BitInputStream bis = new BitInputStream(createTestStream(), ByteOrder.BIG_ENDIAN)) {
            assertEquals(0x0001f080, bis.readBits(17), "First 17 bits should match in big endian");
        }
    }

    @Test
    void testReading17BitsLittleEndian() throws IOException {
        try (BitInputStream bis = new BitInputStream(createTestStream(), ByteOrder.LITTLE_ENDIAN)) {
            assertEquals(0x000140f8, bis.readBits(17), "First 17 bits should match in little endian");
        }
    }

    @Test
    void testReading24BitsBigEndian() throws IOException {
        try (BitInputStream bis = new BitInputStream(createTestStream(), ByteOrder.BIG_ENDIAN)) {
            assertEquals(0x00f84001, bis.readBits(24), "First 24 bits should match in big endian");
        }
    }

    @Test
    void testReading24BitsLittleEndian() throws IOException {
        try (BitInputStream bis = new BitInputStream(createTestStream(), ByteOrder.LITTLE_ENDIAN)) {
            assertEquals(0x000140f8, bis.readBits(24), "First 24 bits should match in little endian");
        }
    }

    @Test
    void testReading30BitsBigEndian() throws IOException {
        try (BitInputStream bis = new BitInputStream(createTestStream(), ByteOrder.BIG_ENDIAN)) {
            assertEquals(0x3e10004b, bis.readBits(30), "First 30 bits should match in big endian");
        }
    }

    @Test
    void testReading30BitsLittleEndian() throws IOException {
        try (BitInputStream bis = new BitInputStream(createTestStream(), ByteOrder.LITTLE_ENDIAN)) {
            assertEquals(0x2f0140f8, bis.readBits(30), "First 30 bits should match in little endian");
        }
    }

    @Test
    void testReading31BitsBigEndian() throws IOException {
        try (BitInputStream bis = new BitInputStream(createTestStream(), ByteOrder.BIG_ENDIAN)) {
            assertEquals(0x7c200097, bis.readBits(31), "First 31 bits should match in big endian");
        }
    }

    @Test
    void testReading31BitsLittleEndian() throws IOException {
        try (BitInputStream bis = new BitInputStream(createTestStream(), ByteOrder.LITTLE_ENDIAN)) {
            assertEquals(0x2f0140f8, bis.readBits(31), "First 31 bits should match in little endian");
        }
    }

    @Test
    void testReadingOneBitFromEmptyStream() throws Exception {
        try (BitInputStream bis = new BitInputStream(new ByteArrayInputStream(ByteUtils.EMPTY_BYTE_ARRAY), ByteOrder.LITTLE_ENDIAN)) {
            assertEquals(-1, bis.readBit(), "Reading from empty stream should return -1");
            assertEquals(-1, bis.readBit(), "Reading from empty stream should return -1");
            assertEquals(-1, bis.readBit(), "Reading from empty stream should return -1");
        }
    }

    @Test
    void testReadingOneBitBigEndian() throws Exception {
        try (BitInputStream bis = new BitInputStream(new ByteArrayInputStream(new byte[] { (byte) 0xEA, 0x03 }), ByteOrder.BIG_ENDIAN)) {
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

            assertEquals(-1, bis.readBit(), "Reading beyond available bits should return -1");
        }
    }

    @Test
    void testReadingOneBitLittleEndian() throws Exception {
        try (BitInputStream bis = new BitInputStream(new ByteArrayInputStream(new byte[] { (byte) 0xEA, 0x03 }), ByteOrder.LITTLE_ENDIAN)) {
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

            assertEquals(-1, bis.readBit(), "Reading beyond available bits should return -1");
        }
    }

    @Test
    void testNegativeBitReadThrowsException() throws IOException {
        try (BitInputStream bis = new BitInputStream(createTestStream(), ByteOrder.LITTLE_ENDIAN)) {
            assertThrows(IOException.class, () -> bis.readBits(-1), "Reading negative bits should throw IOException");
        }
    }

    @Test
    void testReadingMoreThan63BitsThrowsException() throws IOException {
        try (BitInputStream bis = new BitInputStream(createTestStream(), ByteOrder.LITTLE_ENDIAN)) {
            assertThrows(IOException.class, () -> bis.readBits(64), "Reading more than 63 bits should throw IOException");
        }
    }
}