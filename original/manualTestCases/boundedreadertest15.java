package org.apache.commons.io.input;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UnderstandableBoundedReaderTest {

    @Test
    public void testBoundedReaderLimitsReadLength() throws IOException {
        // GIVEN: A string to be read and a BoundedReader that limits the reading to 3 characters.
        String data = "This is a test string.";
        Reader stringReader = new StringReader(data);
        BoundedReader boundedReader = new BoundedReader(stringReader, 3);

        // WHEN: We read from the BoundedReader.
        boundedReader.read(); // Read the first character ('T').
        boundedReader.read(); // Read the second character ('h').
        int result = boundedReader.read(); // Attempt to read the third character.

        // THEN: The BoundedReader should return -1 after reading 3 characters, indicating the end of the bounded region.
        assertEquals(-1, result, "BoundedReader should return -1 after reaching the limit of 3 characters.");

        // Closing the BoundedReader (and implicitly the StringReader) - important for resource management.
        boundedReader.close();
    }

    // A simple StringReader for testing purposes, using a short string.
    private final Reader shortReader = new StringReader("ABC");
}