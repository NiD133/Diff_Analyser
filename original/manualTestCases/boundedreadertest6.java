package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.junit.jupiter.api.Test;

public class GeneratedTestCase {

    @Test
    public void testMarkReset() throws IOException {
        // 1. Setup: Create a StringReader with a sample string.
        String testString = "abcde";
        Reader stringReader = new StringReader(testString);

        // 2. Create a BoundedReader, limiting it to the first 3 characters of the string.
        int limit = 3;
        try (BoundedReader boundedReader = new BoundedReader(stringReader, limit)) { // try-with-resources ensures proper closing

            // 3. Mark the current position (start of the reader).  The readAheadLimit is the number
            // of characters that can be read while still preserving the mark.
            boundedReader.mark(limit); //Mark at the start with a readAheadLimit of limit.

            // 4. Read the first three characters, consuming the BoundedReader.
            boundedReader.read(); // Reads 'a'
            boundedReader.read(); // Reads 'b'
            boundedReader.read(); // Reads 'c'

            // 5. Reset the reader to the marked position (the beginning).
            boundedReader.reset();

            // 6. Read the first three characters again.
            boundedReader.read(); // Reads 'a' again
            boundedReader.read(); // Reads 'b' again
            boundedReader.read(); // Reads 'c' again

            // 7. Verify that reading beyond the limit returns -1 (end of stream).
            assertEquals(-1, boundedReader.read(), "Should return -1 indicating end of stream");
        }
    }
}