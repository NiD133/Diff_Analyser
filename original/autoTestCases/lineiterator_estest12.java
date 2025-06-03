package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*; // Consider removing this if mockito isn't directly used in this example
import java.io.StringReader;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

public class LineIteratorTest {

    @Test(timeout = 4000)
    public void testNextLineAfterForEachRemainingThrowsNoSuchElementException() throws Throwable {
        // Arrange: Create a StringReader with some sample text.
        String testString = ")!PsaWEtKc";
        StringReader stringReader = new StringReader(testString);

        // Act: Create a LineIterator from the StringReader.
        LineIterator lineIterator = new LineIterator(stringReader);

        // Act: Use forEachRemaining to consume all lines in the iterator.
        //      We're mocking the Consumer to avoid needing to actually process the lines.
        @SuppressWarnings("unchecked") // Suppress unchecked cast warning for the mocked Consumer
        Consumer<Object> consumer = (Consumer<Object>) mock(Consumer.class); // Use explicit cast if needed

        // Call the `forEachRemaining` method, which should exhaust the iterator
        lineIterator.forEachRemaining(consumer);

        // Assert: After consuming all lines, calling nextLine() should throw a NoSuchElementException.
        try {
            lineIterator.nextLine();
            fail("Expected NoSuchElementException to be thrown.");
        } catch (NoSuchElementException e) {
            // Assert that the exception message is "No more lines" (or similar, depending on implementation).
            assertEquals("No more lines", e.getMessage()); // Check exception message
        } finally {
            // Ensure the LineIterator is closed properly.  This is important to prevent resource leaks.
            LineIterator.closeQuietly(lineIterator); // Add closing
        }
    }
}