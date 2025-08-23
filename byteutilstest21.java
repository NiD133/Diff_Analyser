package org.apache.commons.compress.utils;

import static org.apache.commons.compress.utils.ByteUtils.toLittleEndian;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.commons.compress.utils.ByteUtils.OutputStreamByteConsumer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ByteUtils#toLittleEndian(ByteUtils.ByteConsumer, long, int)}.
 */
public class ByteUtilsTestTest21 {

    @Test
    @DisplayName("toLittleEndian with an OutputStreamByteConsumer should write the correct little-endian byte sequence")
    void toLittleEndianWithConsumerWritesCorrectBytes() throws IOException {
        // Arrange
        // The number 262914 is 0x040302 in hexadecimal.
        final long valueToConvert = 0x040302L;
        final int numberOfBytes = 3;

        // In little-endian format, the least significant byte (0x02) comes first.
        final byte[] expectedBytes = { 0x02, 0x03, 0x04 };

        // Act
        final byte[] actualBytes;
        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            final OutputStreamByteConsumer consumer = new OutputStreamByteConsumer(outputStream);
            toLittleEndian(consumer, valueToConvert, numberOfBytes);
            actualBytes = outputStream.toByteArray();
        }

        // Assert
        assertArrayEquals(expectedBytes, actualBytes,
            "The byte sequence should be in little-endian order.");
    }
}