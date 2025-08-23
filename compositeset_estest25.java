package org.apache.commons.collections4.set;

import org.junit.Test;

import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Refactored tests for the CompositeSet class, focusing on understandability.
 *
 * The original test combined multiple concerns, including recursive composition,
 * mutator behavior, and operations on composites of empty sets. This version
 * separates those concerns into distinct, clearly named test cases.
 */
public class CompositeSetRefactoredTest {

    /**
     * Tests the behavior of a CompositeSet that contains itself.
     * Operations like size(), contains(), and equals() should handle this
     * recursive structure gracefully without causing a StackOverflowError.
     */
    @Test
    public void testRecursiveCompositionDoesNotCauseInfiniteLoop() {
        // Arrange: Create a CompositeSet that contains itself as a component.
        @SuppressWarnings("unchecked")
        final Set<Integer>[] setArray = (Set<Integer>[]) Array.newInstance(Set.class, 1);
        final CompositeSet<Integer> recursiveSet = new CompositeSet<>(setArray);
        setArray[0] = recursiveSet; // The recursive link

        // Arrange: Create another identical recursive set for equality comparison.
        final CompositeSet<Integer> anotherRecursiveSet = new CompositeSet<>(setArray);

        // Assert
        assertEquals("Size of a recursively empty set should be 0", 0, recursiveSet.size());
        assertFalse("A recursively empty set should not contain any element", recursiveSet.contains(123));
        assertFalse("Removing a non-existent element should return false", recursiveSet.remove(123));
        assertEquals("Two identically structured recursive sets should be equal", recursiveSet, anotherRecursiveSet);
    }

    /**
     * Tests that the add() method correctly delegates to the configured SetMutator
     * and returns its result. The test also verifies that the underlying set's
     * size is not affected, as the mock mutator does not perform a real addition.
     */
    @Test
    public void testAddDelegatesToMutatorAndReturnsItsResult() {
        // Arrange: Create a CompositeSet and a mock SetMutator.
        final CompositeSet<Integer> compositeSet = new CompositeSet<>();
        @SuppressWarnings("unchecked")
        final CompositeSet.SetMutator<Integer> mockMutator = mock(CompositeSet.SetMutator.class);

        // Configure the mock mutator to return true for any add operation.
        when(mockMutator.add(any(CompositeSet.class), anyList(), anyInt())).thenReturn(true);
        compositeSet.setMutator(mockMutator);

        final Integer elementToAdd = 42;

        // Act: Call the add method, which should trigger the mutator.
        final boolean result = compositeSet.add(elementToAdd);

        // Assert
        assertTrue("add() should return the boolean result from the mutator", result);
        verify(mockMutator).add(eq(compositeSet), anyList(), eq(elementToAdd));
        assertEquals("Size should remain 0 as the mock mutator doesn't actually add elements", 0, compositeSet.size());
    }

    /**
     * Tests that toSet() on a CompositeSet composed of multiple empty or null sets
     * correctly returns a single, empty Set.
     */
    @Test
    public void testToSetOnCompositeOfEmptyAndNullSetsReturnsEmptySet() {
        // Arrange: Create an array of Sets containing empty sets and nulls.
        @SuppressWarnings("unchecked")
        final Set<String>[] sets = (Set<String>[]) Array.newInstance(Set.class, 5);
        sets[0] = new HashSet<>();
        sets[1] = null; // CompositeSet should handle nulls gracefully
        sets[2] = new HashSet<>();
        sets[3] = null;
        sets[4] = new HashSet<>();

        final CompositeSet<String> compositeOfEmptySets = new CompositeSet<>(sets);

        // Act: Convert the composite set to a single Set.
        final Set<String> resultSet = compositeOfEmptySets.toSet();

        // Assert
        assertTrue("The resulting set from a composite of empty sets should be empty", resultSet.isEmpty());
    }
}