package org.apache.commons.collections4.set;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Contains improved, more understandable tests for the CompositeSet class,
 * focusing on specific behaviors like initialization, modification, and iteration.
 */
public class CompositeSetImprovedTest {

    /**
     * Tests the state of a newly created empty CompositeSet.
     * An empty CompositeSet should report that it is empty, have a size of zero,
     * and produce an empty array or Set representation.
     */
    @Test
    public void testEmptyCompositeSetBehavior() {
        // Arrange
        CompositeSet<String> emptySet = new CompositeSet<>();

        // Act & Assert
        assertTrue("A new composite set should be empty", emptySet.isEmpty());
        assertEquals("An empty composite set should have size 0", 0, emptySet.size());
        assertArrayEquals("toArray() on an empty set should return an empty array",
                          new Object[0], emptySet.toArray());
        assertTrue("toSet() on an empty set should return an empty set",
                   emptySet.toSet().isEmpty());
    }

    /**
     * Tests that a CompositeSet correctly reflects changes made to its underlying component sets.
     * A CompositeSet is a view over its components, so modifications to a component
     * should be visible through the composite.
     */
    @Test
    public void testCompositeSetReflectsComponentSetChanges() {
        // Arrange
        Set<Integer> componentSet = new HashSet<>();
        CompositeSet<Integer> compositeSet = new CompositeSet<>(componentSet);

        assertTrue("Initially, the composite set should be empty", compositeSet.isEmpty());

        // Act: Modify the underlying component set
        componentSet.add(100);

        // Assert: The change is reflected in the composite set
        assertFalse("Composite set should not be empty after adding to component", compositeSet.isEmpty());
        assertEquals("Composite set size should reflect component's size", 1, compositeSet.size());
        assertTrue("Composite set should contain the element added to its component", compositeSet.contains(100));
    }

    /**
     * Verifies that calling clear() on a CompositeSet removes all elements
     * from all of its underlying component sets.
     */
    @Test
    public void testClearEmptiesAllComponentSets() {
        // Arrange
        Set<String> componentSet1 = new HashSet<>(Arrays.asList("a", "b"));
        Set<String> componentSet2 = new HashSet<>(Arrays.asList("c", "d"));
        CompositeSet<String> compositeSet = new CompositeSet<>(componentSet1, componentSet2);

        assertFalse("Composite set should not be empty before clear()", compositeSet.isEmpty());

        // Act
        compositeSet.clear();

        // Assert
        assertTrue("Composite set should be empty after clear()", compositeSet.isEmpty());
        assertTrue("The first component set should be cleared", componentSet1.isEmpty());
        assertTrue("The second component set should be cleared", componentSet2.isEmpty());
    }

    /**
     * Tests that the iterator of a CompositeSet correctly traverses all elements
     * from all of its component sets.
     */
    @Test
    public void testIteratorCombinesAllComponentSets() {
        // Arrange
        Set<String> componentSet1 = new HashSet<>(Arrays.asList("a", "b"));
        Set<String> componentSet2 = new HashSet<>(Arrays.asList("c", "d"));
        CompositeSet<String> compositeSet = new CompositeSet<>(componentSet1, componentSet2);

        // Act: Collect all elements from the iterator into a new Set
        Set<String> iteratedElements = StreamSupport.stream(compositeSet.spliterator(), false)
                                                    .collect(Collectors.toSet());

        // Assert
        Set<String> expectedElements = new HashSet<>(Arrays.asList("a", "b", "c", "d"));
        assertEquals("Iterator should provide all elements from all components",
                     expectedElements, iteratedElements);
        assertEquals("Composite set size should be the sum of component sizes", 4, compositeSet.size());
    }

    /**
     * Tests that removeIf() correctly removes elements that match the predicate
     * from the composite set and its underlying components.
     */
    @Test
    public void testRemoveIfRemovesMatchingElements() {
        // Arrange
        Set<Integer> evenNumbers = new HashSet<>(Arrays.asList(2, 4, 6));
        Set<Integer> oddNumbers = new HashSet<>(Arrays.asList(1, 3, 5));
        CompositeSet<Integer> compositeSet = new CompositeSet<>(evenNumbers, oddNumbers);

        // Act: Remove all numbers greater than 3
        boolean wasModified = compositeSet.removeIf(num -> num > 3);

        // Assert
        assertTrue("removeIf should return true as elements were removed", wasModified);

        Set<Integer> expectedRemaining = new HashSet<>(Arrays.asList(1, 2, 3));
        assertEquals("Composite set should only contain numbers not matching the predicate",
                     expectedRemaining, compositeSet.toSet());

        assertEquals("Component set of even numbers should be modified",
                     new HashSet<>(Arrays.asList(2)), evenNumbers);
        assertEquals("Component set of odd numbers should be modified",
                     new HashSet<>(Arrays.asList(1, 3)), oddNumbers);
    }
}