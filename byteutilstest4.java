package org.apache.commons.compress.utils;

import static org.apache.commons.compress.utils.ByteUtils.fromLittleEndian;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ByteUtils}.
 */
class ByteUtilsTest {

    @Test
    void fromLittleEndianShouldCorrectlyDecodeUnsigned32BitValue() {
        // Arrange
        // This byte array represents the 32-bit number 0x80040302 in little-endian format.
        // The most significant byte (0x80) makes the value larger than Integer.MAX_VALUE,
        // testing the method's ability to handle unsigned 32-bit integers correctly.
        final byte[] littleEndianBytes = {0x02, 0x03, 0x04, (byte) 0x80};
        final long expectedValue = 0x80040302L;

        // Act
        final long actualValue = fromLittleEndian(littleEndianBytes);

        // Assert
        assertEquals(expectedValue, actualValue,
            "The decoded long should match the expected little-endian value.");
    }
}