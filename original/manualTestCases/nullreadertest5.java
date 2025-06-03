package org.apache.commons.io.input;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.io.Reader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class GeneratedTestCase {

    @Test
    public void testReadCharArray() throws Exception {
        // Test setup: Create a TestNullReader that will provide a sequence of characters.
        // The reader is configured to have a total length of 15 characters.
        final int readerLength = 15;
        final Reader reader = new TestNullReader(readerLength);

        // Test case 1: Read characters into a char array of size 10.
        // Expected behavior: The reader should fill the array completely (read 10 characters).
        final int arraySize = 10;
        final char[] chars = new char[arraySize];
        int charsRead = reader.read(chars);

        // Assertion: Verify that the correct number of characters was read.
        assertEquals(arraySize, charsRead, "First read should return the array's size (10).");

        // Assertion: Verify that the characters read are the expected sequence (0 to 9).
        for (int i = 0; i < charsRead; i++) {
            assertEquals(i, chars[i], "Character at index " + i + " should match the expected value.");
        }

        // Test case 2: Read characters into the same char array again.
        // Expected behavior: The reader has 5 characters remaining (15 - 10 = 5), so it should read only 5 characters.
        charsRead = reader.read(chars);

        // Assertion: Verify that the correct number of characters was read (5).
        assertEquals(5, charsRead, "Second read should return the remaining number of characters (5).");

        // Assertion: Verify that the characters read are the expected sequence (10 to 14). Note the overlap in array positions.
        for (int i = 0; i < charsRead; i++) {
            assertEquals(arraySize + i, chars[i], "Character at index " + i + " (second read) should match the expected value.");
        }

        // Test case 3: Attempt to read more characters after reaching the end of the reader.
        // Expected behavior: The reader should return -1, indicating the end of the stream.
        charsRead = reader.read(chars);

        // Assertion: Verify that the reader returns -1, signaling the end of the stream.
        assertEquals(-1, charsRead, "Third read should return -1 (end of stream).");

        // Test case 4: Attempt to read from the reader after it has already returned -1.
        // Expected behavior: An IOException should be thrown, indicating that the reader is closed or at the end of the stream.
        try {
            reader.read(chars);
            fail("Expected an IOException to be thrown when reading after EOF.");
        } catch (IOException e) {
            // Assertion: Verify that the correct exception message is thrown.
            assertEquals("Read after end of file", e.getMessage(), "The exception message should indicate reading after end of file.");
        }

        // Reset the reader (by closing it) to prepare for the final test case. This simulates re-opening a stream.
        reader.close();

        // Test case 5: Read characters into the array using an offset and a length.
        // Expected behavior: The reader should read 'length' characters, starting at the specified 'offset' in the array.
        final int offset = 2;
        final int length = 4;
        final char[] chars2 = new char[10]; // Use a new array to avoid interference
        charsRead = reader.read(chars2, offset, length);

        // Assertion: Verify that the correct number of characters were read (length = 4).
        assertEquals(length, charsRead, "The fourth read should return the specified length (4).");

        // Assertion: Verify that the characters were read into the correct positions in the array, and have the expected values.
        for (int i = 0; i < length; i++) { // Check only the positions where the characters were written.
            assertEquals(i, chars2[offset + i], "Character at index " + (offset + i) + " (offset read) should match the expected value.");
        }
    }
}