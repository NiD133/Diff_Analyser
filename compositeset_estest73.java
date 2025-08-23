package org.apache.commons.collections4.set;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * This test class contains tests for the CompositeSet class.
 * The original test was automatically generated and has been refactored for clarity.
 */
public class CompositeSet_ESTestTest73 extends CompositeSet_ESTest_scaffolding {

    /**
     * Tests that CompositeSets that are logically empty are considered equal,
     * regardless of their internal composition. Also verifies that different
     * empty CompositeSet instances share the same singleton empty iterator.
     */
    @Test(timeout = 4000)
    public void testEmptyCompositeSetsAreEqualAndShareEmptyIterator() {
        // Arrange: Create different configurations of empty CompositeSets.

        // set1 is empty because it was constructed with no underlying sets.
        final CompositeSet<Integer> setEmptyByDefault = new CompositeSet<>();

        // set2 is also logically empty, but it is composed of other empty sets.
        final CompositeSet<Integer> setComposedOfEmptySets = new CompositeSet<>();
        final Set<Integer> underlyingEmptyHashSet = new HashSet<>();
        final CompositeSet<Integer> underlyingEmptyCompositeSet = new CompositeSet<>();
        setComposedOfEmptySets.addComposited(underlyingEmptyHashSet, underlyingEmptyCompositeSet);

        // set3 is another empty set, created to compare its iterator instance.
        final CompositeSet<Integer> anotherEmptySet = new CompositeSet<>();

        // Assert: Verify core properties of empty sets.
        assertTrue("A set constructed with no components should be empty", setEmptyByDefault.isEmpty());
        assertEquals("A set constructed with no components should have size 0", 0, setEmptyByDefault.size());

        assertTrue("A set composed of only empty sets should be empty", setComposedOfEmptySets.isEmpty());
        assertEquals("A set composed of only empty sets should have size 0", 0, setComposedOfEmptySets.size());

        // Assert: Verify that the two types of empty sets are equal, fulfilling the Set contract.
        assertEquals("An empty set should be equal to a set composed of other empty sets",
                     setEmptyByDefault, setComposedOfEmptySets);
        assertEquals("Equality should be symmetric",
                     setComposedOfEmptySets, setEmptyByDefault);
        assertEquals("Equal sets must have equal hash codes",
                     setEmptyByDefault.hashCode(), setComposedOfEmptySets.hashCode());

        // Assert: Verify that iterators from different empty CompositeSet instances are the same object.
        // This suggests an efficient implementation using a singleton empty iterator.
        final Iterator<Integer> iterator1 = setEmptyByDefault.iterator();
        final Iterator<Integer> iterator2 = anotherEmptySet.iterator();
        assertSame("Iterators from two distinct, empty CompositeSets should be the same instance",
                     iterator1, iterator2);
    }
}