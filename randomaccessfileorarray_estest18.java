package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link RandomAccessFileOrArray#readShort()} method.
 */
public class RandomAccessFileOrArrayReadShortTest {

    @Test
    public void readShort_shouldReadTwoBytesAsBigEndianShort_andAdvancePointer() throws IOException {
        // Arrange
        // The readShort() method reads a 16-bit signed integer in big-endian format.
        // The byte sequence {0x10, 0x10} represents the value (0x10 * 256) + 0x10 = 4112.
        byte[] inputData = new byte[]{(byte) 0x10, (byte) 0x10, (byte) 0xFF}; // Added extra byte to ensure only two are read
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(inputData);

        short expectedValue = 4112;
        long expectedPointerPosition = 2L;

        // Act
        short actualValue = reader.readShort();
        long actualPointerPosition = reader.getFilePointer();

        // Assert
        assertEquals("The read short value should match the expected big-endian interpretation.", expectedValue, actualValue);
        assertEquals("The file pointer should advance by 2 bytes after reading a short.", expectedPointerPosition, actualPointerPosition);
    }
}