package org.apache.commons.compress.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

public class BitInputStreamTest {

    private static final byte[] SAMPLE_STREAM = new byte[] { (byte) 0xF8, 0x40, 0x01, 0x2F };

    private ByteArrayInputStream getSampleStream() {
        return new ByteArrayInputStream(SAMPLE_STREAM);
    }

    // Section: Bit alignment

    @Test
    void testAlignWithByteBoundaryWhenAtBoundary() throws Exception {
        try (BitInputStream bis = new BitInputStream(getSampleStream(), ByteOrder.LITTLE_ENDIAN)) {
            assertEquals(0xF8, bis.readBits(8));
            bis.alignWithByteBoundary();
            assertEquals(0, bis.readBits(4));
        }
    }

    @Test
    void testAlignWithByteBoundaryWhenNotAtBoundary() throws Exception {
        try (BitInputStream bis = new BitInputStream(getSampleStream(), ByteOrder.LITTLE_ENDIAN)) {
            assertEquals(0x08, bis.readBits(4));
            assertEquals(4, bis.bitsCached());
            bis.alignWithByteBoundary();
            assertEquals(0, bis.bitsCached());
            assertEquals(0, bis.readBits(4));
        }
    }

    // Section: Available bits

    @Test
    void testAvailableBitsWithCache() throws Exception {
        try (BitInputStream bis = new BitInputStream(getSampleStream(), ByteOrder.LITTLE_ENDIAN)) {
            assertEquals(0x08, bis.readBits(4));
            assertEquals(28, bis.bitsAvailable());
        }
    }

    @Test
    void testAvailableBitsWithoutCache() throws Exception {
        try (BitInputStream bis = new BitInputStream(getSampleStream(), ByteOrder.LITTLE_ENDIAN)) {
            assertEquals(32, bis.bitsAvailable());
        }
    }

    // Section: Endianness

    @Test
    void testBigEndianWithOverflow() throws Exception {
        final byte[] bigEndianStream = new byte[] { 87, 45, 66, 15, 90, 29, 88, 61, 33, 74 };
        try (BitInputStream bin = new BitInputStream(new ByteArrayInputStream(bigEndianStream), ByteOrder.BIG_ENDIAN)) {
            assertEquals(10, bin.readBits(5));
            assertEquals(8274274654740644818L, bin.readBits(63));
            assertEquals(330, bin.readBits(12));
            assertEquals(-1, bin.readBits(1));
        }
    }

    @Test
    void testLittleEndianWithOverflow() throws Exception {
        final byte[] littleEndianStream = new byte[] { 87, 45, 66, 15, 90, 29, 88, 61, 33, 74 };
        try (BitInputStream bin = new BitInputStream(new ByteArrayInputStream(littleEndianStream), ByteOrder.LITTLE_ENDIAN)) {
            assertEquals(23, bin.readBits(5));
            assertEquals(714595605644185962L, bin.readBits(63));
            assertEquals(1186, bin.readBits(12));
            assertEquals(-1, bin.readBits(1));
        }
    }

    // Section: Bit reading

    @Test
    void testReading17BitsInBigEndian() throws IOException {
        try (BitInputStream bis = new BitInputStream(getSampleStream(), ByteOrder.BIG_ENDIAN)) {
            assertEquals(0x0001f080, bis.readBits(17));
        }
    }

    @Test
    void testReading17BitsInLittleEndian() throws IOException {
        try (BitInputStream bis = new BitInputStream(getSampleStream(), ByteOrder.LITTLE_ENDIAN)) {
            assertEquals(0x000140f8, bis.readBits(17));
        }
    }

    @Test
    void testReading24BitsInBigEndian() throws IOException {
        try (BitInputStream bis = new BitInputStream(getSampleStream(), ByteOrder.BIG_ENDIAN)) {
            assertEquals(0x00f84001, bis.readBits(24));
        }
    }

    @Test
    void testReading24BitsInLittleEndian() throws IOException {
        try (BitInputStream bis = new BitInputStream(getSampleStream(), ByteOrder.LITTLE_ENDIAN)) {
            assertEquals(0x000140f8, bis.readBits(24));
        }
    }

    @Test
    void testReading30BitsInBigEndian() throws IOException {
        try (BitInputStream bis = new BitInputStream(getSampleStream(), ByteOrder.BIG_ENDIAN)) {
            assertEquals(0x3e10004b, bis.readBits(30));
        }
    }

