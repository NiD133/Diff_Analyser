package org.apache.commons.io.input;

import org.junit.jupiter.api.Test; // Changed to JUnit 5
import static org.junit.jupiter.api.Assertions.*; // Changed to JUnit 5
import java.io.IOException;
import java.io.StringReader;

public class BoundedReaderTest {  // Renamed class for better clarity

    @Test
    void testReadCharArrayWithOffsetAndLength() throws IOException { // Renamed method for clarity
        // Arrange: Create a StringReader and a BoundedReader with a limit of 1 character.
        String inputString = "org.apache.commons.io.input.BoundedReader";
        StringReader stringReader = new StringReader(inputString);
        BoundedReader boundedReader = new BoundedReader(stringReader, 1);

        // Arrange: Create a character array to read into.
        char[] charArray = new char[6];

        // Act: Read 1 character from the BoundedReader into the character array,
        //      starting at index 1.
        int bytesRead = boundedReader.read(charArray, 1, 1);

        // Assert: Verify that 1 character was read.
        assertEquals(1, bytesRead, "Expected 1 character to be read.");

        // Assert: Verify that the character 'o' was placed at index 1 of the array.
        //         The rest of the array should remain unchanged (filled with default '\u0000').
        char[] expectedArray = new char[] { '\u0000', 'o', '\u0000', '\u0000', '\u0000', '\u0000' };
        assertArrayEquals(expectedArray, charArray, "Character array should contain 'o' at index 1.");
    }
}