package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Contains tests for {@link BoundedIterator}.
 * This class focuses on edge cases and exception handling.
 */
public class BoundedIteratorTest {

    /**
     * Tests that calling next() on a BoundedIterator that wraps an empty
     * iterator throws a NoSuchElementException.
     *
     * The BoundedIterator's constructor should not fail, but the first
     * call to next() must, as there are no elements to return.
     */
    @Test(expected = NoSuchElementException.class)
    public void nextShouldThrowNoSuchElementExceptionWhenUnderlyingIteratorIsEmpty() {
        // Arrange: Create a BoundedIterator wrapping an iterator with no elements.
        // Using Collections.emptyIterator() is a clear and direct way to represent this.
        final Iterator<String> emptyIterator = Collections.emptyIterator();
        final BoundedIterator<String> boundedIterator = new BoundedIterator<>(emptyIterator, 0L, 10L);

        // Act & Assert: Calling next() is expected to throw NoSuchElementException.
        // The @Test(expected=...) annotation handles the assertion.
        boundedIterator.next();
    }
}