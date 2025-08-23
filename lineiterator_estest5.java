package org.apache.commons.io;

import org.junit.Test;
import java.io.StringReader;
import java.util.NoSuchElementException;

/**
 * Unit tests for {@link LineIterator}.
 */
public class LineIteratorTest {

    /**
     * Tests that creating a new LineIterator on an already consumed Reader
     * results in an empty iterator. This is because both iterators share the
     * same underlying Reader instance, which has advanced past its content.
     */
    @Test(expected = NoSuchElementException.class)
    public void nextShouldThrowExceptionForIteratorOnAlreadyConsumedReader() {
        // Arrange: Create a reader and a "consuming" iterator to exhaust it.
        StringReader reader = new StringReader("a single line");
        LineIterator consumingIterator = new LineIterator(reader);

        // Act: Consume the only line from the reader.
        consumingIterator.next();

        // Arrange: Create a second iterator on the same, now-consumed reader.
        LineIterator secondIterator = new LineIterator(reader);

        // Assert: Calling next() on the second iterator throws NoSuchElementException
        // because the underlying reader has no more lines. The assertion is handled
        // by the @Test(expected=...) annotation.
        secondIterator.next();
    }
}