package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit tests for the {@link ByteUtils} class.
 */
public class ByteUtilsTest {

    @Test
    public void fromLittleEndianShouldReturnZeroForZeroFilledByteArray() {
        // Arrange: Create a byte array filled with zeros. The fromLittleEndian
        // method should interpret this as the number 0, regardless of array length (up to 8).
        byte[] inputBytes = new byte[2];

        // Act: Convert the byte array to a long using little-endian byte order.
        long result = ByteUtils.fromLittleEndian(inputBytes);

        // Assert: The resulting long should be 0.
        assertEquals("An array of all-zero bytes should be converted to 0L.", 0L, result);
    }
}