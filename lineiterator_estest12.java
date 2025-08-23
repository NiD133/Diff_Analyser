package org.apache.commons.io;

import org.junit.Test;

import java.io.StringReader;
import java.util.NoSuchElementException;

/**
 * Unit tests for {@link LineIterator}.
 */
public class LineIteratorTest {

    /**
     * Verifies that calling next() on an iterator created from an empty reader
     * correctly throws a NoSuchElementException, as there are no lines to return.
     */
    @Test(expected = NoSuchElementException.class)
    public void nextShouldThrowNoSuchElementExceptionForEmptyReader() {
        // Arrange: Create a LineIterator with an empty source.
        final StringReader emptyReader = new StringReader("");
        final LineIterator iterator = new LineIterator(emptyReader);

        // Act & Assert: Attempting to get the next line should throw the expected exception.
        // The @Test(expected=...) annotation handles the assertion.
        iterator.next();
    }
}