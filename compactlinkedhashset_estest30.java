package com.google.common.collect;

import org.junit.Test;

import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This class contains tests for the CompactLinkedHashSet.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class CompactLinkedHashSet_ESTestTest30 extends CompactLinkedHashSet_ESTest_scaffolding {

    /**
     * Tests that calling `retainAll` with an empty collection removes all elements
     * from the set and correctly returns `true` to indicate modification.
     */
    @Test
    public void retainAll_withEmptyCollection_shouldRemoveAllElementsAndReturnTrue() {
        // Arrange: Create a non-empty set.
        CompactLinkedHashSet<Object> set = CompactLinkedHashSet.create("element1", null, "element2");
        assertFalse("Precondition failed: Set should not be empty before the test.", set.isEmpty());

        // The collection to retain, which is empty.
        Collection<Object> emptyCollection = Collections.emptySet();

        // Act: Perform the retainAll operation. This should remove all elements from the set.
        boolean wasModified = set.retainAll(emptyCollection);

        // Assert: Verify the set is now empty and the method returned true.
        assertTrue("The set should be empty after retaining an empty collection.", set.isEmpty());
        assertTrue("retainAll should return true because the set was modified.", wasModified);
    }
}