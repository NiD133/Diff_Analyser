package org.apache.commons.collections4.set;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * This test class focuses on the behavior of the CompositeSet.
 * The original test class name "CompositeSet_ESTestTest17" suggests it was
 * auto-generated, so it has been renamed for clarity.
 */
public class CompositeSetTest {

    /**
     * Tests that containsAll() returns false when the CompositeSet is effectively empty
     * but is checked against a non-empty collection.
     */
    @Test
    public void containsAllShouldReturnFalseWhenCompositeSetIsEmptyAndArgumentIsNotEmpty() {
        // Arrange
        // Create a composite set that contains a single, empty set.
        final CompositeSet<Integer> compositeSet = new CompositeSet<>();
        compositeSet.addComposited(new HashSet<>());

        // Create a non-empty collection to check against.
        final Set<Integer> nonEmptyCollection = Collections.singleton(512);

        // Act
        // Check if the empty composite set contains all elements of the non-empty collection.
        final boolean result = compositeSet.containsAll(nonEmptyCollection);

        // Assert
        // The result should be false, as the composite set contains no elements.
        assertFalse("An empty CompositeSet should not 'contain all' elements from a non-empty collection", result);
    }
}