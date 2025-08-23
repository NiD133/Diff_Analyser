package org.mockito.internal.util.collections;

import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for {@link HashCodeAndEqualsSafeSet}.
 */
public class HashCodeAndEqualsSafeSetTest {

    /**
     * Verifies that calling retainAll with an empty collection on a non-empty set
     * correctly removes all elements and returns true, indicating the set was modified.
     */
    @Test
    public void retainAll_shouldReturnTrueAndEmptyTheSet_whenCalledWithEmptyCollection() {
        // Arrange: Create a set and add one element to it.
        HashCodeAndEqualsSafeSet safeSet = new HashCodeAndEqualsSafeSet();
        safeSet.add(new Object());

        // Pre-condition check to ensure the test setup is correct.
        assertFalse("Pre-condition failed: Set should not be empty before the test.", safeSet.isEmpty());

        // Act: Call retainAll with an empty collection.
        boolean wasModified = safeSet.retainAll(Collections.emptyList());

        // Assert: Verify the set was modified and is now empty.
        assertTrue("retainAll should return true because the set was modified.", wasModified);
        assertTrue("The set should be empty after retaining an empty collection.", safeSet.isEmpty());
    }
}