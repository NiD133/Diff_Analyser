package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that readChar() correctly reads a two-byte character in big-endian format
     * and advances the file pointer accordingly, even after other read operations.
     */
    @Test
    public void readChar_afterReadingAnotherValue_returnsCorrectCharAndAdvancesPointer() throws IOException {
        // --- Arrange ---
        // Create a byte array where the bytes at index 1 and 2 represent a character.
        // Byte 0: 0x00 (will be consumed by an initial readBoolean() call)
        // Byte 1: 0x10 (high byte of the character)
        // Byte 2: 0x00 (low byte of the character)
        // In big-endian format, bytes {0x10, 0x00} represent the character '\u1000'.
        byte[] sourceData = new byte[]{0x00, 0x10, 0x00};
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(sourceData);

        // --- Act ---
        // First, read a boolean to advance the file pointer past the first byte.
        reader.readBoolean();
        // Then, read the character we want to test.
        char actualChar = reader.readChar();

        // --- Assert ---
        // 1. Verify that the character was read correctly.
        char expectedChar = '\u1000';
        assertEquals("The character should be correctly read as big-endian", expectedChar, actualChar);

        // 2. Verify that the file pointer is in the correct position after all reads.
        // Pointer should be at 3 (1 byte for the boolean + 2 bytes for the char).
        long expectedPointerPosition = 3L;
        assertEquals("File pointer should be at position 3 after reading a boolean and a char",
                expectedPointerPosition, reader.getFilePointer());
    }
}