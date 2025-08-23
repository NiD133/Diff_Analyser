package org.apache.commons.collections4.iterators;

import org.junit.Test;
import java.util.NoSuchElementException;

/**
 * This class contains tests for the {@link FilterListIterator}.
 * This specific test focuses on the behavior of an iterator that has not been
 * initialized with an underlying collection's iterator.
 */
public class FilterListIterator_ESTestTest45 { // Retaining original class name for context

    /**
     * Tests that calling next() on a FilterListIterator created with the default
     * constructor (and thus without an underlying iterator) throws a
     * NoSuchElementException.
     */
    @Test(expected = NoSuchElementException.class)
    public void nextShouldThrowNoSuchElementExceptionWhenNoIteratorIsSet() {
        // Arrange: Create a FilterListIterator without providing an underlying iterator.
        // This simulates a common misuse or an incompletely configured iterator.
        final FilterListIterator<Object> emptyIterator = new FilterListIterator<>();

        // Act & Assert: Attempting to get the next element should throw a
        // NoSuchElementException, as hasNext() will be false. The assertion is
        // handled by the @Test(expected=...) annotation.
        emptyIterator.next();
    }
}