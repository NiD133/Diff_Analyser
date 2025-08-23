package com.google.common.collect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for {@link CompactLinkedHashSet}.
 */
public class CompactLinkedHashSetTest {

    @Test
    public void clear_onEmptySetWithInitialCapacity_shouldRemainEmpty() {
        // Arrange: Create an empty set with a specific initial capacity.
        // Although the set is empty, its internal arrays are pre-allocated.
        CompactLinkedHashSet<Integer> set = CompactLinkedHashSet.createWithExpectedSize(100);

        // Act: Clear the set.
        set.clear();

        // Assert: The set should still be empty after clearing.
        assertTrue("The set should be empty after clearing.", set.isEmpty());
        assertEquals("The size of the set should be 0.", 0, set.size());
    }
}