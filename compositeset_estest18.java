package org.apache.commons.collections4.set;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

import java.util.Collections;
import java.util.Set;

/**
 * Contains tests for the {@link CompositeSet#containsAll(java.util.Collection)} method.
 */
public class CompositeSetContainsAllTest {

    /**
     * Tests that containsAll() returns false when the CompositeSet is empty
     * but the collection being checked is not.
     */
    @Test
    public void containsAllShouldReturnFalseForEmptyCompositeSetAndNonEmptyCollection() {
        // Arrange
        // Create a composite set that is empty (it contains no underlying sets).
        final CompositeSet<Integer> emptyCompositeSet = new CompositeSet<>();
        
        // Create a non-empty collection to check against.
        final Set<Integer> nonEmptyCollection = Collections.singleton(-6);

        // Act
        // Check if the empty composite set contains all elements of the non-empty collection.
        final boolean result = emptyCompositeSet.containsAll(nonEmptyCollection);

        // Assert
        // The result must be false, as an empty set cannot contain any elements.
        assertFalse("An empty CompositeSet should not contain elements from a non-empty collection", result);
    }
}