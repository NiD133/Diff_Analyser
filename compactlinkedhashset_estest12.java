package com.google.common.collect;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the package-private method {@link CompactLinkedHashSet#adjustAfterRemove(int, int)}.
 *
 * <p>This method is used to update an index (like an iterator's cursor) after an element has been
 * removed from the set. When an element is removed, the last element in the backing array is moved
 * into the removed element's slot to maintain a compact structure.
 */
public class CompactLinkedHashSetAdjustAfterRemoveTest {

    @Test
    public void adjustAfterRemove_whenIndexPointsToMovedElement_returnsNewIndex() {
        // This test simulates the scenario where an index points to the last element of the set.
        // When another element is removed, that last element is moved. The index should be
        // updated to point to the new location of the moved element. In CompactLinkedHashSet,
        // this specific case is identified when the index to adjust equals the set's size.

        // Arrange: Create a set with two elements. Its size is 2.
        CompactLinkedHashSet<String> set = CompactLinkedHashSet.create();
        set.add("element-A");
        set.add("element-B");

        // The index to adjust is 2, which equals the set's size. This represents a pointer
        // to the position of the last element before its removal-related move.
        int indexToAdjust = 2;
        int removedElementIndex = 0; // Simulate removing the element at index 0.

        // Act: Call adjustAfterRemove.
        // The logic is `(indexToAdjust == size()) ? removedElementIndex : indexToAdjust`.
        // Since 2 == 2, the method should return `removedElementIndex`.
        int adjustedIndex = set.adjustAfterRemove(indexToAdjust, removedElementIndex);

        // Assert: The index is adjusted to the slot of the removed element.
        assertEquals("Index should be updated to the removed element's position",
            removedElementIndex, adjustedIndex);
    }

    @Test
    public void adjustAfterRemove_whenIndexPointsToUnmovedElement_returnsOriginalIndex() {
        // This test simulates the scenario where an index points to an element that is not
        // affected by a removal operation (i.e., it's not the last element that gets moved).
        // In this case, the index should remain unchanged.

        // Arrange: Create a set with three elements. Its size is 3.
        CompactLinkedHashSet<String> set = CompactLinkedHashSet.create();
        set.add("element-A");
        set.add("element-B");
        set.add("element-C");

        // The index to adjust points to the middle element, which will not be moved.
        int indexToAdjust = 1;
        int removedElementIndex = 0; // Simulate removing the element at index 0.

        // Act: Call adjustAfterRemove.
        // The logic is `(indexToAdjust == size()) ? removedElementIndex : indexToAdjust`.
        // Since 1 != 3, the method should return `indexToAdjust`.
        int adjustedIndex = set.adjustAfterRemove(indexToAdjust, removedElementIndex);

        // Assert: The index should not be changed.
        assertEquals("Index pointing to an unmoved element should not change",
            indexToAdjust, adjustedIndex);
    }
}