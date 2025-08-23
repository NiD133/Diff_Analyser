package org.apache.commons.compress.utils;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link ByteUtils}.
 */
public class ByteUtilsTest {

    /**
     * Tests that {@link ByteUtils#fromLittleEndian(byte[], int, int)}
     * correctly converts a single byte from an array into a long,
     * treating it as an unsigned value.
     */
    @Test
    public void fromLittleEndianShouldConvertSingleByteToUnsignedLong() {
        // Arrange
        // The byte at index 1 represents the unsigned value 188.
        // In Java, a byte is signed, so (byte) 188 is stored as -68.
        byte[] sourceData = { 0x00, (byte) 188, 0x00 };
        int offset = 1;
        int length = 1;
        long expectedValue = 188L;

        // Act
        long actualValue = ByteUtils.fromLittleEndian(sourceData, offset, length);

        // Assert
        assertEquals(expectedValue, actualValue);
    }
}