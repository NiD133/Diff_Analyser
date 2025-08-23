package org.apache.commons.collections4.set;

import org.apache.commons.collections4.set.CompositeSet.SetMutator;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * This class contains improved tests for CompositeSet, focusing on understandability.
 * The original test 'test25' was refactored into the following focused test cases.
 */
public class CompositeSet_ESTestTest26 extends CompositeSet_ESTest_scaffolding {

    /**
     * Tests that calling addAll() on a CompositeSet without a configured SetMutator
     * throws an UnsupportedOperationException.
     */
    @Test
    public void addAll_withoutMutator_shouldThrowUnsupportedOperationException() {
        // Arrange
        final CompositeSet<String> compositeSet = new CompositeSet<>();
        final List<String> itemsToAdd = Arrays.asList("a", "b", "c");
        final String expectedMessage = "addAll() is not supported on CompositeSet without a SetMutator strategy";

        // Act & Assert
        try {
            compositeSet.addAll(itemsToAdd);
            fail("Expected an UnsupportedOperationException to be thrown");
        } catch (final UnsupportedOperationException e) {
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    /**
     * Tests that the add() method returns false and does not modify the set
     * when the configured SetMutator is stubbed to return false.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void add_withMutatorReturningFalse_shouldNotAddElement() {
        // Arrange
        final SetMutator<Integer> mockMutator = mock(SetMutator.class);
        doReturn(false).when(mockMutator).add(any(CompositeSet.class), anyList(), any(Integer.class));

        final CompositeSet<Integer> compositeSet = new CompositeSet<>(new HashSet<>());
        compositeSet.setMutator(mockMutator);

        // Act
        final boolean result = compositeSet.add(100);

        // Assert
        assertFalse("add() should return false as specified by the mutator.", result);
        assertTrue("The set should remain empty.", compositeSet.isEmpty());
    }

    /**
     * Tests that basic operations on an empty CompositeSet that contains itself
     * (a recursive structure) behave correctly without causing infinite loops.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void operations_onEmptyRecursiveSet_shouldBehaveCorrectly() {
        // Arrange: Create a CompositeSet that contains itself as its only member.
        final Set<Integer>[] sets = (Set<Integer>[]) Array.newInstance(Set.class, 1);
        final CompositeSet<Integer> recursiveSet = new CompositeSet<>(sets);
        sets[0] = recursiveSet;

        // Act & Assert

        // 1. Test size()
        assertEquals("Size of an empty recursive set should be 0.", 0, recursiveSet.size());

        // 2. Test contains()
        assertFalse("contains() on an empty recursive set should return false.", recursiveSet.contains(123));

        // 3. Test remove()
        assertFalse("remove() on an empty recursive set should return false.", recursiveSet.remove(123));
    }

    /**
     * Tests that removing a non-existent element from a CompositeSet returns false.
     */
    @Test
    public void remove_nonExistentElement_shouldReturnFalse() {
        // Arrange
        final Set<String> set1 = new HashSet<>(Arrays.asList("a", "b"));
        final Set<String> set2 = new HashSet<>(Arrays.asList("c", "d"));
        final CompositeSet<String> compositeSet = new CompositeSet<>(set1, set2);

        // Act
        final boolean result = compositeSet.remove("e");

        // Assert
        assertFalse("Removing a non-existent element should return false.", result);
        assertEquals("The size of the set should not change.", 4, compositeSet.size());
    }
}