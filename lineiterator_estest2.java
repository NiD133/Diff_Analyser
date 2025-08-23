package org.apache.commons.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import org.junit.Test;

/**
 * Tests for {@link LineIterator}.
 */
public class LineIteratorTest {

    /**
     * Tests that calling next() on a reader with a single line returns that line.
     */
    @Test
    public void testNext_whenReaderHasOneLine_returnsThatLine() {
        // Arrange
        final String singleLine = "The quick brown fox";
        final StringReader reader = new StringReader(singleLine);

        try (final LineIterator lineIterator = new LineIterator(reader)) {
            // Act & Assert
            assertTrue("Iterator should have a next line", lineIterator.hasNext());
            
            final String actualLine = lineIterator.next();
            assertEquals("The returned line should match the input", singleLine, actualLine);
            
            assertFalse("Iterator should have no more lines", lineIterator.hasNext());
        }
    }
}