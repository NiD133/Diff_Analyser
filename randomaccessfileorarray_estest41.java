package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.IOException;

/**
 * This test class contains tests for the RandomAccessFileOrArray class.
 * This specific test was improved for understandability.
 */
public class RandomAccessFileOrArray_ImprovedTest {

    /**
     * Tests that readInt() correctly reads a 4-byte integer in big-endian format
     * and advances the file pointer accordingly, even after a preceding read operation.
     */
    @Test
    public void readInt_afterReadingShort_advancesPointerAndReturnsCorrectValue() throws IOException {
        // Arrange: Set up a byte array where the first two bytes are for a short,
        // and the next four bytes represent a big-endian integer.
        // The integer is formed by the bytes: 0xF7, 0x00, 0x00, 0x00.
        byte[] sourceData = new byte[] {
            0x00, 0x00,                   // Bytes for the initial readShort()
            (byte) 0xF7, 0x00, 0x00, 0x00, // Bytes for the readInt()
            0x00, 0x00                    // Trailing bytes
        };
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(sourceData);

        // Act:
        // 1. Read a short to advance the pointer by 2 bytes.
        reader.readShort();
        // 2. Read the integer from the new position.
        int actualIntValue = reader.readInt();
        long finalFilePointer = reader.getFilePointer();

        // Assert:
        // Verify that the integer was read correctly. The byte sequence 0xF7, 0x00, 0x00, 0x00
        // in big-endian format represents the integer 0xF7000000.
        int expectedIntValue = 0xF7000000; // This is -150994944 in decimal
        assertEquals("The integer read should match the big-endian byte representation.", expectedIntValue, actualIntValue);

        // Verify that the file pointer has advanced by 6 bytes in total (2 for the short + 4 for the int).
        long expectedFilePointer = 6L;
        assertEquals("File pointer should be at position 6 after reading a short and an int.", expectedFilePointer, finalFilePointer);
    }
}