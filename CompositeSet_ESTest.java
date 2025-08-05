/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.collections4.set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.Before;
import org.junit.Test;

/**
 * Clean, understandable tests for {@link CompositeSet}.
 * This class was refactored from an auto-generated EvoSuite test suite.
 */
public class CompositeSetTest {

    private Set<String> setA;
    private Set<String> setB;
    private CompositeSet<String> compositeSet;

    @Before
    public void setUp() {
        setA = new HashSet<>();
        setB = new HashSet<>();
        compositeSet = new CompositeSet<>(setA, setB);
    }

    // --- Constructor and Initial State Tests ---

    @Test
    public void constructorWithNoSetsShouldBeEmpty() {
        CompositeSet<String> emptySet = new CompositeSet<>();
        assertTrue("A new CompositeSet should be empty", emptySet.isEmpty());
        assertEquals(0, emptySet.size());
    }

    @Test
    public void constructorWithSetsShouldContainTheirElements() {
        setA.add("a");
        setB.add("b");
        assertTrue("Should contain element from setA", compositeSet.contains("a"));
        assertTrue("Should contain element from setB", compositeSet.contains("b"));
        assertEquals("Size should be the sum of elements in contained sets", 2, compositeSet.size());
    }

    /**
     * Tests that creating a CompositeSet from a set that contains itself
     * results in a StackOverflowError. This is an edge case test for the
     * constructor's collision detection logic, which can lead to infinite recursion.
     */
    @Test(expected = StackOverflowError.class)
    public void constructorShouldThrowStackOverflowWhenSetContainsItself() {
        Set<Object> set = new HashSet<>();
        set.add(set); // The set now contains itself
        new CompositeSet<>(set);
    }

    // --- Modification Method Tests (add, addAll) ---

