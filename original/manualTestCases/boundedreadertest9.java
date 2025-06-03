package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.junit.jupiter.api.Test;

public class GeneratedTestCase {

    @Test
    public void testMarkResetWithMarkOutsideBoundedReaderMax() throws IOException {
        // GIVEN: A string to read from and the maximum length the BoundedReader will allow.
        String inputString = "abc";
        int maxLength = 3;

        // AND: Create a StringReader from the input string.
        Reader stringReader = new StringReader(inputString);

        // AND: Create a BoundedReader wrapping the StringReader, limiting the read length.
        //      The BoundedReader will only read up to 'maxLength' characters.
        try (BoundedReader boundedReader = new BoundedReader(stringReader, maxLength)) {

            // WHEN: Mark a position beyond the BoundedReader's maximum allowed length.
            //       Attempting to mark at a position outside the bounds of the BoundedReader
            //       should still allow us to read up to the maximum length.
            boundedReader.mark(4);  // Mark position 4 (which is past the end of "abc")

            // AND: Read characters from the BoundedReader until it reaches the end of the bounded region.
            boundedReader.read(); // Read 'a'
            boundedReader.read(); // Read 'b'
            boundedReader.read(); // Read 'c'

            // THEN: Ensure that attempting to read beyond the bounded region returns -1,
            //       indicating the end of the stream.
            assertEquals(-1, boundedReader.read(), "Should return -1 when trying to read past the bounded length.");
        } // try-with-resources ensures the BoundedReader is closed.
    }
}