package org.apache.commons.collections4.set;

import org.junit.Test;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class CompositeSetTest {

    /**
     * Tests the behavior of equals() and containsAll() on CompositeSets,
     * particularly in scenarios involving empty and nested empty sets.
     */
    @Test
    public void testEqualsAndContainsAllWithEmptyAndNestedCompositeSets() {
        // Part 1: Verify that equals() compares elements, not the internal structure of composited sets.

        // Arrange: Create two CompositeSets that are both logically empty but have different internal structures.
        CompositeSet<Object> emptyCompositeSet = new CompositeSet<>();
        CompositeSet<Object> compositeSetContainingAnEmptySet = new CompositeSet<>();
        compositeSetContainingAnEmptySet.addComposited(new CompositeSet<>()); // Add an empty set

        // Assert: Both sets should be considered equal because they both contain zero elements.
        // The equals() method for a Set is defined by its contents, not its implementation details.
        assertEquals(0, emptyCompositeSet.size());
        assertEquals(0, compositeSetContainingAnEmptySet.size());
        assertEquals("A composite set with no backing sets should be equal to one with an empty backing set",
                     emptyCompositeSet, compositeSetContainingAnEmptySet);
        assertEquals("Equality should be symmetric",
                     compositeSetContainingAnEmptySet, emptyCompositeSet);


        // Part 2: Verify containsAll() behavior on an empty CompositeSet.

        // Arrange: Create a non-empty set to check against the empty composite set.
        Set<Integer> setWithOneElement = new LinkedHashSet<>();
        setWithOneElement.add(-6);

        // Act
        boolean resultContainsAll = emptyCompositeSet.containsAll(setWithOneElement);

        // Assert: An empty set cannot contain all the elements of a non-empty set.
        assertFalse("An empty CompositeSet should not contain all elements of a non-empty set",
                    resultContainsAll);


        // Part 3: Verify equals() behavior between an empty CompositeSet and a non-empty Set.

        // Arrange: Using the sets from the previous parts.

        // Act
        boolean resultEquals = emptyCompositeSet.equals(setWithOneElement);

        // Assert: Sets of different sizes should not be equal.
        assertFalse("An empty CompositeSet should not be equal to a non-empty set",
                    resultEquals);
    }
}