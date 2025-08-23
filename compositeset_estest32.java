package org.apache.commons.collections4.set;

import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the CompositeSet class, focusing on its behavior when empty or containing other empty sets.
 */
public class CompositeSetTest {

    /**
     * This test verifies a key property of CompositeSet: its emptiness and equality are determined
     * by the elements it contains, not the structure of its component sets.
     *
     * A CompositeSet that contains only an empty set is itself considered empty and should be
     * equal to another CompositeSet that has no components.
     */
    @Test
    public void compositeSetContainingOnlyEmptySetIsEmptyAndEqualsEmptySet() {
        // Arrange: Create two empty composite sets.
        CompositeSet<String> emptySet = new CompositeSet<>();
        CompositeSet<String> setContainingEmptySet = new CompositeSet<>();

        // Act: Add one empty set as a component of the other.
        setContainingEmptySet.addComposited(new HashSet<>());

        // Assert: The composite set is still considered empty because its component has no elements.
        assertTrue("A composite set containing only an empty set should be empty", setContainingEmptySet.isEmpty());
        assertEquals(0, setContainingEmptySet.size());

        // Assert: It should also be equal to a composite set with no components.
        assertEquals("A composite set containing an empty set should equal an empty composite set",
                emptySet, setContainingEmptySet);
    }

    /**
     * This test verifies that standard collection operations on an empty CompositeSet
     * behave as expected, ensuring it conforms to the Set contract.
     */
    @Test
    public void operationsOnEmptyCompositeSetBehaveAsExpected() {
        // Arrange: Create an empty composite set and another empty collection for operations.
        CompositeSet<Integer> emptyCompositeSet = new CompositeSet<>();
        Set<Integer> otherEmptySet = Collections.emptySet();

        // --- Test toSet() ---
        // Act: Convert the composite set to a standard Set.
        Set<Integer> resultSet = emptyCompositeSet.toSet();
        // Assert: The resulting set should also be empty.
        assertTrue("toSet() on an empty composite set should return an empty set", resultSet.isEmpty());

        // --- Test retainAll() ---
        // Act: retainAll with an empty collection should not change the set.
        boolean changed = emptyCompositeSet.retainAll(otherEmptySet);
        // Assert: The method should return false, indicating no modification.
        assertFalse("retainAll with an empty collection should not modify the set", changed);

        // --- Test removeComposited() ---
        // Act: Attempt to remove a component set that was never added.
        emptyCompositeSet.removeComposited(otherEmptySet);
        // Assert: This should have no effect on the internal list of component sets.
        assertTrue("removeComposited should have no effect on an empty set's components",
                emptyCompositeSet.getSets().isEmpty());
    }
}