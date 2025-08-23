package org.apache.commons.collections4.iterators;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.ExceptionPredicate;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link FilterListIterator} class, focusing on exception handling.
 */
public class FilterListIteratorTest {

    /**
     * Tests that hasPrevious() propagates the RuntimeException thrown by the predicate.
     *
     * The hasPrevious() method needs to evaluate the previous element to see if it
     * matches the predicate. This test ensures that if the predicate throws an
     * exception during this evaluation, the exception is not caught but is instead
     * passed up to the caller.
     */
    @Test
    public void testHasPreviousPropagatesExceptionFromPredicate() {
        // Arrange: Create a list with one element and an iterator positioned at the end.
        // This ensures that the underlying iterator's hasPrevious() returns true.
        final List<String> sourceList = new LinkedList<>();
        sourceList.add("A");
        final ListIterator<String> baseIterator = sourceList.listIterator(1);

        // Arrange: Use a predicate that is guaranteed to throw an exception.
        final Predicate<String> exceptionPredicate = ExceptionPredicate.exceptionPredicate();
        final FilterListIterator<String> filteredIterator = new FilterListIterator<>(baseIterator, exceptionPredicate);

        // Act & Assert
        try {
            filteredIterator.hasPrevious();
            fail("A RuntimeException should have been thrown by the predicate.");
        } catch (final RuntimeException e) {
            // Assert: Verify that the correct exception was propagated.
            final String expectedMessage = "ExceptionPredicate invoked";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}