package org.apache.commons.io;

import static org.junit.Assert.assertFalse;

import java.io.StringReader;
import org.junit.Test;

/**
 * Tests for {@link LineIterator}.
 */
public class LineIteratorTest {

    /**
     * Tests that hasNext() returns false when the underlying reader is empty.
     */
    @Test
    public void hasNext_shouldReturnFalse_whenReaderIsEmpty() {
        // Arrange: Create a LineIterator with an empty source.
        // Using try-with-resources to ensure the iterator and underlying reader are closed.
        try (final StringReader emptyReader = new StringReader("");
             final LineIterator lineIterator = new LineIterator(emptyReader)) {

            // Act: Check if there is a next line.
            final boolean hasNext = lineIterator.hasNext();

            // Assert: The iterator should report that it has no lines.
            assertFalse("An iterator for an empty reader should not have a next line.", hasNext);
        }
    }
}