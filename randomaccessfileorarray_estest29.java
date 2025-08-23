package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.IOException;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that readShortLE() correctly reads a 16-bit signed short
     * in little-endian byte order and advances the internal pointer by two bytes.
     */
    @Test
    public void readShortLE_shouldCorrectlyParseLittleEndianBytes() throws IOException {
        // --- Arrange ---
        // The test aims to read the little-endian representation of the short 0xAD00.
        // In little-endian, the least significant byte (0x00) comes first.
        byte[] inputBytes = new byte[] {
            (byte) 0x00, // Least Significant Byte
            (byte) 0xAD, // Most Significant Byte
            (byte) 0xFF  // Extra byte to ensure only two are read
        };

        // The expected short value is 0xAD00, which is -21248 in decimal.
        short expectedShort = (short) 0xAD00;
        long expectedPositionAfterRead = 2L;

        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(inputBytes);

        // --- Act ---
        short actualShort = reader.readShortLE();
        long actualPositionAfterRead = reader.getFilePointer();

        // --- Assert ---
        assertEquals("The method should correctly interpret the bytes as a little-endian short.",
                expectedShort, actualShort);
        
        assertEquals("The file pointer should advance by 2 bytes after reading a short.",
                expectedPositionAfterRead, actualPositionAfterRead);
    }
}