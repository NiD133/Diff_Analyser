package org.apache.commons.io;

import org.junit.Test;
import java.io.StringReader;
import static org.junit.Assert.assertFalse;

/**
 * Contains tests for the {@link LineIterator} class.
 */
public class LineIteratorTest {

    /**
     * Verifies that hasNext() returns false after an iterator has been closed
     * using the static LineIterator.closeQuietly() method.
     */
    @Test
    public void testHasNextReturnsFalseAfterCloseQuietly() {
        // Arrange: Create a LineIterator with some sample data.
        StringReader reader = new StringReader("line 1\nline 2");
        LineIterator iterator = new LineIterator(reader);

        // Act: Close the iterator quietly.
        LineIterator.closeQuietly(iterator);

        // Assert: Check that the iterator reports it has no more elements.
        assertFalse("hasNext() must return false after the iterator is closed.", iterator.hasNext());
    }
}