package org.apache.commons.compress.utils;

import static org.apache.commons.compress.utils.ByteUtils.fromLittleEndian;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link ByteUtils} class.
 */
class ByteUtilsTest {

    @Test
    void fromLittleEndian_shouldReadCorrectValueFromDataInput() throws IOException {
        // Arrange
        // The input bytes are {2, 3, 4, 5}. We will read the first 3 bytes.
        // In little-endian format, the bytes {0x02, 0x03, 0x04} represent a number
        // where 0x02 is the least significant byte.
        final byte[] inputBytes = {2, 3, 4, 5};
        final DataInput dataInput = new DataInputStream(new ByteArrayInputStream(inputBytes));
        final int numberOfBytesToRead = 3;

        // The little-endian value of {0x02, 0x03, 0x04} is 0x040302.
        // In decimal: (4 * 65536) + (3 * 256) + 2 = 262914.
        // Using a hex literal makes the expected value's byte representation clear.
        final long expectedValue = 0x040302L;

        // Act
        final long actualValue = fromLittleEndian(dataInput, numberOfBytesToRead);

        // Assert
        assertEquals(expectedValue, actualValue,
            "The method should correctly interpret the 3-byte little-endian value.");
    }
}