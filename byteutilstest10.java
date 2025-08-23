package org.apache.commons.compress.utils;

import static org.apache.commons.compress.utils.ByteUtils.fromLittleEndian;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ByteUtils}.
 */
public class ByteUtilsTest {

    @Test
    void fromLittleEndianDataInputShouldReadUnsigned32BitValue() throws IOException {
        // Arrange
        // These bytes represent the number 2,147,746,562 in little-endian format.
        // This value (0x80040302) is larger than Integer.MAX_VALUE, which allows us
        // to verify that the method correctly reads a 4-byte value into a long
        // without incorrect sign extension.
        final byte[] littleEndianBytes = {0x02, 0x03, 0x04, (byte) 0x80};
        final DataInput dataInput = new DataInputStream(new ByteArrayInputStream(littleEndianBytes));

        // The expected value in hexadecimal makes the little-endian conversion obvious.
        // Bytes: 02 03 04 80 (LE) -> Long: 0x80040302L
        final long expectedValue = 0x80040302L;

        // Act
        final long actualValue = fromLittleEndian(dataInput, 4);

        // Assert
        assertEquals(expectedValue, actualValue);
    }
}