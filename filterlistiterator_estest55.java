package org.apache.commons.collections4.iterators;

import org.apache.commons.collections4.functors.TruePredicate;
import org.junit.Test;

import java.util.Collections;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Contains tests for the {@link FilterListIterator} class.
 * This test case focuses on the behavior of the iterator when it is empty.
 */
public class FilterListIteratorTest {

    /**
     * Verifies that calling next() on a FilterListIterator that wraps an empty
     * underlying iterator correctly throws a NoSuchElementException.
     *
     * The original auto-generated test had a confusing and flawed setup that would
     * have resulted in a NullPointerException, not the expected NoSuchElementException.
     * This version clarifies the intent by using a standard empty iterator.
     */
    @Test(expected = NoSuchElementException.class)
    public void nextShouldThrowNoSuchElementExceptionWhenUnderlyingIteratorIsEmpty() {
        // Arrange: Create a filter iterator with a valid but empty underlying iterator.
        // The predicate doesn't matter here, as there are no elements to test.
        final ListIterator<Object> emptyIterator = Collections.emptyList().listIterator();
        final FilterListIterator<Object> filterIterator =
                new FilterListIterator<>(emptyIterator, TruePredicate.truePredicate());

        // Act & Assert: Calling next() is expected to throw the exception.
        filterIterator.next();
    }
}