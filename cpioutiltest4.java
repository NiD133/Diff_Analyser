package org.apache.commons.compress.archivers.cpio;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CpioUtil}.
 */
class CpioUtilTest {

    @Test
    void long2byteArrayShouldEncodeMagicNumberAsLittleEndianBytes() {
        // Arrange
        // The CPIO "old binary" magic number is 070707 (octal), which is 0x71c7 in hexadecimal.
        final long magicNumber = CpioConstants.MAGIC_OLD_BINARY;
        final int outputLength = 2;
        final boolean swapHalfWords = false; // This flag means little-endian byte order.

        // The expected little-endian byte representation of the 16-bit value 0x71c7.
        final byte[] expectedBytes = { (byte) 0xc7, 0x71 };

        // Act
        final byte[] actualBytes = CpioUtil.long2byteArray(magicNumber, outputLength, swapHalfWords);

        // Assert
        assertArrayEquals(expectedBytes, actualBytes,
            "The magic number should be converted to its correct 2-byte little-endian representation.");
    }
}