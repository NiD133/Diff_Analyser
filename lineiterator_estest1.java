package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;

/**
 * Tests for {@link LineIterator}.
 */
public class LineIteratorTest {

    /**
     * Tests that the iterator correctly reads a single line of text
     * from a reader that contains no line terminators.
     */
    @Test
    public void next_whenReaderHasOnlyOneLine_shouldReturnThatLine() {
        // Arrange: Create a reader with a single, simple line of text.
        final String singleLine = "This is the only line.";
        final StringReader reader = new StringReader(singleLine);

        // Act & Assert: Use try-with-resources to ensure the iterator is closed.
        try (LineIterator lineIterator = new LineIterator(reader)) {
            // The iterator should report that a line is available.
            assertTrue("Iterator should have a next line.", lineIterator.hasNext());

            // Retrieve the line using the current 'next()' method.
            final String actualLine = lineIterator.next();

            // The retrieved line must match the original input.
            assertEquals("The line read should match the input.", singleLine, actualLine);

            // After reading the only line, the iterator should be exhausted.
            assertFalse("Iterator should have no more lines.", lineIterator.hasNext());
        }
    }
}