package com.google.common.collect;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test for the internal {@link CompactLinkedHashSet#adjustAfterRemove} method.
 */
public class CompactLinkedHashSet_ESTestTest14 extends CompactLinkedHashSet_ESTest_scaffolding {

    /**
     * Tests that adjustAfterRemove returns the 'removedIndex' when the 'indexBeforeRemove'
     * is greater than or equal to the set's size. This is a specific behavior for
     * CompactLinkedHashSet's implementation of this internal helper method.
     */
    @Test
    public void adjustAfterRemove_withIndexGreaterThanSize_returnsRemovedIndex() {
        // Arrange
        // Create an empty set, which has a size of 0.
        CompactLinkedHashSet<Integer> emptySet = CompactLinkedHashSet.create();
        
        // Use an index that is guaranteed to be >= size() for an empty set.
        int indexGreaterThanSize = 73;
        
        // The value for the index that was hypothetically removed.
        int removedIndex = -1;

        // Act
        // The implementation in CompactLinkedHashSet is:
        // return (indexBeforeRemove >= size()) ? indexRemoved : indexBeforeRemove;
        // Since 73 >= 0 is true, the method should return removedIndex.
        int adjustedIndex = emptySet.adjustAfterRemove(indexGreaterThanSize, removedIndex);

        // Assert
        assertEquals(
            "When the index to adjust is out of bounds (>= size), the method should return the removed index.",
            removedIndex,
            adjustedIndex);
    }
}