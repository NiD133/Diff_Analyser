package com.google.common.collect;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link CompactLinkedHashSet#clear()}.
 */
public class CompactLinkedHashSetClearTest {

    @Test
    public void clear_onNonEmptySet_shouldMakeTheSetEmpty() {
        // Arrange: Create a set with a single element to make it non-empty.
        CompactLinkedHashSet<String> set = CompactLinkedHashSet.create("an element");

        // Act: Call the clear() method, which is the action under test.
        set.clear();

        // Assert: Verify the set is empty after being cleared.
        assertTrue("The set should be empty after calling clear().", set.isEmpty());
        assertEquals("The set size should be 0 after calling clear().", 0, set.size());
    }
}