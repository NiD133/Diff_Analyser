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
 * Tests for the DataOutput-related methods in {@link ByteUtils}.
 */
public class ByteUtilsTest {

    @Test
    @DisplayName("toLittleEndian should write a 4-byte unsigned integer correctly to a DataOutput")
    void toLittleEndianWritesUnsignedInt32ToDataOutput() throws IOException {
        // ARRANGE
        // The value 2,147,746,562 (0x80040302L) represents an unsigned 32-bit integer.
        // It exceeds Integer.MAX_VALUE, so it must be stored in a long.
        final long valueToWrite = 0x80040302L;
        final int length = 4;

        // In little-endian byte order, this value is represented as {0x02, 0x03, 0x04, 0x80}.
        final byte[] expectedBytes = { 2, 3, 4, (byte) 128 };

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            final DataOutput dos = new DataOutputStream(bos);

            // ACT
            toLittleEndian(dos, valueToWrite, length);

            // ASSERT
            assertArrayEquals(expectedBytes, bos.toByteArray());
        }
    }
}