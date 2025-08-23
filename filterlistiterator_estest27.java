package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertThrows;

/**
 * Contains tests for {@link FilterListIterator}.
 * This class focuses on improving the understandability of an auto-generated test case.
 */
public class FilterListIteratorTest {

    /**
     * Verifies that calling next() on a FilterListIterator throws a NullPointerException
     * if the iterator was created without a predicate.
     */
    @Test
    public void testNextThrowsNullPointerExceptionWhenPredicateIsNull() {
        // Arrange: Create a source list and its iterator.
        // The list must not be empty, so the iterator has an element to evaluate.
        List<Integer> sourceList = new LinkedList<>(Arrays.asList(100));
        ListIterator<Integer> sourceIterator = sourceList.listIterator();

        // Instantiate FilterListIterator without providing a predicate.
        // This leaves the internal 'predicate' field as null.
        FilterListIterator<Integer> filterListIterator = new FilterListIterator<>(sourceIterator);

        // Act & Assert: Expect a NullPointerException when next() is called.
        // The iterator will try to invoke predicate.evaluate(), which fails.
        assertThrows(NullPointerException.class, () -> {
            filterListIterator.next();
        });
    }

    /**
     * This is an alternative implementation using a try-catch block, which can be
     * more readable for developers less familiar with JUnit 5's assertThrows.
     */
    @Test
    public void testNextThrowsNullPointerExceptionWhenPredicateIsNull_withTryCatch() {
        // Arrange
        List<Integer> sourceList = new LinkedList<>(Arrays.asList(100));
        ListIterator<Integer> sourceIterator = sourceList.listIterator();
        FilterListIterator<Integer> filterListIterator = new FilterListIterator<>(sourceIterator);

        // Act & Assert
        try {
            filterListIterator.next();
            fail("Expected a NullPointerException because no predicate was set, but no exception was thrown.");
        } catch (final NullPointerException e) {
            // This is the expected behavior, so the test passes.
        }
    }
}