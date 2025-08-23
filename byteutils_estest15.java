package org.apache.commons.compress.utils;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link ByteUtils}.
 */
public class ByteUtilsTest {

    /**
     * Tests that {@link ByteUtils#fromLittleEndian(byte[])} correctly converts an 8-byte array
     * in little-endian format to a long value.
     */
    @Test
    public void fromLittleEndianWithFullByteArrayShouldConvertCorrectly() {
        // Arrange
        // Create an 8-byte array to represent a little-endian long.
        // We set the byte at index 5 to 0xFB. In little-endian, the byte at index `i`
        // corresponds to the i-th power of 256.
        byte[] littleEndianBytes = new byte[8];
        littleEndianBytes[5] = (byte) 0xFB; // 0xFB is -5 in signed byte representation

        // The expected long value is 0xFB shifted left by 40 bits (5 * 8).
        // Hex representation is much clearer than the decimal equivalent (275977418571776L).
        long expectedValue = 0xFB0000000000L;

        // Act
        long actualValue = ByteUtils.fromLittleEndian(littleEndianBytes);

        // Assert
        assertEquals(expectedValue, actualValue);
    }
}