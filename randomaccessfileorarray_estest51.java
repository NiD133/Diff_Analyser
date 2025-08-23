package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * This class contains tests for the RandomAccessFileOrArray class.
 * This particular test was improved for understandability.
 */
public class RandomAccessFileOrArray_ESTestTest51 extends RandomAccessFileOrArray_ESTest_scaffolding {

    /**
     * Verifies that readChar() correctly reads a character and advances the file pointer
     * after a preceding read operation has already moved the pointer.
     */
    @Test
    public void readChar_advancesPointerAndReadsCorrectCharacter_afterInitialRead() throws IOException {
        // Arrange: Set up the input data and the object under test.
        // The byte array is structured as follows:
        // - Bytes 0-1: An unsigned short (0x0000)
        // - Bytes 2-3: A char representing '4' (0x0034, since ASCII '4' is 52)
        byte[] sourceData = new byte[8];
        sourceData[3] = '4'; // The high byte at index 2 is implicitly 0.

        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(sourceData);
        
        char expectedChar = '4';
        long expectedFilePointer = 4L; // 2 bytes for the short + 2 bytes for the char

        // Act: Perform the read operations.
        // First, read an unsigned short to advance the pointer past the first two bytes.
        reader.readUnsignedShort(); 
        
        // Then, read the character we are interested in.
        char actualChar = reader.readChar();
        long actualFilePointer = reader.getFilePointer();

        // Assert: Verify the results.
        assertEquals("The character read should match the one in the source data.", expectedChar, actualChar);
        assertEquals("The file pointer should be at position 4 after reading a short and a char.", expectedFilePointer, actualFilePointer);
    }
}