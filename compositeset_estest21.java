package org.apache.commons.collections4.set;

import org.junit.Test;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link CompositeSet} class, focusing on mutation operations.
 */
public class CompositeSetTest {

    /**
     * Tests that calling removeAll on a CompositeSet correctly removes elements
     * from the underlying component set and updates the state of the CompositeSet.
     */
    @Test
    public void testRemoveAllClearsBothCompositeAndUnderlyingSet() {
        // Arrange: Create a CompositeSet with one underlying set containing one element.
        final Set<Integer> underlyingSet = new LinkedHashSet<>();
        underlyingSet.add(100);

        final CompositeSet<Integer> compositeSet = new CompositeSet<>(underlyingSet);

        // Sanity check to ensure the initial state is correct.
        assertFalse("CompositeSet should not be empty after initialization", compositeSet.isEmpty());
        assertEquals("CompositeSet should contain one element", 1, compositeSet.size());
        assertTrue("Underlying set should contain the element", underlyingSet.contains(100));

        // Act: Remove all elements from the composite set that are also in the underlying set.
        // This operation should modify both the composite and the underlying set.
        final boolean wasModified = compositeSet.removeAll(underlyingSet);

        // Assert: The composite set and the underlying set should now be empty.
        assertTrue("removeAll should return true as the set was modified", wasModified);
        assertTrue("CompositeSet should be empty after removeAll", compositeSet.isEmpty());
        assertEquals("CompositeSet size should be 0 after removeAll", 0, compositeSet.size());
        assertTrue("The change should propagate to the underlying set, which should now be empty", underlyingSet.isEmpty());
    }
}