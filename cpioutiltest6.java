package org.apache.commons.compress.archivers.cpio;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link CpioUtil} class.
 */
class CpioUtilTest {

    /**
     * Tests that byteArray2long correctly converts a little-endian byte array
     * representing the "old binary" magic number to a long when half-word
     * swapping is disabled.
     */
    @Test
    void byteArray2longShouldConvertLittleEndianBytesWhenNotSwapped() {
        // Arrange
        // The CPIO "old binary" magic number is 070707 (octal) or 0x71c7 (hex).
        final long expectedMagicNumber = CpioConstants.MAGIC_OLD_BINARY;

        // This is the little-endian byte representation of 0x71c7.
        final byte[] oldBinaryMagicBytes = {(byte) 0xc7, (byte) 0x71};
        final boolean swapHalfWords = false;

        // Act
        final long actualMagicNumber = CpioUtil.byteArray2long(oldBinaryMagicBytes, swapHalfWords);

        // Assert
        assertEquals(expectedMagicNumber, actualMagicNumber,
            "The converted long should match the CPIO old binary magic number");
    }
}