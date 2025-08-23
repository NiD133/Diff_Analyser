package com.google.common.collect;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link CompactLinkedHashSet}.
 */
public class CompactLinkedHashSetTest {

    /**
     * Verifies that resizing the internal data structure of the set does not
     * alter the number of elements it contains.
     */
    @Test
    public void resizeEntries_whenCapacityIncreases_shouldNotChangeSetSize() {
        // Arrange: Create a set with a single element.
        CompactLinkedHashSet<String> set = CompactLinkedHashSet.create("one");
        assertEquals("Precondition: Set should contain one element", 1, set.size());

        // Act: Resize the internal entries array to a larger capacity.
        // The specific new capacity is not critical, only that it's different.
        int newCapacity = 41;
        set.resizeEntries(newCapacity);

        // Assert: The size of the set should remain unchanged after the resize.
        assertEquals("Set size should be preserved after resizing", 1, set.size());
    }
}