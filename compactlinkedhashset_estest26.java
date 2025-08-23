package com.google.common.collect;

import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for internal state management of {@link CompactLinkedHashSet}.
 */
public class CompactLinkedHashSetTest {

    /**
     * Verifies that calling allocArrays() on a set that has already been initialized
     * (and thus has its internal arrays allocated) throws an IllegalStateException.
     */
    @Test
    public void allocArrays_whenArraysAlreadyAllocated_throwsIllegalStateException() {
        // Arrange: Create a set using a factory method that initializes its internal arrays.
        // The CompactLinkedHashSet.create(Collection) factory ensures the set is fully
        // initialized and ready for use.
        CompactLinkedHashSet<String> set = CompactLinkedHashSet.create(Collections.singleton("one element"));

        // Act & Assert: Attempting to allocate the arrays again should fail.
        try {
            set.allocArrays();
            fail("Expected an IllegalStateException because the internal arrays have already been allocated.");
        } catch (IllegalStateException expected) {
            // Verify the exception message to confirm the cause of the failure.
            assertEquals("Arrays already allocated", expected.getMessage());
        }
    }
}