package org.apache.commons.collections4.iterators;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.ExceptionPredicate;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test class has been refactored for clarity. The original test was auto-generated
 * and contained significant irrelevant code.
 */
public class FilterListIterator_ESTestTest13 extends FilterListIterator_ESTest_scaffolding {

    /**
     * Tests that FilterListIterator's hasNext() method propagates exceptions
     * thrown by the underlying predicate.
     */
    @Test
    public void hasNextThrowsExceptionWhenPredicateThrowsException() {
        // Arrange
        // Create a source list with one element for the iterator to process.
        final List<Integer> sourceList = Collections.singletonList(42);
        final ListIterator<Integer> sourceIterator = sourceList.listIterator();

        // Use a predicate that is designed to always throw a RuntimeException upon evaluation.
        final Predicate<Integer> exceptionPredicate = ExceptionPredicate.exceptionPredicate();
        final FilterListIterator<Integer> filteredIterator =
                new FilterListIterator<>(sourceIterator, exceptionPredicate);

        // Act & Assert
        // The hasNext() method will call the predicate on the first element.
        // We expect this to trigger the exception from the predicate.
        try {
            filteredIterator.hasNext();
            fail("A RuntimeException should have been thrown by the predicate.");
        } catch (final RuntimeException e) {
            // Verify that the caught exception is the one we expect from ExceptionPredicate.
            assertEquals("ExceptionPredicate invoked", e.getMessage());
        }
    }
}