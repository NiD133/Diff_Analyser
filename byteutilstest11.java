package org.apache.commons.compress.utils;

import static org.apache.commons.compress.utils.ByteUtils.fromLittleEndian;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ByteUtils}.
 */
class ByteUtilsTest {

    @Test
    void fromLittleEndianShouldReadCorrectValueFromInputStream() throws IOException {
        // Arrange
        // The input byte array represents a multi-byte number in little-endian format.
        // The test will read the first 3 bytes: [0x02, 0x03, 0x04].
        // The last byte (0x05) should be ignored by the method call.
        final byte[] littleEndianBytes = { 2, 3, 4, 5 };
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(littleEndianBytes);
        final int bytesToRead = 3;

        // The expected value for the little-endian bytes [0x02, 0x03, 0x04] is calculated as:
        // 2 * (256^0) + 3 * (256^1) + 4 * (256^2) = 2 + 768 + 262144 = 262914
        final long expectedValue = 262914;

        // Act
        final long actualValue = fromLittleEndian(inputStream, bytesToRead);

        // Assert
        assertEquals(expectedValue, actualValue,
            "The method should correctly interpret the 3-byte little-endian value from the stream.");
    }
}