    @Test
    public void addShouldThrowUnsupportedOperationExceptionWhenNoMutatorIsSet() {
        try {
            compositeSet.add("new");
            fail("add() should throw UnsupportedOperationException without a mutator");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void addAllShouldThrowUnsupportedOperationExceptionWhenNoMutatorIsSet() {
        try {
            compositeSet.addAll(Collections.singletonList("new"));
            fail("addAll() should throw UnsupportedOperationException without a mutator");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void addShouldDelegateToMutatorAndReturnTrueOnSuccess() {
        // Arrange
        @SuppressWarnings("unchecked")
        CompositeSet.SetMutator<String> mutator = mock(CompositeSet.SetMutator.class);
        doReturn(true).when(mutator).add(any(CompositeSet.class), anyList(), any(String.class));
        compositeSet.setMutator(mutator);

        // Act
        boolean result = compositeSet.add("new");

        // Assert
        assertTrue("add should return true when mutator returns true", result);
        verify(mutator).add(compositeSet, compositeSet.getSets(), "new");
    }

    @Test
    public void addShouldDelegateToMutatorAndReturnFalseOnFailure() {
        // Arrange
        @SuppressWarnings("unchecked")
        CompositeSet.SetMutator<String> mutator = mock(CompositeSet.SetMutator.class);
        doReturn(false).when(mutator).add(any(CompositeSet.class), anyList(), any(String.class));
        compositeSet.setMutator(mutator);

        // Act
        boolean result = compositeSet.add("new");

        // Assert
        assertFalse("add should return false when mutator returns false", result);
        verify(mutator).add(compositeSet, compositeSet.getSets(), "new");
    }

    // --- Modification Method Tests (remove, removeAll, retainAll, clear) ---

    @Test
    public void removeShouldReturnTrueAndRemoveElementWhenElementExists() {
        setA.add("a");
        assertTrue("Element 'a' should be present before removal", compositeSet.contains("a"));

        boolean result = compositeSet.remove("a");

        assertTrue("remove should return true for an existing element", result);
        assertFalse("Element 'a' should be removed", compositeSet.contains("a"));
        assertFalse("Underlying setA should not contain 'a'", setA.contains("a"));
    }

    @Test
    public void removeShouldReturnFalseWhenElementDoesNotExist() {
        setA.add("a");
        boolean result = compositeSet.remove("nonexistent");
        assertFalse("remove should return false for a non-existing element", result);
        assertEquals("Set size should not change", 1, compositeSet.size());
    }

    @Test
    public void removeAllShouldRemoveAllSpecifiedElements() {
        setA.add("a");
        setA.add("x");
        setB.add("b");
        setB.add("y");

        boolean result = compositeSet.removeAll(Arrays.asList("x", "y", "z"));

        assertTrue("removeAll should return true as the set was modified", result);
        assertTrue("Should still contain 'a'", compositeSet.contains("a"));
        assertTrue("Should still contain 'b'", compositeSet.contains("b"));
        assertFalse("Should not contain 'x'", compositeSet.contains("x"));
        assertFalse("Should not contain 'y'", compositeSet.contains("y"));
        assertEquals("Size should be 2 after removal", 2, compositeSet.size());
    }

    @Test
    public void retainAllShouldKeepOnlySpecifiedElements() {
        setA.add("a");
        setA.add("x");
        setB.add("b");
        setB.add("y");

        boolean result = compositeSet.retainAll(Arrays.asList("a", "b", "z"));

        assertTrue("retainAll should return true as the set was modified", result);
        assertTrue("Should still contain 'a'", compositeSet.contains("a"));
        assertTrue("Should still contain 'b'", compositeSet.contains("b"));
        assertFalse("Should not contain 'x'", compositeSet.contains("x"));
        assertFalse("Should not contain 'y'", compositeSet.contains("y"));
        assertEquals("Size should be 2 after retainAll", 2, compositeSet.size());
    }

    @Test
    public void clearShouldRemoveAllElementsFromAllCompositedSets() {
        setA.add("a");
        setB.add("b");
        assertFalse("CompositeSet should not be empty before clear", compositeSet.isEmpty());

        compositeSet.clear();

        assertTrue("CompositeSet should be empty after clear", compositeSet.isEmpty());
        assertTrue("Underlying setA should be empty", setA.isEmpty());
        assertTrue("Underlying setB should be empty", setB.isEmpty());
    }

    @Test
    public void removeIfShouldRemoveMatchingElements() {
        setA.add("apple");
        setB.add("banana");
        setA.add("avocado");

        boolean result = compositeSet.removeIf(s -> s.startsWith("a"));

        assertTrue("removeIf should return true as the set was modified", result);
        assertFalse("Should not contain 'apple'", compositeSet.contains("apple"));
        assertFalse("Should not contain 'avocado'", compositeSet.contains("avocado"));
        assertTrue("Should still contain 'banana'", compositeSet.contains("banana"));
        assertEquals("Size should be 1 after removeIf", 1, compositeSet.size());
    }

    @Test
    public void removeIfShouldPropagateExceptionFromPredicate() {
        setA.add("a");
        Predicate<String> failingPredicate = s -> {
            throw new IllegalStateException("Predicate failed");
        };

        try {
            compositeSet.removeIf(failingPredicate);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("Predicate failed", e.getMessage());
        }
        assertTrue("Set should not be modified on predicate failure", compositeSet.contains("a"));
    }

    // --- Query Method Tests (contains, containsAll, isEmpty, size) ---

    @Test
    public void containsAllShouldReturnTrueIfAllElementsArePresent() {
        setA.add("a");
        setB.add("b");
        assertTrue(compositeSet.containsAll(Arrays.asList("a", "b")));
    }

    @Test
    public void containsAllShouldReturnFalseIfAnElementIsMissing() {
        setA.add("a");
        setB.add("b");
        assertFalse(compositeSet.containsAll(Arrays.asList("a", "c")));
    }

    @Test
    public void isEmptyShouldReturnFalseWhenAnySetHasElements() {
        setA.add("a");
        assertFalse(compositeSet.isEmpty());
    }

    // --- Iterator Tests ---

    @Test
    public void iteratorShouldIterateOverAllElements() {
        setA.add("a");
        setB.add("b");
        Set<String> foundElements = new HashSet<>();
        for (String s : compositeSet) {
            foundElements.add(s);
        }
        assertEquals(new HashSet<>(Arrays.asList("a", "b")), foundElements);
    }

    @Test
    public void iteratorRemoveShouldRemoveElementFromUnderlyingSet() {
        setA.add("a");
        setB.add("b");

        Iterator<String> it = compositeSet.iterator();
        while (it.hasNext()) {
            if ("a".equals(it.next())) {
                it.remove();
            }
        }

        assertFalse("Element 'a' should be removed", compositeSet.contains("a"));
        assertTrue("Element 'b' should remain", compositeSet.contains("b"));
        assertFalse("Underlying setA should not contain 'a'", setA.contains("a"));
    }

    // --- toArray Tests ---

    @Test
    public void toArrayShouldReturnArrayWithAllElements() {
        setA.add("a");
        setB.add("b");
        Object[] array = compositeSet.toArray();
        assertEquals(2, array.length);
        List<Object> list = Arrays.asList(array);
        assertTrue(list.contains("a"));
        assertTrue(list.contains("b"));
    }

    @Test
    public void toArrayWithSufficientlyLargeArrayShouldPopulateIt() {
        setA.add("a");
        setB.add("b");
        String[] array = new String[3];
        array[2] = "z"; // To check if it gets overwritten with null

        String[] result = compositeSet.toArray(array);

        assertEquals("Should return the same array instance", array, result);
        assertEquals("Third element should be set to null", null, result[2]);
        List<String> list = Arrays.asList(result);
        assertTrue(list.contains("a"));
        assertTrue(list.contains("b"));
    }

    @Test
    public void toArrayWithInsufficientArrayShouldReturnNewArray() {
        setA.add("a");
        setB.add("b");
        String[] array = new String[1];

        String[] result = compositeSet.toArray(array);

        assertFalse("Should return a new array instance", array == result);
        assertEquals(2, result.length);
        List<String> list = Arrays.asList(result);
        assertTrue(list.contains("a"));
        assertTrue(list.contains("b"));
    }

    @Test(expected = ArrayStoreException.class)
    public void toArrayWithIncompatibleTypeShouldThrowArrayStoreException() {
        setA.add("a");
        compositeSet.toArray(new Integer[1]);
    }

    // --- equals and hashCode Tests ---

    @Test
    public void equalsShouldBeTrueForSameSetOfElements() {
        setA.add("a");
        setB.add("b");

        Set<String> other = new HashSet<>(Arrays.asList("a", "b"));
        assertTrue("CompositeSet should be equal to a Set with the same elements", compositeSet.equals(other));
        assertTrue("The other Set should be equal to the CompositeSet", other.equals(compositeSet));
    }

    @Test
    public void hashCodeShouldBeEqualToEquivalentSetHashCode() {
        setA.add("a");
        setB.add("b");

        Set<String> other = new HashSet<>(Arrays.asList("a", "b"));
        assertEquals("Hash code should match an equivalent Set", other.hashCode(), compositeSet.hashCode());
    }

    // --- Composited Set Management Tests ---

    @Test
    public void addCompositedShouldThrowExceptionOnCollisionWhenNoMutatorIsSet() {
        setA.add("a");
        Set<String> setC = new HashSet<>();
        setC.add("a"); // Collision

        try {
            compositeSet.addComposited(setC);
            fail("addComposited should throw UnsupportedOperationException on collision without a mutator");
        } catch (UnsupportedOperationException e) {
            // Expected
            assertTrue(e.getMessage().contains("Collision"));
        }
    }

    @Test
    public void addCompositedShouldSucceedWithDisjointSets() {
        setA.add("a");
        Set<String> setC = new HashSet<>();
        setC.add("c");

        compositeSet.addComposited(setC);

        assertEquals("Size should be 2 after adding disjoint set", 2, compositeSet.size());
        assertTrue(compositeSet.contains("c"));
    }

    @Test
    public void removeCompositedShouldRemoveSetFromComposite() {
        setA.add("a");
        setB.add("b");

        compositeSet.removeComposited(setA);

        assertFalse("Element 'a' from removed set should not be present", compositeSet.contains("a"));
        assertTrue("Element 'b' from remaining set should be present", compositeSet.contains("b"));
        assertEquals("Size should be 1 after removing a composited set", 1, compositeSet.size());
    }

    @Test
    public void getSetsShouldReturnListOfCompositedSets() {
        List<Set<String>> sets = compositeSet.getSets();
        assertEquals(2, sets.size());
        assertTrue(sets.contains(setA));
        assertTrue(sets.contains(setB));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getSetsShouldReturnUnmodifiableList() {
        List<Set<String>> sets = compositeSet.getSets();
        sets.add(new HashSet<>()); // Should throw
    }

    @Test
    public void toSetShouldReturnAHashSetCopy() {
        setA.add("a");
        setB.add("b");

        Set<String> copy = compositeSet.toSet();

        assertEquals("Copy should have the same elements", compositeSet, copy);
        assertFalse("Copy should be a different instance", compositeSet == copy);

        // Modify the copy and check that the original is unchanged
        copy.remove("a");
        assertTrue("Original should be unchanged after modifying the copy", compositeSet.contains("a"));
    }
}