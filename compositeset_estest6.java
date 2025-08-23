package org.apache.commons.collections4.set;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Contains improved tests for {@link CompositeSet}, focusing on clarity and maintainability.
 *
 * The original test case was a single, complex method that was hard to understand.
 * It has been split into two focused tests:
 * 1. A test for the equals() method with sets of different content.
 * 2. A test for iteration and size calculation with a complex structure of nested sets.
 */
public class CompositeSetRefactoredTest {

    /**
     * Tests that CompositeSet.equals() correctly returns false when comparing
     * a non-empty set with an empty set.
     */
    @Test
    public void equalsShouldReturnFalseForSetsWithDifferentContent() {
        // Arrange
        Set<String> setWithElement = new HashSet<>(Collections.singletonList("a"));
        CompositeSet<String> compositeSetWithElement = new CompositeSet<>(setWithElement);
        CompositeSet<String> emptyCompositeSet = new CompositeSet<>();

        // Act & Assert
        assertNotEquals("A non-empty set should not be equal to an empty set",
                compositeSetWithElement, emptyCompositeSet);
        assertNotEquals("An empty set should not be equal to a non-empty set",
                emptyCompositeSet, compositeSetWithElement);
    }

    /**
     * Verifies that a CompositeSet correctly handles a structure of nested
     * CompositeSets, regular sets, and empty sets for iteration and size calculation.
     *
     * This replaces the core logic of the original generated test, which created a
     * confusing structure of empty sets. This version uses a clear, mixed-content
     * structure to provide a more meaningful verification.
     */
    @Test
    public void shouldCorrectlyIterateOverNestedAndEmptySets() {
        // Arrange
        // 1. A standard set with one element.
        Set<String> standardSet = new HashSet<>(Collections.singletonList("A"));

        // 2. An empty set, which should contribute nothing to the composite set.
        Set<String> emptySet = new HashSet<>();

        // 3. A nested CompositeSet containing another set.
        Set<String> nestedContentSet = new HashSet<>(Collections.singletonList("B"));
        CompositeSet<String> nestedCompositeSet = new CompositeSet<>(nestedContentSet);

        // Create the main CompositeSet from the components above.
        CompositeSet<String> mainCompositeSet = new CompositeSet<>(
                standardSet, emptySet, nestedCompositeSet
        );

        // Act
        // Collect all elements from the main set's iterator to verify its contents.
        Set<String> actualElements = new HashSet<>();
        mainCompositeSet.iterator().forEachRemaining(actualElements::add);

        // Assert
        // Check the size of the composite set.
        assertEquals("Size should be the total number of unique elements ('A' and 'B')",
                2, mainCompositeSet.size());

        // Verify the contents of the composite set.
        Set<String> expectedElements = new HashSet<>(Arrays.asList("A", "B"));
        assertEquals("The set should contain all elements from its components",
                expectedElements, actualElements);

        // Verify the contains() method for all expected elements.
        assertTrue("Should contain 'A' from the standard set", mainCompositeSet.contains("A"));
        assertTrue("Should contain 'B' from the nested composite set", mainCompositeSet.contains("B"));
        assertFalse("Should not contain an element that was never added", mainCompositeSet.contains("C"));
    }
}