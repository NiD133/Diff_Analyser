package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.io.StringReader;

/**
 * Contains tests for the {@link LineIterator} class.
 * This class was refactored from an auto-generated EvoSuite test for clarity.
 */
public class LineIterator_ESTestTest13 extends LineIterator_ESTest_scaffolding {

    /**
     * Tests that the iterator correctly handles input that contains only a single newline character.
     * The expected behavior is for the iterator to return one empty string and then report that it has no more lines.
     */
    @Test
    public void next_shouldReturnEmptyString_whenInputIsOnlyNewline() {
        // Arrange: Create a LineIterator with a reader containing just a newline character.
        final StringReader reader = new StringReader("\n");
        final LineIterator lineIterator = new LineIterator(reader);

        // Act & Assert:
        // 1. Verify that the iterator reports having a next line.
        assertTrue("Iterator should have a line for the empty string before the newline.", lineIterator.hasNext());

        // 2. Get the next line and verify it is an empty string.
        final String firstLine = lineIterator.next();
        assertEquals("The line returned should be an empty string.", "", firstLine);

        // 3. Verify that the iterator is now exhausted.
        assertFalse("Iterator should have no more lines after reading the empty line.", lineIterator.hasNext());
    }
}