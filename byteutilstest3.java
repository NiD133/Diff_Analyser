package org.apache.commons.compress.utils;

import static org.apache.commons.compress.utils.ByteUtils.fromLittleEndian;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ByteUtils}.
 */
class ByteUtilsTest {

    /**
     * The {@code fromLittleEndian(byte[])} method converts a byte array to a long.
     * Since a long consists of 8 bytes, this test verifies that the method throws an
     * {@link IllegalArgumentException} if the input array is longer than 8 bytes.
     */
    @Test
    void fromLittleEndianFromArrayShouldThrowExceptionForOversizedArray() {
        // A long is 8 bytes, so an array of 9 bytes is too large to convert.
        final byte[] oversizedArray = new byte[9];

        assertThrows(IllegalArgumentException.class, () -> fromLittleEndian(oversizedArray),
            "Should throw an exception for arrays longer than 8 bytes.");
    }
}