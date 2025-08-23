package org.apache.commons.compress.utils;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link ByteUtils} class.
 */
public class ByteUtilsTest {

    /**
     * Tests that {@code fromLittleEndian} correctly decodes an 8-byte array
     * representing a negative long value. In little-endian format, the most
     * significant byte is last.
     */
    @Test
    public void fromLittleEndianShouldCorrectlyDecodeNegativeLong() {
        // Arrange: Create an 8-byte array for a little-endian long.
        // The most significant byte (at index 7) is set to 0xB8 (which is -72),
        // making the resulting 64-bit integer negative.
        // The byte array [0, 0, 0, 0, 0, 0, 0, 0xB8] represents the long
        // value 0xB800000000000000 in hexadecimal.
        byte[] littleEndianBytes = new byte[8];
        littleEndianBytes[7] = (byte) 0xB8; // -72 in decimal

        // Using a hex literal for the expected value makes the relationship
        // to the input byte array much clearer than the decimal equivalent.
        long expectedValue = 0xB800000000000000L; // -5188146770730811392L in decimal

        // Act: Convert the byte array to a long.
        long actualValue = ByteUtils.fromLittleEndian(littleEndianBytes);

        // Assert: The converted value should match the expected long.
        assertEquals(expectedValue, actualValue);
    }
}