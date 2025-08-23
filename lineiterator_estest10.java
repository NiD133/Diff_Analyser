package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;

/**
 * Contains tests for the {@link LineIterator} class.
 * This refactored test focuses on improving clarity and maintainability.
 */
public class LineIteratorTest {

    /**
     * Verifies that hasNext() is idempotent, meaning it can be called multiple times
     * without changing the iterator's state, and it consistently returns true
     * when there is a next line available.
     */
    @Test
    public void hasNextShouldReturnTrueWhenCalledMultipleTimesOnSingleLineInput() {
        // Arrange: Create a LineIterator with a single line of text.
        StringReader reader = new StringReader("a single line");
        LineIterator lineIterator = new LineIterator(reader);

        // Act: Call hasNext() twice without calling next().
        // The first call reads and caches the line from the reader.
        boolean firstCallResult = lineIterator.hasNext();
        // The second call should return true based on the cached line.
        boolean secondCallResult = lineIterator.hasNext();

        // Assert: Verify that both calls returned true.
        assertTrue("First call to hasNext() should return true for a non-empty reader.", firstCallResult);
        assertTrue("Second call to hasNext() should also return true before next() is called.", secondCallResult);
    }
}