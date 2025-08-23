package org.apache.commons.collections4.iterators;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.TruePredicate;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link FilterListIterator}.
 * This class contains a test case from an auto-generated suite,
 * which has been improved for clarity.
 */
public class FilterListIterator_ESTestTest57 {

    /**
     * Tests that previousIndex() returns -1 for a new FilterListIterator
     * that has not been associated with an underlying iterator.
     */
    @Test
    public void previousIndexShouldReturnNegativeOneForNewIteratorWithoutUnderlyingIterator() {
        // Arrange: Create a FilterListIterator with a predicate but no underlying iterator.
        // The specific predicate does not matter for this test, so a simple one is used.
        final Predicate<Object> predicate = TruePredicate.truePredicate();
        final FilterListIterator<Object> filterListIterator = new FilterListIterator<>(predicate);

        // Act: Call the previousIndex() method.
        final int previousIndex = filterListIterator.previousIndex();

        // Assert: The index should be -1, which is the standard behavior for a
        // ListIterator positioned at the beginning of a list.
        assertEquals("A new iterator should have a previous index of -1", -1, previousIndex);
    }
}