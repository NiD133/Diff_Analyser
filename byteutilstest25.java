package org.apache.commons.compress.utils;

import static org.apache.commons.compress.utils.ByteUtils.toLittleEndian;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ByteUtils}.
 */
class ByteUtilsTest {

    /**
     * Tests that {@link ByteUtils#toLittleEndian(OutputStream, long, int)}
     * correctly writes a multi-byte value to an OutputStream in little-endian byte order.
     */
    @Test
    void toLittleEndianShouldWriteValueToStreamInLittleEndianOrder() throws IOException {
        // Arrange
        // The value 0x040302L is chosen because its byte representation in big-endian
        // is {0x04, 0x03, 0x02}, making it easy to verify the little-endian conversion.
        final long valueToWrite = 0x040302L;
        final byte[] expectedLittleEndianBytes = { 0x02, 0x03, 0x04 };
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Act
        toLittleEndian(outputStream, valueToWrite, expectedLittleEndianBytes.length);

        // Assert
        final byte[] actualBytes = outputStream.toByteArray();
        assertArrayEquals(expectedLittleEndianBytes, actualBytes,
            "The long value should be written to the stream as a 3-byte little-endian sequence.");
    }
}