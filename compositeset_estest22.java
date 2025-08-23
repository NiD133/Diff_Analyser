package org.apache.commons.collections4.set;

import org.apache.commons.collections4.set.CompositeSet.SetMutator;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * This class contains improved, more understandable tests for the {@link CompositeSet} class.
 * Each test is focused on a single behavior and follows the Arrange-Act-Assert pattern.
 */
public class CompositeSetTest {

    @Test
    public void sizeOfEmptyRecursiveSetShouldBeZero() {
        // Arrange: Create a CompositeSet that contains itself.
        // This is an edge case to test for potential infinite loops during traversal.
        @SuppressWarnings("unchecked")
        Set<Integer>[] setArray = (Set<Integer>[]) new Set[1];
        CompositeSet<Integer> compositeSet = new CompositeSet<>(setArray);
        setArray[0] = compositeSet; // The set now contains itself.

        // Act
        int size = compositeSet.size();

        // Assert
        assertEquals("Size of an empty set containing itself should be 0", 0, size);
    }

    @Test
    public void containsOnEmptyRecursiveSetShouldReturnFalse() {
        // Arrange: Create a CompositeSet that contains itself.
        @SuppressWarnings("unchecked")
        Set<Integer>[] setArray = (Set<Integer>[]) new Set[1];
        CompositeSet<Integer> compositeSet = new CompositeSet<>(setArray);
        setArray[0] = compositeSet; // The set now contains itself.

        // Act
        boolean result = compositeSet.contains(123);

        // Assert
        assertFalse("contains() should return false for any element in an empty recursive set", result);
    }

    @Test
    public void addShouldReturnTrueWhenMutatorAllowsIt() {
        // Arrange
        CompositeSet<Integer> compositeSet = new CompositeSet<>();
        @SuppressWarnings("unchecked")
        SetMutator<Integer> mockMutator = mock(SetMutator.class);

        // Configure the mock mutator to allow additions by returning true.
        doReturn(true).when(mockMutator).add(any(CompositeSet.class), anyList(), anyInt());
        compositeSet.setMutator(mockMutator);

        // Act
        boolean result = compositeSet.add(100);

        // Assert
        assertTrue("add() should return true as specified by the mutator", result);
        verify(mockMutator).add(eq(compositeSet), anyList(), eq(100));
    }

    @Test
    public void removeShouldReturnFalseForNonExistentElement() {
        // Arrange
        Set<String> innerSet = new HashSet<>();
        innerSet.add("existing");
        CompositeSet<String> compositeSet = new CompositeSet<>(innerSet);

        // Act
        boolean result = compositeSet.remove("non-existent");

        // Assert
        assertFalse("remove() should return false if the element is not in any of the composited sets", result);
    }

    @Test
    public void toArrayOnEmptySetShouldReturnEmptyArray() {
        // Arrange
        CompositeSet<Object> compositeSet = new CompositeSet<>();

        // Act
        Object[] array = compositeSet.toArray();

        // Assert
        assertNotNull("toArray() should not return null", array);
        assertEquals("toArray() on an empty set should produce an empty array", 0, array.length);
    }

    @Test
    public void getMutatorShouldReturnNullWhenNotSet() {
        // Arrange
        CompositeSet<Integer> compositeSet = new CompositeSet<>();

        // Act
        SetMutator<Integer> mutator = compositeSet.getMutator();

        // Assert
        assertNull("getMutator() should return null by default", mutator);
    }

    @Test
    public void constructorWithAnotherCompositeSetShouldCreateEqualCopy() {
        // Arrange
        Set<String> innerSet = new HashSet<>();
        innerSet.add("A");
        CompositeSet<String> originalSet = new CompositeSet<>(innerSet);

        // Act
        // Create a new CompositeSet from the original one.
        CompositeSet<String> copiedSet = new CompositeSet<>(originalSet);

        // Assert
        assertEquals("The copied set should be equal to the original", originalSet, copiedSet);
        assertNotSame("The copied set should be a different instance from the original", originalSet, copiedSet);
    }

    @Test
    public void addCompositedWithSelfOnEmptySetShouldSucceed() {
        // Arrange
        CompositeSet<Integer> compositeSet = new CompositeSet<>();

        // Act
        compositeSet.addComposited(compositeSet);

        // Assert
        assertEquals("The composite set should now contain one set (itself)", 1, compositeSet.getSets().size());
        assertTrue("The contained set should be the instance itself", compositeSet.getSets().contains(compositeSet));
        assertTrue("The set should remain empty after the operation", compositeSet.isEmpty());
    }
}