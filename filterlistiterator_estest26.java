package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Contains tests for the {@link FilterListIterator} class.
 */
public class FilterListIteratorTest {

    /**
     * Tests that calling next() on a FilterListIterator throws a NullPointerException
     * if the iterator was constructed without a predicate.
     */
    @Test(expected = NullPointerException.class)
    public void nextShouldThrowNullPointerExceptionWhenPredicateIsNull() {
        // Arrange: Create a list with one element to ensure the iterator has a next item.
        final List<Integer> sourceList = new LinkedList<>(Arrays.asList(100));
        final ListIterator<Integer> baseIterator = sourceList.listIterator();

        // Create a FilterListIterator using the constructor that does not set a predicate,
        // leaving the internal predicate field as null.
        final FilterListIterator<Integer> filterIterator = new FilterListIterator<>(baseIterator);

        // Act & Assert: Calling next() should trigger a NullPointerException
        // because the iterator attempts to use the null predicate.
        filterIterator.next();
    }
}