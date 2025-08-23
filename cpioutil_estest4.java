package org.apache.commons.compress.archivers.cpio;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/**
 * Tests for the utility methods in {@link CpioUtil}.
 * This focuses on the long2byteArray method, ensuring it correctly converts numbers
 * and handles invalid arguments gracefully.
 */
public class CpioUtilTest {

    /**
     * Tests the conversion of a long to a standard big-endian byte array.
     */
    @Test
    public void long2byteArrayShouldConvertLongToBigEndianByteArray() {
        // A long with distinct bytes to make byte order obvious.
        final long numberToConvert = 0x0102030405060708L;
        final int outputLength = 8;
        final boolean swapHalfWords = false;

        final byte[] expected = {
            (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04,
            (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08
        };

        final byte[] actual = CpioUtil.long2byteArray(numberToConvert, outputLength, swapHalfWords);

        assertArrayEquals("The long should be converted to a big-endian byte array.", expected, actual);
    }

    /**
     * Tests the conversion of a long to a byte array with swapped half-words.
     * In CPIO, a "half-word" is a 2-byte pair. Swapping them changes the byte order
     * from [0][1][2][3] to [1][0][3][2].
     */
    @Test
    public void long2byteArrayShouldConvertLongToSwappedHalfWordByteArray() {
        final long numberToConvert = 0x0102030405060708L;
        final int outputLength = 8;
        final boolean swapHalfWords = true;

        // Expected order after swapping each 2-byte pair:
        // {0x02, 0x01, 0x04, 0x03, 0x06, 0x05, 0x08, 0x07}
        final byte[] expected = {
            (byte) 0x02, (byte) 0x01, (byte) 0x04, (byte) 0x03,
            (byte) 0x06, (byte) 0x05, (byte) 0x08, (byte) 0x07
        };

        final byte[] actual = CpioUtil.long2byteArray(numberToConvert, outputLength, swapHalfWords);

        assertArrayEquals("The long should be converted with half-words swapped.", expected, actual);
    }

    /**
     * Verifies that the method throws an exception when the requested array length
     * is an odd number, as this is not supported.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void long2byteArrayShouldThrowExceptionForOddLength() {
        CpioUtil.long2byteArray(123L, 3, false);
    }

    /**
     * Verifies that the method throws an exception when the requested array length
     * is negative, as this is not a valid length.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void long2byteArrayShouldThrowExceptionForNegativeLength() {
        CpioUtil.long2byteArray(123L, -2, false);
    }

    /**
     * Verifies that the method throws an exception when the requested array length
     * is zero, as it must be a positive multiple of two.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void long2byteArrayShouldThrowExceptionForZeroLength() {
        CpioUtil.long2byteArray(123L, 0, false);
    }
}