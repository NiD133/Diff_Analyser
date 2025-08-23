package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.Collections;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Contains tests for the {@link FilterListIterator} class.
 */
public class FilterListIteratorTest {

    /**
     * Tests that calling next() on a FilterListIterator wrapping an empty
     * iterator throws a NoSuchElementException.
     */
    @Test(expected = NoSuchElementException.class)
    public void testNextOnEmptyIteratorShouldThrowNoSuchElementException() {
        // Arrange: Create a FilterListIterator with an empty underlying iterator.
        // A null predicate is used, meaning no elements will be filtered out.
        // The behavior should be identical to the underlying empty iterator.
        final ListIterator<String> emptyIterator = Collections.<String>emptyList().listIterator();
        final FilterListIterator<String> filterIterator = new FilterListIterator<>(emptyIterator);

        // Act & Assert: Calling next() should throw the expected exception.
        filterIterator.next();
    }
}