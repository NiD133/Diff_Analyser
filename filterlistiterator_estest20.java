package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import static org.junit.Assert.fail;

/**
 * This class contains improved tests for FilterListIterator.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class FilterListIteratorImprovedTest {

    /**
     * Tests that calling hasNext() on a FilterListIterator throws a NullPointerException
     * if a predicate has not been set.
     */
    @Test
    public void hasNextShouldThrowNullPointerExceptionWhenPredicateNotSet() {
        // Arrange
        // Create a source list and its iterator. The content is not important,
        // as the exception occurs before any element is evaluated.
        final List<Integer> sourceList = new LinkedList<>(Arrays.asList(100, 200));
        final ListIterator<Integer> underlyingIterator = sourceList.listIterator();

        // Create a FilterListIterator and provide it with an iterator,
        // but intentionally do not set a predicate.
        final FilterListIterator<Integer> filterListIterator = new FilterListIterator<>();
        filterListIterator.setListIterator(underlyingIterator);

        // Act & Assert
        try {
            // Calling hasNext() should fail because it needs a predicate to evaluate the elements.
            filterListIterator.hasNext();
            fail("Expected a NullPointerException because the predicate was not set.");
        } catch (final NullPointerException e) {
            // This is the expected and correct behavior.
        }
    }
}