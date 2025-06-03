package org.apache.commons.io.input;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoundedReaderTest {

    @Test
    public void testMarkResetMarkMore() throws IOException {
        // Input string for the reader
        String inputString = "abcde";

        // Create a StringReader from the input string.  This will be wrapped by our BoundedReader.
        StringReader stringReader = new StringReader(inputString);

        // Create a BoundedReader that allows reading a maximum of 3 characters.
        try (BoundedReader boundedReader = new BoundedReader(stringReader, 3)) {

            // Mark the current position. The readAheadLimit is set to 4, which is larger than the bound, so it should be okay
            boundedReader.mark(4);

            // Read three characters ('a', 'b', 'c'). The current position is now after 'c'.
            boundedReader.read(); // Reads 'a'
            boundedReader.read(); // Reads 'b'
            boundedReader.read(); // Reads 'c'

            // Reset the reader to the marked position (before 'a').
            boundedReader.reset();

            // Read three characters again ('a', 'b', 'c').
            boundedReader.read(); // Reads 'a'
            boundedReader.read(); // Reads 'b'
            boundedReader.read(); // Reads 'c'

            // Attempt to read another character.  The BoundedReader is exhausted (reached its limit of 3 characters),
            // so it should return -1 (end of stream).
            assertEquals(-1, boundedReader.read(), "Should return -1 as the BoundedReader is exhausted.");
        }
    }
}