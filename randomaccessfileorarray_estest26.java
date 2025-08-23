package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that readUnsignedInt() correctly reads four bytes as a big-endian
     * unsigned integer and advances the internal pointer by four positions.
     */
    @Test
    public void readUnsignedInt_readsFourBytesAsBigEndianAndAdvancesPointer() throws IOException {
        // Arrange
        // The method under test, readUnsignedInt(), reads data in big-endian format.
        // The decimal value 63232 is represented in hexadecimal as 0x0000F700.
        // As a byte array, this is {0x00, 0x00, 0xF7, 0x00}.
        // Note: (byte) 0xF7 is equivalent to the original test's (byte) -9.
        byte[] inputBytes = new byte[] {
            0x00, 0x00, (byte) 0xF7, 0x00,
            // Add extra bytes to ensure the method only reads the first four.
            0x01, 0x02, 0x03, 0x04
        };
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(inputBytes);
        long expectedValue = 63232L;

        // Act
        long actualValue = reader.readUnsignedInt();

        // Assert
        assertEquals("The unsigned int value should be read correctly in big-endian format.",
                expectedValue, actualValue);
        assertEquals("The file pointer should advance by 4 bytes after the read operation.",
                4L, reader.getFilePointer());
    }
}