package org.apache.commons.io;

import org.junit.jupiter.api.Test;  // Using JUnit 5 for clarity
import static org.junit.jupiter.api.Assertions.*; // Using JUnit 5 assertions

import java.io.BufferedReader;
import java.io.StringReader;

public class LineIteratorSimpleTest {

    @Test
    public void testValidLineCheck() throws Exception {
        // Arrange:  Create a simple string reader with a single newline character.
        StringReader stringReader = new StringReader("\n");
        BufferedReader bufferedReader = new BufferedReader(stringReader);

        // Act: Create a LineIterator from the reader.  This iterator is designed
        //      to read lines from a Reader object.
        LineIterator lineIterator = new LineIterator(bufferedReader);

        // Assert: Verify that the `isValidLine` method returns true when passed "I?".
        //         The original test's logic implicitly assumes that isValidLine will check if the passed string is not null or empty (after trimming).  "\n" is handled as an empty line. Therefore, "I?" should return true since it's not an empty line.
        assertTrue(lineIterator.isValidLine("I?"), "isValidLine should return true for a non-null, non-empty string.");

        // Cleanup: Properly close the iterator and reader (optional, but good practice).
        LineIterator.closeQuietly(lineIterator);
        bufferedReader.close();
    }
}