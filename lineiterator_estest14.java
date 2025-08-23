package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;

/**
 * Tests for {@link LineIterator}.
 */
public class LineIteratorTest {

    /**
     * Tests that the iterator correctly handles input containing only a single newline character.
     * It should return one empty line.
     */
    @Test
    public void nextShouldReturnEmptyStringForSingleNewline() {
        // Arrange
        final StringReader reader = new StringReader("\n");

        // Act & Assert
        // Use try-with-resources to ensure the iterator (and underlying reader) is closed.
        try (final LineIterator lineIterator = new LineIterator(reader)) {
            assertTrue("Iterator should have a line", lineIterator.hasNext());
            final String line = lineIterator.next();
            assertEquals("The first line should be empty for a single newline input", "", line);
        }
    }
}