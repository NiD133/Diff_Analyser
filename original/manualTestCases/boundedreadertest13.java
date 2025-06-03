package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class GeneratedTestCase {

    @Test
    public void testReadMultiWithOffset() throws IOException {
        // 1. Arrange:
        //   - Create a StringReader with the input string "0123456789".
        String inputString = "0123456789";
        StringReader stringReader = new StringReader(inputString);

        //   - Create a BoundedReader that wraps the StringReader and limits reading to 3 characters.
        BoundedReader boundedReader = new BoundedReader(stringReader, 3);

        //   - Create a character buffer of size 4, filled with 'X' initially.
        char[] charBuffer = new char[4];
        Arrays.fill(charBuffer, 'X');

        // 2. Act:
        //   - Call the read method of the BoundedReader, attempting to read 2 characters into the charBuffer,
        //     starting at offset 1.  Store the number of characters actually read.
        int charactersRead = boundedReader.read(charBuffer, 1, 2);

        // 3. Assert:
        //   - Assert that the read method returns 2, indicating that 2 characters were read.
        assertEquals(2, charactersRead, "The number of characters read should be 2.");

        //   - Assert that the charBuffer now contains 'X', '0', '1', 'X'.
        //     - charBuffer[0] should remain 'X' because we started writing at offset 1.
        //     - charBuffer[1] should be '0', the first character read from the StringReader.
        //     - charBuffer[2] should be '1', the second character read from the StringReader.
        //     - charBuffer[3] should remain 'X' because we only read 2 characters.
        assertEquals('X', charBuffer[0], "charBuffer[0] should be 'X'");
        assertEquals('0', charBuffer[1], "charBuffer[1] should be '0'");
        assertEquals('1', charBuffer[2], "charBuffer[2] should be '1'");
        assertEquals('X', charBuffer[3], "charBuffer[3] should be 'X'");

        // 4. Cleanup:
        //   - Close the BoundedReader to release resources.  Although in this case, closing a StringReader
        //     doesn't actually do anything, it's good practice to close Readers within try-with-resources.
        boundedReader.close(); // Added explicit close for completeness.  StringReader.close() is a no-op.
    }
}