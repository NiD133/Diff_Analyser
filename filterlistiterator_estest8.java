package org.apache.commons.collections4.iterators;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.NullPredicate;
import org.junit.Test;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link FilterListIterator} class, focusing on its
 * core filtering and exception-handling behaviors.
 */
public class FilterListIteratorUnderstandableTest {

    /**
     * Tests that the {@code previous()} method correctly finds and returns the
     * last preceding element that matches the given predicate.
     */
    @Test
    public void previous_whenElementMatches_returnsMatchingElement() {
        // Arrange
        // Create a list with a non-matching element (-1) and a matching one (null).
        LinkedList<Integer> sourceList = new LinkedList<>();
        sourceList.add(null);
        sourceList.add(-1);

        // The predicate will only accept null elements.
        Predicate<Integer> nullOnlyPredicate = NullPredicate.nullPredicate();

        // Create an iterator starting at the end of the list (index 2).
        // To test previous(), we'll start it after the 'null' at index 1.
        ListIterator<Integer> sourceIterator = sourceList.listIterator(1);

        FilterListIterator<Integer> filterIterator = new FilterListIterator<>(sourceIterator, nullOnlyPredicate);

        // Act & Assert
        // The iterator should find the 'null' at index 0 when looking backwards.
        assertTrue("hasPrevious() should be true as a null element exists before the cursor.", filterIterator.hasPrevious());

        // Retrieve the matching element.
        Integer previousElement = filterIterator.previous();
        assertNull("previous() should return the matching null element.", previousElement);

        // After moving past the null, there are no more preceding elements that match.
        assertFalse("hasPrevious() should now be false.", filterIterator.hasPrevious());
    }

    /**
     * Tests that calling {@code next()} on a FilterListIterator that has not been
     * configured with an underlying ListIterator throws a NoSuchElementException.
     * This is expected behavior as there is no source to iterate over.
     */
    @Test(expected = NoSuchElementException.class)
    public void next_whenNoUnderlyingIteratorIsSet_throwsNoSuchElementException() {
        // Arrange
        // Create a FilterListIterator without providing an underlying iterator.
        FilterListIterator<Object> emptyFilterIterator = new FilterListIterator<>();

        // Act
        // Calling next() should immediately fail because there is no source data.
        emptyFilterIterator.next();

        // Assert: The @Test(expected=...) annotation handles the exception assertion.
    }
}