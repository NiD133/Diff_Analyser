package org.apache.commons.collections4.iterators;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.NullPredicate;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

/**
 * Contains tests for {@link FilterListIterator}.
 * This class focuses on improving the understandability of an existing test case.
 */
public class FilterListIteratorImprovedTest {

    /**
     * Tests that the iterator correctly finds a matching element when searching
     * backwards with previous(), even if the iterator was initialized past the
     * matching element's position.
     */
    @Test
    public void testPreviousFindsElementBeforeInitialPosition() {
        // Arrange
        // Create a list where the element that matches the predicate is at the beginning.
        List<Integer> list = new LinkedList<>(Arrays.asList(null, 99));

        // The predicate will only accept null elements.
        Predicate<Integer> acceptsOnlyNull = NullPredicate.nullPredicate();

        // Create the FilterListIterator with the predicate.
        FilterListIterator<Integer> filterIterator = new FilterListIterator<>(acceptsOnlyNull);

        // Initialize the underlying iterator to start at index 1 (after the matching 'null' element).
        ListIterator<Integer> underlyingIterator = list.listIterator(1);
        filterIterator.setListIterator(underlyingIterator);

        // The initial state of hasNext() should be false, as there are no matching
        // elements from index 1 onwards. This call also moves the underlying iterator's
        // cursor to the end of the list.
        assertFalse("hasNext() should be false as no matching elements are forward", filterIterator.hasNext());

        // Act
        // Call previous() to search backwards. It should skip the element '99'
        // and find the 'null' at index 0.
        Integer previousElement = filterIterator.previous();

        // Assert
        // The element returned by previous() should be the matching 'null'.
        assertNull("previous() should have returned the null element", previousElement);

        // After previous() returns the element at index 0, the iterator's cursor
        // is positioned at the start of the list. Therefore, the next element
        // is at index 0, and there is no previous element.
        assertEquals("nextIndex() should be 0 after finding the first element", 0, filterIterator.nextIndex());
        assertEquals("previousIndex() should be -1 at the start of the list", -1, filterIterator.previousIndex());
    }
}