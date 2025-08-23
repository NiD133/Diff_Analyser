package org.apache.commons.compress.archivers.cpio;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link CpioUtil} utility class.
 */
class CpioUtilTest {

    /**
     * The CPIO "old binary" magic number is 070707 (octal), which is 0x71c7 in hexadecimal.
     * This test verifies that when converting this number to a 2-byte array with the
     * swapHalfWord flag enabled, the result is in big-endian byte order. The flag
     * effectively reverses the default little-endian conversion for each 2-byte pair.
     */
    @Test
    @DisplayName("long2byteArray should produce big-endian bytes for a 2-byte value when swap is enabled")
    void longToByteArrayWithSwapShouldProduceBigEndianBytes() {
        // Arrange
        // The CPIO old binary magic number is 070707 (octal) or 0x71c7 (hex).
        final long magicNumber = CpioConstants.MAGIC_OLD_BINARY;
        final int outputLength = 2;
        final boolean enableSwap = true;

        // For the number 0x71c7, the big-endian byte representation is {0x71, 0xc7}.
        final byte[] expectedBigEndianBytes = { 0x71, (byte) 0xc7 };

        // Act
        final byte[] actualBytes = CpioUtil.long2byteArray(magicNumber, outputLength, enableSwap);

        // Assert
        assertArrayEquals(expectedBigEndianBytes, actualBytes,
            "The conversion with swap enabled should produce a big-endian byte array.");
    }
}