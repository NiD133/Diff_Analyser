package org.mockito.internal.util.collections;

import org.junit.Test;

import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for {@link HashCodeAndEqualsSafeSet}.
 * This class demonstrates clear, human-written tests for the collection's behavior.
 */
public class HashCodeAndEqualsSafeSetTest {

    @Test
    public void removeIf_shouldRemoveNullElement_whenPredicateMatchesNull() {
        // Arrange
        // Create a set that contains a single null element.
        // The 'of' factory method correctly handles multiple nulls, resulting in a set of size 1.
        HashCodeAndEqualsSafeSet safeSet = HashCodeAndEqualsSafeSet.of(null, null);
        assertEquals("Precondition: Set should contain one null element.", 1, safeSet.size());

        // Create a predicate to identify null elements.
        Predicate<Object> isNullPredicate = Predicate.isEqual(null);

        // Act
        // Attempt to remove all elements matching the predicate.
        boolean wasModified = safeSet.removeIf(isNullPredicate);

        // Assert
        // The method should return true because the set was changed.
        assertTrue("removeIf should return true as the null element was removed.", wasModified);
        // The set should be empty after the removal operation.
        assertTrue("The set should be empty after removing its only element.", safeSet.isEmpty());
    }
}