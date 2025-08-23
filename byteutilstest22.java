package org.apache.commons.compress.utils;

import static org.apache.commons.compress.utils.ByteUtils.toLittleEndian;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.commons.compress.utils.ByteUtils.OutputStreamByteConsumer;
import org.junit.jupiter.api.Test;

public class ByteUtilsTestTest22 {

    @Test
    void toLittleEndianWithConsumerWritesUnsignedIntCorrectly() throws IOException {
        // Arrange
        // The test value is 0x80040302, an unsigned 32-bit integer that is larger
        // than Integer.MAX_VALUE. This helps verify the correct handling of values
        // that might otherwise be misinterpreted as negative.
        final long valueToWrite = 0x80040302L;
        final byte[] expectedBytes = { 2, 3, 4, (byte) 128 }; // Little-endian for 0x80040302

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final OutputStreamByteConsumer consumer = new OutputStreamByteConsumer(outputStream);

        // Act
        toLittleEndian(consumer, valueToWrite, 4);

        // Assert
        final byte[] actualBytes = outputStream.toByteArray();
        assertArrayEquals(expectedBytes, actualBytes);
    }
}