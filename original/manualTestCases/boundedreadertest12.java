package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class GeneratedTestCase {

    @Test
    public void testReadMulti() throws IOException {
        // Input string for the BoundedReader
        String inputString = "0123456789";

        // Create a StringReader from the input string.
        try (StringReader stringReader = new StringReader(inputString);
             // Create a BoundedReader that limits reading to 3 characters from the StringReader.
             BoundedReader boundedReader = new BoundedReader(stringReader, 3)) {

            // Buffer to read characters into.  Initialized with 'X' to easily verify which parts were read.
            char[] charBuffer = new char[4];
            Arrays.fill(charBuffer, 'X'); // Initialize the buffer with 'X'

            // Attempt to read 4 characters from the BoundedReader into the buffer, starting at index 0.
            int bytesRead = boundedReader.read(charBuffer, 0, 4);

            // Assert that the number of characters read is 3 (the limit of the BoundedReader).
            assertEquals(3, bytesRead, "Expected to read 3 characters.");

            // Assert that the first 3 characters in the buffer are '0', '1', and '2'.
            assertEquals('0', charBuffer[0], "First character should be '0'");
            assertEquals('1', charBuffer[1], "Second character should be '1'");
            assertEquals('2', charBuffer[2], "Third character should be '2'");

            // Assert that the fourth character in the buffer remains 'X' (was not overwritten because only 3 characters were read).
            assertEquals('X', charBuffer[3], "Fourth character should remain 'X' (not overwritten)");
        } // try-with-resources ensures the readers are closed.
    }
}