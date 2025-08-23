package org.apache.commons.collections4.set;

import org.apache.commons.collections4.functors.UniquePredicate;
import org.junit.Test;

import java.util.function.Predicate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This class contains tests for the {@link CompositeSet} class, focusing on its behavior
 * with mutation operations.
 */
public class CompositeSetTest {

    /**
     * Tests that calling {@code removeIf()} on an empty CompositeSet
     * correctly returns false and leaves the set empty.
     */
    @Test
    public void removeIfOnEmptySetShouldReturnFalse() {
        // Arrange: Create an empty composite set and a predicate.
        // A CompositeSet is empty if it contains no underlying sets.
        final CompositeSet<Integer> emptySet = new CompositeSet<>();
        final Predicate<Object> predicate = new UniquePredicate<>();

        // Act: Call removeIf on the empty set.
        final boolean wasModified = emptySet.removeIf(predicate);

        // Assert: Verify that the method returned false and the set is still empty.
        assertFalse("removeIf should return false when called on an empty set.", wasModified);
        assertTrue("The set should remain empty after the removeIf operation.", emptySet.isEmpty());
    }
}