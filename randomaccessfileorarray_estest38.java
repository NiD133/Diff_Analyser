package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class.
 * This specific test focuses on the readIntLE() method.
 */
public class RandomAccessFileOrArray_ESTestTest38 { // Retaining original class name for context

    /**
     * Tests that readIntLE() correctly parses a 32-bit integer from a byte array
     * in little-endian format.
     *
     * Little-endian format means the least significant byte is read first.
     * The test provides the byte sequence [0x00, 0x00, 0xF7, 0x00].
     * The expected integer is constructed as:
     * (b4 << 24) | (b3 << 16) | (b2 << 8) | b1
     * (0x00 << 24) | (0xF7 << 16) | (0x00 << 8) | 0x00 = 0x00F70000 = 16187392
     */
    @Test
    public void readIntLE_shouldCorrectlyParseLittleEndianInteger() throws IOException {
        // Arrange
        // Input bytes representing a little-endian integer. The value -9 is 0xF7 as a byte.
        byte[] littleEndianBytes = {
            (byte) 0x00, // Least significant byte
            (byte) 0x00,
            (byte) 0xF7,
            (byte) 0x00  // Most significant byte
        };
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(littleEndianBytes);
        int expectedValue = 16187392; // Hex: 0x00F70000

        // Act
        int actualValue = reader.readIntLE();

        // Assert
        assertEquals("The parsed integer should match the expected little-endian value.",
            expectedValue, actualValue);
        assertEquals("File pointer should advance by 4 bytes after reading an int.",
            4L, reader.getFilePointer());
    }
}