package org.apache.commons.io;

import org.junit.jupiter.api.Test; // Use JUnit 5 for better readability
import static org.junit.jupiter.api.Assertions.*; // Simplified assertions
import java.io.StringReader;

/**
 * Test case for the LineIterator class.
 * This test focuses on verifying that the LineIterator correctly handles a carriage return character.
 */
public class LineIteratorTest { // Renamed class for better clarity

    @Test
    void testLineIteratorWithCarriageReturn() {
        // Arrange: Create a StringReader with a string containing only a carriage return.
        StringReader reader = new StringReader("\r");

        // Act: Create a LineIterator from the StringReader and retrieve the next line.
        LineIterator iterator = new LineIterator(reader);
        String line = iterator.next();

        // Assert: Verify that the LineIterator returns an empty string.
        //  A carriage return by itself is treated as an empty line.
        assertEquals("", line, "LineIterator should return an empty string when encountering only a carriage return.");

        // Optional: Check that the iterator has no more lines.  This prevents accidental infinite loops
        assertFalse(iterator.hasNext(), "LineIterator should not have any more lines after reading the carriage return");

        // Clean up the iterator - important for resource management
        LineIterator.closeQuietly(iterator);

    }
}