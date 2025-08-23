package org.apache.commons.collections4.iterators;

import org.apache.commons.collections4.functors.TruePredicate;
import org.junit.Test;

import java.util.Collections;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Contains tests for the {@link FilterListIterator} class.
 */
public class FilterListIteratorTest {

    /**
     * Tests that calling previous() on an empty FilterListIterator throws a NoSuchElementException.
     * An iterator can be empty either because its underlying iterator is empty or because
     * all elements have been filtered out. This test covers the former case.
     */
    @Test(expected = NoSuchElementException.class)
    public void previousOnEmptyIteratorShouldThrowNoSuchElementException() {
        // Arrange: Create a FilterListIterator with an empty backing iterator.
        // The predicate's logic is irrelevant here, so we use one that always returns true.
        ListIterator<Object> emptyBackingIterator = Collections.emptyList().listIterator();
        FilterListIterator<Object> filterIterator =
                new FilterListIterator<>(emptyBackingIterator, TruePredicate.truePredicate());

        // Act & Assert: Calling previous() on an iterator with no previous elements
        // is expected to throw a NoSuchElementException.
        filterIterator.previous();
    }
}