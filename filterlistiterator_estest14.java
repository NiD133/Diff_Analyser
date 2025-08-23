package org.apache.commons.collections4.iterators;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.NullPredicate;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests for {@link FilterListIterator} focusing on index management when moving backward after moving forward.
 */
public class FilterListIteratorTest {

    /**
     * Tests that the nextIndex and previousIndex are correctly updated after
     * calling next() and then previous().
     */
    @Test
    public void testPreviousAfterNextUpdatesIndicesCorrectly() {
        // Arrange: Create a list with a single null element, which our predicate will accept.
        final List<Integer> sourceList = new LinkedList<>(Collections.singletonList(null));
        final ListIterator<Integer> sourceIterator = sourceList.listIterator();
        final Predicate<Integer> nullPredicate = NullPredicate.nullPredicate();

        // The FilterListIterator is initialized with the predicate and the source iterator.
        final FilterListIterator<Integer> filterIterator = new FilterListIterator<>(sourceIterator, nullPredicate);

        // Assert initial state
        assertEquals("Initial nextIndex should be 0", 0, filterIterator.nextIndex());
        assertEquals("Initial previousIndex should be -1", -1, filterIterator.previousIndex());

        // Act 1: Advance the iterator forward past the 'null' element.
        final Integer nextElement = filterIterator.next();

        // Assert 1: Check the state after moving forward.
        assertNull("next() should have returned the null element", nextElement);
        assertEquals("nextIndex after next() should be 1", 1, filterIterator.nextIndex());
        assertEquals("previousIndex after next() should be 0", 0, filterIterator.previousIndex());

        // Act 2: Move the iterator backward over the same 'null' element.
        final Integer previousElement = filterIterator.previous();

        // Assert 2: Check that the iterator has returned to its initial state.
        assertNull("previous() should have returned the null element", previousElement);
        assertEquals("nextIndex after previous() should be 0", 0, filterIterator.nextIndex());
        assertEquals("previousIndex after previous() should be -1", -1, filterIterator.previousIndex());
    }
}