package org.apache.commons.compress.utils;

import static org.apache.commons.compress.utils.ByteUtils.toLittleEndian;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

public class ByteUtilsTestTest20 {

    /**
     * Tests that {@link ByteUtils#toLittleEndian(byte[], long, int, int)}
     * correctly encodes a 32-bit value into a byte array where the most
     * significant bit is set. This is a key test case for handling values
     * that might be misinterpreted in a signed context.
     */
    @Test
    void toLittleEndianShouldCorrectlyEncode32BitValueWithHighBitSet() {
        // Arrange
        // The value 0x80040302L corresponds to the little-endian byte sequence
        // {0x02, 0x03, 0x04, 0x80}. Using a hex literal makes the relationship
        // between the number and its byte representation much clearer.
        final long valueToEncode = 0x80040302L;
        final byte[] expectedBytes = {2, 3, 4, (byte) 128};
        final byte[] actualBytes = new byte[4];

        // Act
        toLittleEndian(actualBytes, valueToEncode, 0, 4);

        // Assert
        assertArrayEquals(expectedBytes, actualBytes,
            "The byte array should contain the correct little-endian representation of the value.");
    }
}