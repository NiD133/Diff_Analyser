package org.apache.commons.collections4.iterators;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.ExceptionPredicate;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

/**
 * Tests for the {@link FilterListIterator} to ensure it correctly handles
 * exceptions thrown by its underlying predicate.
 */
public class FilterListIteratorTest {

    /**
     * Verifies that an exception thrown by the predicate during a hasNext()
     * check is propagated to the caller.
     */
    @Test(expected = RuntimeException.class)
    public void hasNextShouldPropagateExceptionFromPredicate() {
        // Arrange
        // 1. Create a source list with one element. The element's value is irrelevant
        //    because the predicate will throw an exception regardless of the input.
        final List<Object> sourceList = Collections.singletonList("any-element");
        final ListIterator<Object> sourceIterator = sourceList.listIterator();

        // 2. Use a predicate that is designed to always throw a RuntimeException.
        final Predicate<Object> exceptionThrowingPredicate = ExceptionPredicate.exceptionPredicate();

        // 3. Create the FilterListIterator instance to be tested.
        final FilterListIterator<Object> filteredIterator =
                new FilterListIterator<>(sourceIterator, exceptionThrowingPredicate);

        // Act & Assert
        // Calling hasNext() forces the iterator to evaluate the predicate on the first element.
        // The test expects this call to throw the RuntimeException from the predicate.
        // The assertion is handled by the @Test(expected) annotation.
        filteredIterator.hasNext();
    }
}