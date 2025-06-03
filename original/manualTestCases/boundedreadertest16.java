package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class BoundedReaderExampleTest {

    @Test
    public void testSkipAndReadWithBoundedReader() throws IOException {
        // 1. Arrange: Define the input string and create a StringReader.
        String inputString = "This is a test string.";
        Reader stringReader = new StringReader(inputString);

        // 2. Arrange: Create a BoundedReader that limits the read length to 3 characters.
        int limit = 3;
        BoundedReader boundedReader = new BoundedReader(stringReader, limit);

        // 3. Act: Skip the first 2 characters.
        long skipped = boundedReader.skip(2);

        // 4. Assert: Verify that we skipped the correct number of characters.
        assertEquals(2, skipped, "Skipped characters should match the requested amount.");

        // 5. Act: Read a single character.  After skipping two characters, the next character is 'i'.
        int firstReadChar = boundedReader.read();

        // 6. Assert: Verify that the read character is correct.
        assertEquals('i', firstReadChar, "The first read character should be 'i'.");

        // 7. Act: Attempt to read again. The BoundedReader is limited to 3 characters. We've already skipped 2 and read 1 (total of 3).  Therefore, the next read should return -1, indicating the end of the bounded input.
        int secondReadChar = boundedReader.read();

        // 8. Assert: Verify that reading beyond the limit returns -1.
        assertEquals(-1, secondReadChar, "Reading beyond the limit should return -1.");

        // 9. Close the reader to release resources.  This is important to do in a real-world scenario.
        boundedReader.close();
        stringReader.close(); //Close the StringReader as well

    }
}