package com.google.common.collect;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Collections;

/**
 * This test focuses on the internal behavior of the `moveLastEntry` method
 * in {@link CompactLinkedHashSet}.
 */
public class CompactLinkedHashSet_ESTestTest1 extends CompactLinkedHashSet_ESTest_scaffolding {

    /**
     * Tests the edge case where `moveLastEntry` is called on a set with a single element,
     * attempting to move that element to its own position. The test verifies that this
     * operation does not change the size of the set.
     */
    @Test
    public void moveLastEntry_onSingleElementSetToSameIndex_shouldNotChangeSize() {
        // Arrange: Create a set with a single element.
        // The original test used `new Integer[2]`, which creates an array of two nulls.
        // When creating a set from this, it results in a single `null` element.
        // Using Collections.singleton(null) is a more direct and understandable way
        // to achieve the same state.
        CompactLinkedHashSet<Integer> set = CompactLinkedHashSet.create(Collections.singleton(null));
        assertEquals("Precondition: Set should contain exactly one element.", 1, set.size());

        // Act: Call the internal `moveLastEntry` method.
        // In a set with one element, the "last entry" is at index 0. We are testing
        // the case of moving this entry to its own destination index (0).
        // The second argument, `mask`, is part of the hash table's internal mechanics.
        int destinationIndex = 0;
        int mask = 0;
        set.moveLastEntry(destinationIndex, mask);

        // Assert: The size of the set should remain unchanged.
        assertEquals("The set size should not change after the move operation.", 1, set.size());
    }
}