package com.google.common.collect;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link CompactLinkedHashSet}.
 */
public class CompactLinkedHashSetTest {

    @Test
    public void firstEntryIndex_onEmptySet_returnsNegativeTwo() {
        // Arrange: Create an empty CompactLinkedHashSet.
        // The expected value -2 corresponds to the internal `ENDPOINT` constant,
        // which indicates that the set is empty.
        CompactLinkedHashSet<Object> set = CompactLinkedHashSet.create();
        int expectedIndex = -2;

        // Act: Get the index of the first entry.
        int actualIndex = set.firstEntryIndex();

        // Assert: Verify the index is the special value for an empty set.
        assertEquals(
            "The first entry index of a newly created, empty set should be -2.",
            expectedIndex,
            actualIndex);
    }
}