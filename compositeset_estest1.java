package org.apache.commons.collections4.set;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.ExceptionPredicate;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link CompositeSet}.
 */
public class CompositeSetTest {

    /**
     * Tests that calling removeIf with a predicate that always throws an exception
     * correctly propagates that exception.
     */
    @Test
    public void testRemoveIfWithExceptionPredicateThrowsException() {
        // Arrange: Create a CompositeSet containing a single element.
        final Set<Integer> underlyingSet = new HashSet<>();
        underlyingSet.add(42);
        final CompositeSet<Integer> compositeSet = new CompositeSet<>(underlyingSet);

        // Use a predicate that is designed to always throw a RuntimeException.
        final Predicate<Integer> exceptionPredicate = ExceptionPredicate.exceptionPredicate();

        // Act & Assert
        try {
            compositeSet.removeIf(exceptionPredicate);
            fail("Expected a RuntimeException to be thrown by removeIf.");
        } catch (final RuntimeException e) {
            // Verify that the exception is the one thrown by ExceptionPredicate.
            assertEquals("ExceptionPredicate invoked", e.getMessage());
        }
    }
}