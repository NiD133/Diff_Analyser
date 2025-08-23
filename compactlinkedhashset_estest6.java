package com.google.common.collect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * Unit tests for {@link CompactLinkedHashSet}.
 */
public class CompactLinkedHashSetTest {

    /**
     * Verifies that calling toArray() on an empty set returns a non-null, empty array.
     */
    @Test
    public void toArray_onEmptySet_returnsEmptyArray() {
        // Arrange: Create an empty set.
        CompactLinkedHashSet<String> set = new CompactLinkedHashSet<>();

        // Act: Convert the set to an array.
        Object[] resultArray = set.toArray();

        // Assert: The resulting array should be empty but not null.
        assertNotNull("The array returned from an empty set should not be null.", resultArray);
        assertEquals("The length of the array from an empty set should be 0.", 0, resultArray.length);
    }
}