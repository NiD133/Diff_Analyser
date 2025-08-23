package com.itextpdf.text.pdf;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class.
 * This specific test focuses on the readUnsignedShort() method.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that readUnsignedShort() correctly reads two bytes as a big-endian
     * unsigned short integer and advances the file pointer accordingly.
     */
    @Test
    public void readUnsignedShort_readsTwoBytesAndReturnsCorrectUnsignedValue() throws IOException {
        // Arrange
        // Create a byte array where the third and fourth bytes represent the unsigned short 0xF700.
        // 0xF7 is -9 in a signed byte.
        // 0xF700 in decimal is (247 * 256) + 0 = 63232.
        byte[] inputData = {
                0x00, 0x00,       // Bytes for the initial readShort() to skip
                (byte) 0xF7, 0x00, // The two bytes to be read as an unsigned short
                0x00, 0x00, 0x00, 0x00
        };
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(inputData);

        // Act
        // 1. Skip the first two bytes to position the pointer at our target data.
        reader.readShort();

        // 2. Read the unsigned short value, which is the focus of this test.
        int actualValue = reader.readUnsignedShort();

        // Assert
        // Verify that the correct unsigned short value was read.
        int expectedValue = 63232;
        assertEquals("The unsigned short value should be read correctly.", expectedValue, actualValue);

        // Verify that the file pointer has advanced by a total of 4 bytes (2 for readShort, 2 for readUnsignedShort).
        long expectedFilePointer = 4L;
        long actualFilePointer = reader.getFilePointer();
        assertEquals("The file pointer should be advanced to the correct position.", expectedFilePointer, actualFilePointer);
    }
}