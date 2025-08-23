package org.apache.commons.collections4.iterators;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.NullPredicate;
import org.junit.Test;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link FilterListIterator}.
 * This refactored test focuses on a specific behavior from the original generated test.
 */
public class FilterListIteratorTest {

    /**
     * Tests that calling previous() on a new iterator throws a NoSuchElementException,
     * even if hasNext() has been called and returns true. The iterator's position
     * should not have advanced, so there is no previous element.
     */
    @Test(expected = NoSuchElementException.class)
    public void previousShouldThrowNoSuchElementExceptionWhenAtStart() {
        // Arrange: Create a list with an element that will pass the filter.
        // The predicate is configured to only accept null elements.
        final LinkedList<Integer> sourceList = new LinkedList<>();
        sourceList.add(null);
        sourceList.add(1); // Add an element that will be filtered out.

        final ListIterator<Integer> sourceIterator = sourceList.listIterator();
        final Predicate<Integer> acceptsNullPredicate = NullPredicate.nullPredicate();
        final FilterListIterator<Integer> filterIterator = new FilterListIterator<>(sourceIterator, acceptsNullPredicate);

        // Pre-condition check: Ensure the iterator has a next element but is still at the beginning.
        assertTrue("The iterator should have a next element ready.", filterIterator.hasNext());

        // Act: Attempt to retrieve the previous element when the iterator is at the start.
        // This is expected to throw a NoSuchElementException.
        filterIterator.previous();
    }
}