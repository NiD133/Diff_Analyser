package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This test suite focuses on the behavior of the ObjectGraphIterator,
 * particularly its handling of empty or exhausted iterators.
 */
public class ObjectGraphIterator_ESTestTest35 {

    /**
     * Verifies that calling next() on an iterator that was initialized
     * with a null root (making it effectively empty) throws a NoSuchElementException.
     */
    @Test(expected = NoSuchElementException.class)
    public void nextShouldThrowExceptionWhenIteratorIsEmpty() {
        // Arrange: Create an iterator that is empty from the start by providing a null root iterator.
        final ObjectGraphIterator<Object> emptyIterator = new ObjectGraphIterator<>((Iterator<?>) null);

        // Act & Assert: Calling next() on the empty iterator is expected to throw
        // a NoSuchElementException, which is verified by the @Test annotation.
        emptyIterator.next();
    }
}