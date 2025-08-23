package org.apache.commons.compress.utils;

import static org.apache.commons.compress.utils.ByteUtils.toLittleEndian;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ByteUtils#toLittleEndian(DataOutput, long, int)}.
 */
class ByteUtilsTest {

    @Test
    @DisplayName("toLittleEndian should write a long value to a DataOutput in little-endian format")
    void toLittleEndianShouldWriteCorrectBytesToDataOutput() throws IOException {
        // Arrange
        // The value 262914 is represented in little-endian format by 3 bytes as {2, 3, 4}
        // because: 262914 = (2 * 256^0) + (3 * 256^1) + (4 * 256^2)
        final long valueToWrite = 2 + (3 * 256L) + (4 * 256L * 256L);
        final int length = 3;
        final byte[] expectedBytes = {2, 3, 4};

        try (final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);

            // Act
            toLittleEndian(dataOutput, valueToWrite, length);

            // Assert
            final byte[] actualBytes = byteArrayOutputStream.toByteArray();
            assertArrayEquals(expectedBytes, actualBytes,
                "The byte array should match the little-endian representation of the value.");
        }
    }
}