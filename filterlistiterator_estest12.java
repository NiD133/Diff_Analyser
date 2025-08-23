package org.apache.commons.collections4.iterators;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.NullPredicate;
import org.junit.Test;

import java.util.LinkedList;
import java.util.ListIterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link FilterListIterator}.
 * This improved test focuses on a specific scenario: iterating backward from the end of a list.
 */
public class FilterListIteratorTest {

    /**
     * Tests that when a {@link FilterListIterator} is positioned at the end of a list,
     * calling {@code previous()} correctly returns the last matching element and updates
     * the iterator's indices.
     */
    @Test
    public void testPreviousFromEndWithMatchingElement() {
        // Arrange
        final LinkedList<Object> list = new LinkedList<>();
        list.add("A");
        list.add(null); // The element that should be found by the predicate.
        list.add("B");

        // Use a predicate that only accepts null values.
        final Predicate<Object> nullOnlyPredicate = NullPredicate.nullPredicate();

        // Create a list iterator starting at the end of the list (index 3).
        final ListIterator<Object> underlyingIterator = list.listIterator(list.size());

        final FilterListIterator<Object> filterIterator =
                new FilterListIterator<>(underlyingIterator, nullOnlyPredicate);

        // Act & Assert

        // 1. Verify that the iterator reports it has a previous element that matches the predicate.
        assertTrue("hasPrevious() should be true as a matching 'null' element exists.", filterIterator.hasPrevious());

        // 2. Retrieve the previous element and verify it is the expected 'null'.
        final Object previousElement = filterIterator.previous();
        assertNull("previous() should return the matching 'null' element.", previousElement);

        // 3. After moving backward past the 'null' at index 1, the cursor should be positioned
        //    before it. The next element would be at index 1, and the previous index is 0.
        assertEquals("nextIndex() should be 1 after moving back to the 'null' element.", 1, filterIterator.nextIndex());
        assertEquals("previousIndex() should be 0.", 0, filterIterator.previousIndex());
    }
}