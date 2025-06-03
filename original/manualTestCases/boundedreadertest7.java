package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.StringReader;

import org.junit.jupiter.api.Test;

public class GeneratedTestCase {

    @Test
    public void testMarkResetFromOffset1() throws IOException {
        // GIVEN: A BoundedReader wrapping a StringReader with a limit of 3 characters.
        StringReader stringReader = new StringReader("abcde"); // The underlying reader with some content.
        BoundedReader boundedReader = new BoundedReader(stringReader, 3); // Limits reading to the first 3 chars.

        try (boundedReader) { // Ensure resources are closed properly.

            // WHEN: We mark the reader, read past the limit, then reset and mark again.
            boundedReader.mark(3); // Mark the current position (beginning).  The read limit from mark is 3.
            boundedReader.read(); // Read 'a'.  Position is now at 'b'.
            boundedReader.read(); // Read 'b'.  Position is now at 'c'.
            boundedReader.read(); // Read 'c'.  Position is now at 'd' but reader is bounded, we should get -1

            // THEN: Reading should return -1 because we've exceeded the bound.
            assertEquals(-1, boundedReader.read(), "Should return -1 as we've exceeded the bound.");

            // WHEN: We reset the reader to the mark and mark again with a smaller limit.
            boundedReader.reset(); // Reset back to the position when mark(3) was called (beginning).
            boundedReader.mark(1); // Mark again, but now with a limit of only 1 character.

            // WHEN: Read one character
            boundedReader.read();   //Read 'a'.

            // THEN:  Reading again should return -1 because we've exceeded the bound of 1 character.
            assertEquals(-1, boundedReader.read(), "Should return -1 as we've exceeded the new bound of 1.");
        }
    }
}