package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the readChar() method of the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayReadCharTest {

    /**
     * Verifies that readChar() correctly reads two bytes as a big-endian character
     * and advances the internal pointer accordingly.
     */
    @Test
    public void readChar_readsTwoBytesAsBigEndianChar_andAdvancesPointer() throws IOException {
        // Arrange
        // The character '\uF734' is formed by two bytes: 0xF7 and 0x34.
        // We place these at an offset to ensure the method reads from the correct position.
        byte[] sourceBytes = new byte[] {
            0x00, 0x00,       // 2 bytes to be skipped by an initial read
            (byte) 0xF7,      // High byte of the target character
            (byte) 0x34,      // Low byte of the target character
            0x00, 0x00, 0x00, 0x00
        };
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(sourceBytes);

        // Act
        // 1. Advance the pointer by 2 bytes to the start of our character data.
        reader.readShort();
        // 2. Read the character from the new position (index 2).
        char actualChar = reader.readChar();

        // Assert
        final char expectedChar = '\uF734';
        assertEquals("The character should be correctly read in big-endian format.", expectedChar, actualChar);

        final long expectedPointerPosition = 4L; // 2 bytes for readShort() + 2 bytes for readChar()
        assertEquals("The file pointer should be advanced to the correct position.", expectedPointerPosition, reader.getFilePointer());
    }
}