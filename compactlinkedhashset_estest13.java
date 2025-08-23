package com.google.common.collect;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.Collections;

public class CompactLinkedHashSet_ESTestTest13 extends CompactLinkedHashSet_ESTest_scaffolding {

    /**
     * Tests that `adjustAfterRemove` returns the `indexRemoved` parameter when the
     * `indexBeforeRemove` parameter is out of bounds (i.e., greater than or equal to the set's size).
     */
    @Test
    public void adjustAfterRemove_whenIndexIsOutOfBounds_returnsRemovedIndex() {
        // Arrange: Create a set with a single element to establish a known size.
        CompactLinkedHashSet<String> set = CompactLinkedHashSet.create(Collections.singleton("one"));
        assertEquals("Precondition: Set size should be 1", 1, set.size());

        // Use an index that is clearly out of bounds and a distinct value for the removed index.
        int outOfBoundsIndex = 100;
        int removedIndex = 50;

        // Act: Call the method with the out-of-bounds index.
        int adjustedIndex = set.adjustAfterRemove(outOfBoundsIndex, removedIndex);

        // Assert: The method should return the `removedIndex` value, not the out-of-bounds index.
        assertEquals(
            "For an out-of-bounds index, adjustAfterRemove should return the removedIndex",
            removedIndex,
            adjustedIndex);

        // Verify that the method is a pure function and does not modify the set's size.
        assertEquals("The set size should remain unchanged", 1, set.size());
    }
}