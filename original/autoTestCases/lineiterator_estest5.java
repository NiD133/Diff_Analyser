package org.apache.commons.io;

import org.junit.jupiter.api.Test; // Using JUnit 5 for better readability
import static org.junit.jupiter.api.Assertions.*; // Using JUnit 5 Assertions
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class LineIteratorTest {

    @Test
    void testNextThrowsIllegalStateExceptionWhenReaderIsClosed() throws IOException {
        // Arrange: Create a StringReader and wrap it in a BufferedReader.
        String testString = "This is a test line.";
        StringReader stringReader = new StringReader(testString);
        BufferedReader bufferedReader = new BufferedReader(stringReader);

        // Arrange: Create a LineIterator from the BufferedReader.
        LineIterator lineIterator = new LineIterator(bufferedReader);

        // Act: Close the BufferedReader.
        bufferedReader.close();

        // Assert: Calling next() on the LineIterator should throw an IllegalStateException.
        assertThrows(IllegalStateException.class, () -> {
            try {
                lineIterator.next();
            } catch (IllegalStateException e) {
                // Assert that the exception message indicates the underlying IOException.
                assertEquals("java.io.IOException: Stream closed", e.getMessage()); // Specific check
                throw e; // Re-throw so the assertThrows can verify the exception type
            }
        }, "Expected IllegalStateException when calling next() after closing the reader.");
    }
}