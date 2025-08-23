package org.apache.commons.io;

import org.junit.Test;

import java.io.StringReader;

/**
 * Tests for {@link LineIterator}.
 */
public class LineIteratorTest {

    /**
     * Tests that calling the remove() method, which is an optional operation
     * for an Iterator, throws an UnsupportedOperationException. This is the
     * expected behavior for a read-only iterator.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void removeShouldThrowUnsupportedOperationException() {
        // Arrange: Create a LineIterator. The reader's content is irrelevant for this test.
        final StringReader reader = new StringReader("");
        final LineIterator iterator = new LineIterator(reader);

        // Act: Attempt to call the unsupported remove() method.
        // Assert: The @Test(expected=...) annotation asserts that an
        // UnsupportedOperationException is thrown.
        iterator.remove();
    }
}