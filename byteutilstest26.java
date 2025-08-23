package org.apache.commons.compress.utils;

import static org.apache.commons.compress.utils.ByteUtils.toLittleEndian;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ByteUtils}.
 * The original test class name 'ByteUtilsTestTest26' was renamed for clarity.
 */
class ByteUtilsTest {

    /**
     * Tests that {@code toLittleEndian} correctly writes a 32-bit value that is larger than
     * {@link Integer#MAX_VALUE}, which is a common way to handle unsigned 32-bit integers.
     */
    @Test
    void toLittleEndianShouldWriteUnsigned32BitValueToStream() throws IOException {
        // Arrange
        // The value 0x80040302L is used because it is larger than Integer.MAX_VALUE,
        // testing the method's ability to handle unsigned 32-bit integers passed as a long.
        final long valueToWrite = 0x80040302L;

        // The expected little-endian byte representation of 0x80040302 is {0x02, 0x03, 0x04, 0x80}.
        final byte[] expectedBytes = { 2, 3, 4, (byte) 128 };
        final int lengthInBytes = 4; // 32 bits

        // Act
        final byte[] actualBytes;
        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            toLittleEndian(outputStream, valueToWrite, lengthInBytes);
            actualBytes = outputStream.toByteArray();
        }

        // Assert
        assertArrayEquals(expectedBytes, actualBytes);
    }
}