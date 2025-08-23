package com.itextpdf.text.pdf;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    @Test
    public void readLong_shouldReturnCorrectBigEndianValueAndAdvancePointer() throws IOException {
        // Arrange
        // A byte array representing a 64-bit integer (long) in big-endian format.
        // The value is 0x0000000098000000, which is constructed from the bytes below.
        byte[] inputBytes = new byte[]{
                0x00, 0x00, 0x00, 0x00, (byte) 0x98, 0x00, 0x00, 0x00
        };
        RandomAccessFileOrArray randomAccess = new RandomAccessFileOrArray(inputBytes);

        // The expected long value is 2,550,136,832, which is 0x98000000 in hexadecimal.
        // This is derived from (0x98 << 24).
        long expectedValue = 0x98000000L;
        long expectedPointerPosition = 8L;

        // Act
        long actualValue = randomAccess.readLong();
        long actualPointerPosition = randomAccess.getFilePointer();

        // Assert
        assertEquals("The read long value should match the big-endian representation.", expectedValue, actualValue);
        assertEquals("The file pointer should advance by 8 bytes after reading a long.", expectedPointerPosition, actualPointerPosition);
    }
}