package org.apache.commons.compress.utils;

import static org.apache.commons.compress.utils.ByteUtils.fromLittleEndian;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ByteUtils}.
 */
// The class name has been standardized to follow common Java testing conventions.
// Original: ByteUtilsTestTest14
class ByteUtilsTest {

    @Test
    @DisplayName("fromLittleEndian should correctly read a 4-byte unsigned value from an InputStream")
    void fromLittleEndianFromStreamShouldReadUnsigned32BitValue() throws IOException {
        // Arrange
        // The byte array {0x02, 0x03, 0x04, 0x80} represents the 32-bit little-endian
        // value 0x80040302. This value is chosen because it is larger than Integer.MAX_VALUE,
        // which tests the handling of unsigned integers that must be stored in a long.
        final byte[] littleEndianBytes = {2, 3, 4, (byte) 128};
        final InputStream inputStream = new ByteArrayInputStream(littleEndianBytes);

        // The expected value is 0x80040302 in hexadecimal.
        final long expectedValue = 0x80040302L;

        // Act
        // Read 4 bytes from the stream and interpret them as a little-endian number.
        final long actualValue = fromLittleEndian(inputStream, 4);

        // Assert
        assertEquals(expectedValue, actualValue);
    }
}