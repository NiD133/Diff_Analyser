package org.apache.commons.collections4.set;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * This class contains improved tests for CompositeSet, focusing on clarity and maintainability.
 * The original test was a single, complex method that has been broken down into focused,
 * easy-to-understand test cases.
 */
public class CompositeSet_ESTestTest24 {

    /**
     * Tests that the addAll method delegates to the configured SetMutator and returns its result.
     */
    @Test
    public void addAll_should_delegateToMutatorAndReturnItsResult() {
        // Arrange
        final CompositeSet<Integer> compositeSet = new CompositeSet<>();
        final Collection<Integer> collectionToAdd = new ArrayList<>(); // An empty collection

        @SuppressWarnings("unchecked")
        final CompositeSet.SetMutator<Integer> mockMutator = mock(CompositeSet.SetMutator.class);

        // Configure the mutator to report that the collection was changed (returns true)
        when(mockMutator.addAll(any(CompositeSet.class), anyList(), anyCollection())).thenReturn(true);
        compositeSet.setMutator(mockMutator);

        // Act
        final boolean result = compositeSet.addAll(collectionToAdd);

        // Assert
        assertTrue("addAll should return true as dictated by the mutator", result);
        assertTrue("The set should remain empty as an empty collection was added", compositeSet.isEmpty());
        verify(mockMutator, times(1)).addAll(compositeSet, compositeSet.getSets(), collectionToAdd);
    }

    /**
     * Tests that calling removeIf on an empty CompositeSet returns false and the set remains empty.
     */
    @Test
    public void removeIf_should_returnFalse_when_setIsEmpty() {
        // Arrange
        final CompositeSet<Integer> emptySet = new CompositeSet<>();
        final Predicate<Integer> anyPredicate = e -> true;

        // Act
        final boolean result = emptySet.removeIf(anyPredicate);

        // Assert
        assertFalse("removeIf should return false for an empty set", result);
        assertTrue("The set should remain empty after the operation", emptySet.isEmpty());
    }

    /**
     * Tests that calling toArray with a pre-sized array on an empty CompositeSet returns the same array instance.
     */
    @Test
    public void toArray_should_returnProvidedArrayInstance_when_setIsEmpty() {
        // Arrange
        final CompositeSet<Integer> emptySet = new CompositeSet<>();
        final Integer[] emptyArray = new Integer[0];

        // Act
        final Integer[] result = emptySet.toArray(emptyArray);

        // Assert
        assertSame("For an empty set, toArray(T[]) should return the given array instance", emptyArray, result);
    }

    /**
     * Tests that adding and subsequently removing a set via addComposited and removeComposited
     * correctly modifies the internal list of sets.
     */
    @Test
    public void addAndRemoveComposited_should_updateInternalSetList() {
        // Arrange
        final CompositeSet<String> compositeSet = new CompositeSet<>();
        final Set<String> setComponent = new HashSet<>();
        assertTrue("The composite set should be initially empty", compositeSet.getSets().isEmpty());

        // Act (Add)
        compositeSet.addComposited(setComponent);

        // Assert (Add)
        assertEquals("The composite set should contain one set after addComposited", 1, compositeSet.getSets().size());
        assertTrue("The list of sets should contain the added set", compositeSet.getSets().contains(setComponent));

        // Act (Remove)
        compositeSet.removeComposited(setComponent);

        // Assert (Remove)
        assertTrue("The composite set should be empty after removing the composited set", compositeSet.getSets().isEmpty());
    }
}