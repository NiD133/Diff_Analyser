package com.google.common.collect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Contains tests for {@link CompactLinkedHashSet}.
 */
public class CompactLinkedHashSetTest {

    /**
     * Verifies that converting the set to its hash-flooding resistant implementation
     * correctly preserves the set's contents, particularly when the set contains only a null element.
     */
    @Test
    public void convertToHashFloodingResistantImplementation_preservesSetWithSingleNullElement() {
        // Arrange: Create a set from an array containing only null values.
        // The factory method deduplicates these, resulting in a set with a single null element.
        Integer[] arrayOfAllNulls = new Integer[6];
        CompactLinkedHashSet<Integer> set = CompactLinkedHashSet.create(arrayOfAllNulls);

        // Verify the initial state before the main action.
        assertEquals("Precondition failed: Set should contain exactly one element (null).", 1, set.size());
        assertTrue("Precondition failed: Set should contain the null element.", set.contains(null));

        // Act: Convert the set's internal storage. This is an internal optimization that
        // should not affect the set's logical contents.
        set.convertToHashFloodingResistantImplementation();

        // Assert: The set's content should remain unchanged after the conversion.
        assertEquals("The set size should remain 1 after conversion.", 1, set.size());
        assertTrue("The null element should still be present after conversion.", set.contains(null));
    }
}