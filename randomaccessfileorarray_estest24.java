package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the RandomAccessFileOrArray class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that readUnsignedShort() correctly reads a two-byte, big-endian
     * value and advances the internal pointer by two bytes.
     */
    @Test
    public void readUnsignedShort_readsBigEndianValueAndAdvancesPointer() throws IOException {
        // Arrange: Set up a byte array where the first two bytes represent the number 16.
        // In big-endian format, the number 16 is represented by the byte sequence {0x00, 0x10}.
        byte[] sourceData = new byte[]{0x00, 0x10, (byte) 0xFF, (byte) 0xFF};
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray(sourceData);

        int expectedValue = 16;
        long expectedPointerPosition = 2L;

        // Act: Read the unsigned short from the beginning of the data.
        int actualValue = fileOrArray.readUnsignedShort();
        long actualPointerPosition = fileOrArray.getFilePointer();

        // Assert: Verify that the correct value was read and the pointer was advanced.
        assertEquals("The method should correctly interpret the two bytes as an unsigned short.", expectedValue, actualValue);
        assertEquals("The file pointer should advance by 2 bytes after the read operation.", expectedPointerPosition, actualPointerPosition);
    }
}