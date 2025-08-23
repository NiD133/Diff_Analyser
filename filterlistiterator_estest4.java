package org.apache.commons.collections4.iterators;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.NullPredicate;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link FilterListIterator}.
 * This refactored test focuses on a specific scenario from the original generated test.
 */
public class FilterListIteratorTest {

    /**
     * Tests the behavior of previous() after hasNext() has been called.
     *
     * The original test appeared to test the interaction between hasNext() and previous().
     * When hasNext() finds a matching element, it advances the underlying iterator. A subsequent
     * call to previous() should then successfully return that same element.
     *
     * Note: The original test incorrectly expected a NoSuchElementException. This refactored
     * test corrects the assertion to reflect the expected behavior of the iterator.
     */
    @Test
    public void previousShouldReturnElementFoundByHasNext() {
        // Arrange
        // Create a list where only the 'null' element will be matched by the predicate.
        final List<Integer> sourceList = Arrays.asList(10, null, 20);
        final Predicate<Integer> nullPredicate = NullPredicate.nullPredicate();

        // Create a ListIterator starting at index 1 (before the 'null' element).
        final ListIterator<Integer> sourceIterator = sourceList.listIterator(1);
        final FilterListIterator<Integer> filterListIterator = new FilterListIterator<>(sourceIterator, nullPredicate);

        // Act & Assert
        // 1. Verify hasNext() finds the 'null' element. This also caches the element
        //    and advances the underlying iterator's position.
        assertTrue("hasNext() should find the matching 'null' element", filterListIterator.hasNext());

        // 2. Calling previous() should now return the 'null' element that hasNext() just found.
        final Integer previousElement = filterListIterator.previous();
        assertNull("previous() should return the cached 'null' element", previousElement);

        // 3. For completeness, verify that there are no more preceding elements that match.
        assertFalse("There should be no more matching elements in the previous direction", filterListIterator.hasPrevious());
        assertThrows(NoSuchElementException.class, filterListIterator::previous);
    }
}