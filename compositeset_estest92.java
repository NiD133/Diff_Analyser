package org.apache.commons.collections4.set;

import org.junit.Test;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Improved, understandable tests for the CompositeSet class, focusing on the behavior of empty sets.
 * This refactors an auto-generated test into focused, well-documented test cases.
 */
public class CompositeSetImprovedTest {

    @Test
    public void constructorWithNullArrayShouldCreateEmptySet() {
        // Arrange & Act
        // The constructor should handle a null array of sets gracefully.
        CompositeSet<Integer> compositeSet = new CompositeSet<>((Set<Integer>[]) null);

        // Assert
        assertTrue("A CompositeSet created with a null array should be empty.", compositeSet.isEmpty());
        assertEquals("An empty CompositeSet should have a size of 0.", 0, compositeSet.size());
    }

    @Test
    public void emptyCompositeSetsShouldBeEqualAndHaveSameHashCode() {
        // Arrange
        CompositeSet<Integer> set1 = new CompositeSet<>();
        CompositeSet<Integer> set2 = new CompositeSet<>(set1); // Create a copy

        // Assert
        assertEquals("An empty CompositeSet should be equal to a copy of itself.", set2, set1);
        assertEquals("Equal CompositeSets should have the same hash code.", set2.hashCode(), set1.hashCode());
    }

    @Test
    public void retainAllWithEmptyCollectionOnEmptySetShouldReturnFalse() {
        // Arrange
        CompositeSet<Integer> set = new CompositeSet<>();
        CompositeSet<Integer> emptyCollection = new CompositeSet<>();

        // Act
        // retainAll should return false as the set is not modified.
        boolean wasModified = set.retainAll(emptyCollection);

        // Assert
        assertFalse("retainAll should return false when the set is not modified.", wasModified);
        assertTrue("The set should remain empty after the retainAll operation.", set.isEmpty());
    }

    @Test
    public void containsAllWithEmptyCollectionOnEmptySetShouldReturnTrue() {
        // Arrange
        CompositeSet<Integer> set = new CompositeSet<>();
        CompositeSet<Integer> emptyCollection = new CompositeSet<>();

        // Act
        // An empty collection is considered a subset of any collection.
        boolean result = set.containsAll(emptyCollection);

        // Assert
        assertTrue("containsAll with an empty collection should always return true.", result);
    }

    @Test
    public void addCompositedWithEmptySetShouldNotChangeOverallSize() {
        // Arrange
        CompositeSet<Integer> compositeSet = new CompositeSet<>();
        Set<Integer> emptySetToAdd = new HashSet<>();

        // Act
        compositeSet.addComposited(emptySetToAdd);

        // Assert
        assertEquals("Adding an empty set should not change the total element count.", 0, compositeSet.size());
        assertEquals("The number of composited sets should increase by one.", 1, compositeSet.getSets().size());
        assertTrue("The list of sets should contain the newly added set.", compositeSet.getSets().contains(emptySetToAdd));
    }

    @Test
    @SuppressWarnings("unchecked") // For mock creation with a generic type
    public void setMutatorShouldBeRetrievableWithGetMutator() {
        // Arrange
        CompositeSet<Integer> compositeSet = new CompositeSet<>();
        CompositeSet.SetMutator<Integer> mockMutator = mock(CompositeSet.SetMutator.class);

        // Act
        compositeSet.setMutator(mockMutator);
        CompositeSet.SetMutator<Integer> retrievedMutator = compositeSet.getMutator();

        // Assert
        assertSame("getMutator should return the same instance set by setMutator.", mockMutator, retrievedMutator);
    }
}