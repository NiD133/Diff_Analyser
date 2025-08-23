package org.apache.commons.collections4.iterators;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.NullPredicate;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

/**
 * Improved test for the FilterListIterator class.
 * This version focuses on clarity and maintainability.
 */
public class FilterListIteratorImprovedTest {

    /**
     * Tests that calling previous() throws a NoSuchElementException when there are
     * no more preceding elements that match the predicate.
     */
    @Test
    public void previousThrowsExceptionWhenNoMoreMatchingPreviousElements() {
        // Arrange: Set up the test scenario.
        // 1. A list containing a single 'null' element.
        final LinkedList<Integer> list = new LinkedList<>(Collections.singletonList(null));

        // 2. A predicate that only accepts 'null' values.
        final Predicate<Integer> predicate = NullPredicate.nullPredicate();

        // 3. A FilterListIterator wrapping an iterator positioned at the end of the list.
        final ListIterator<Integer> baseIterator = list.listIterator(1);
        final FilterListIterator<Integer> filterListIterator = new FilterListIterator<>(baseIterator, predicate);

        // Act & Assert: Perform the actions and verify the results.

        // The first call to previous() should successfully find and return the 'null' element.
        assertEquals("The first call to previous() should return the matching element.",
                     null, filterListIterator.previous());

        // After consuming the only matching element, hasPrevious() should now be false.
        assertFalse("hasPrevious() should be false after the only matching element is consumed.",
                    filterListIterator.hasPrevious());

        // A subsequent call to previous() should throw an exception because no more
        // matching elements exist.
        try {
            filterListIterator.previous();
            fail("A NoSuchElementException should have been thrown.");
        } catch (final NoSuchElementException e) {
            // This is the expected behavior.
        }
    }
}