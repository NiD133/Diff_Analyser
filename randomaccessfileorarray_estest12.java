package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

public class RandomAccessFileOrArray_ESTestTest12 extends RandomAccessFileOrArray_ESTest_scaffolding {

    /**
     * Tests that readCharLE() correctly reads a two-byte character in
     * little-endian format and advances the internal file pointer accordingly.
     */
    @Test
    public void readCharLE_shouldReadCharacterInLittleEndianAndAdvancePointer() throws IOException {
        // Arrange
        // The character U+1E77 ('w' with a dot below) is represented in little-endian
        // byte order as {0x77, 0x1E}.
        // Byte 0: 0x77 (119) -> Least significant byte
        // Byte 1: 0x1E (30)  -> Most significant byte
        byte[] littleEndianBytes = new byte[]{(byte) 0x77, (byte) 0x1E};
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(littleEndianBytes);

        char expectedChar = '\u1E77';
        long expectedPositionAfterRead = Character.BYTES; // A char is 2 bytes

        // Act
        char actualChar = reader.readCharLE();
        long actualPositionAfterRead = reader.getFilePointer();

        // Assert
        assertEquals("The method should correctly interpret the little-endian bytes as a char.",
                expectedChar, actualChar);
        assertEquals("The file pointer should advance by the size of a char.",
                expectedPositionAfterRead, actualPositionAfterRead);
    }
}