    @Test
    void testReading30BitsInLittleEndian() throws IOException {
        try (BitInputStream bis = new BitInputStream(getSampleStream(), ByteOrder.LITTLE_ENDIAN)) {
            assertEquals(0x2f0140f8, bis.readBits(30));
        }
    }

    @Test
    void testReading31BitsInBigEndian() throws IOException {
        try (BitInputStream bis = new BitInputStream(getSampleStream(), ByteOrder.BIG_ENDIAN)) {
            assertEquals(0x7c200097, bis.readBits(31));
        }
    }

    @Test
    void testReading31BitsInLittleEndian() throws IOException {
        try (BitInputStream bis = new BitInputStream(getSampleStream(), ByteOrder.LITTLE_ENDIAN)) {
            assertEquals(0x2f0140f8, bis.readBits(31));
        }
    }

    // Section: Edge cases

    @Test
    void testClearBitCache() throws IOException {
        try (BitInputStream bis = new BitInputStream(getSampleStream(), ByteOrder.LITTLE_ENDIAN)) {
            assertEquals(0x08, bis.readBits(4));
            bis.clearBitCache();
            assertEquals(0, bis.readBits(1));
        }
    }

    @Test
    void testEOF() throws IOException {
        try (BitInputStream bis = new BitInputStream(getSampleStream(), ByteOrder.LITTLE_ENDIAN)) {
            assertEquals(0x2f0140f8, bis.readBits(30));
            assertEquals(-1, bis.readBits(3));
        }
    }

    @Test
    void testReadingOneBitFromEmptyStream() throws Exception {
        try (BitInputStream bis = new BitInputStream(new ByteArrayInputStream(new byte[0]), ByteOrder.LITTLE_ENDIAN)) {
            assertEquals(-1, bis.readBit());
            assertEquals(-1, bis.readBit());
            assertEquals(-1, bis.readBit());
        }
    }

    @Test
    void testReadingOneBitInBigEndian() throws Exception {
        try (BitInputStream bis = new BitInputStream(new ByteArrayInputStream(new byte[] { (byte) 0xEA, 0x03 }), ByteOrder.BIG_ENDIAN)) {
            assertEquals(1, bis.readBit());
            assertEquals(1, bis.readBit());
            assertEquals(1, bis.readBit());
            assertEquals(0, bis.readBit());
            assertEquals(1, bis.readBit());
            assertEquals(0, bis.readBit());
            assertEquals(1, bis.readBit());
            assertEquals(0, bis.readBit());
            assertEquals(0, bis.readBit());
            assertEquals(0, bis.readBit());
            assertEquals(0, bis.readBit());
            assertEquals(1, bis.readBit());
            assertEquals(1, bis.readBit());
            assertEquals(-1, bis.readBit());
        }
    }

    @Test
    void testReadingOneBitInLittleEndian() throws Exception {
        try (BitInputStream bis = new BitInputStream(new ByteArrayInputStream(new byte[] { (byte) 0xEA, 0x03 }), ByteOrder.LITTLE_ENDIAN)) {
            assertEquals(0, bis.readBit());
            assertEquals(1, bis.readBit());
            assertEquals(0, bis.readBit());
            assertEquals(1, bis.readBit());
            assertEquals(0, bis.readBit());
            assertEquals(1, bis.readBit());
            assertEquals(1, bis.readBit());
            assertEquals(1, bis.readBit());
            assertEquals(1, bis.readBit());
            assertEquals(1, bis.readBit());
            assertEquals(0, bis.readBit());
            assertEquals(0, bis.readBit());
            assertEquals(0, bis.readBit());
            assertEquals(0, bis.readBit());
            assertEquals(0, bis.readBit());
            assertEquals(-1, bis.readBit());
        }
    }

    @Test
    void testShouldNotAllowReadingOfANegativeAmountOfBits() throws IOException {
        try (BitInputStream bis = new BitInputStream(getSampleStream(), ByteOrder.LITTLE_ENDIAN)) {
            assertThrows(IOException.class, () -> bis.readBits(-1));
        }
    }

    @Test
    void testShouldNotAllowReadingOfMoreThan63BitsAtATime() throws IOException {
        try (BitInputStream bis = new BitInputStream(getSampleStream(), ByteOrder.LITTLE_ENDIAN)) {
            assertThrows(IOException.class, () -> bis.readBits(64));
        }
    }
}