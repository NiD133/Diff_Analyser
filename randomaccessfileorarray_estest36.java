package com.itextpdf.text.pdf;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class RandomAccessFileOrArray_ESTestTest36 {

    /**
     * Tests that readLong() correctly interprets an 8-byte sequence from the input
     * as a big-endian long value and advances the internal pointer accordingly.
     */
    @Test
    public void readLongShouldReturnCorrectBigEndianValueAndAdvancePointer() throws IOException {
        // Arrange
        // Create a byte array representing a 64-bit long in big-endian format.
        // The value is 0x0010000000000000, where the second byte is 16 (0x10).
        byte[] bigEndianLongBytes = new byte[]{
                (byte) 0x00, (byte) 0x10, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00
        };

        // The expected long value corresponding to the byte array above.
        // Using a hex literal makes the relationship to the byte array clear.
        long expectedLongValue = 0x0010000000000000L; // This is 4503599627370496 in decimal

        ByteArrayInputStream inputStream = new ByteArrayInputStream(bigEndianLongBytes);
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(inputStream);

        // Act
        long actualLongValue = reader.readLong();

        // Assert
        // 1. Verify that the correct long value was read from the stream.
        assertEquals("The read long value should match the big-endian representation.",
                expectedLongValue, actualLongValue);

        // 2. Verify that the file pointer has advanced by the size of a long (8 bytes).
        long expectedPosition = Long.BYTES;
        assertEquals("The file pointer should advance by 8 bytes after reading a long.",
                expectedPosition, reader.getFilePointer());
    }
}