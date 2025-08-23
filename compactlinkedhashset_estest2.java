package com.google.common.collect;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

// Note: The original test was automatically generated and had several understandability issues,
// including a non-descriptive name, confusing setup, and a questionable assertion. This
// version has been rewritten to be a clear and correct white-box test of the internal
// `insertEntry` method.

public class CompactLinkedHashSet_ESTestTest2 extends CompactLinkedHashSet_ESTest_scaffolding {

    /**
     * Verifies that the internal {@code insertEntry} method correctly adds an element
     * and updates the set's size.
     *
     * <p><b>Note:</b> This is a white-box test that calls a package-private method.
     * It tests the low-level mechanics of the class, not its public API.
     */
    @Test(timeout = 4000)
    public void insertEntry_whenCalledDirectly_addsElementAndIncrementsSize() {
        // Arrange: Start with an empty set for a clear and simple test case.
        CompactLinkedHashSet<String> set = CompactLinkedHashSet.create();
        String elementToAdd = "test-element";
        int elementHash = elementToAdd.hashCode();

        // The following parameters are for the internal implementation of the hash set.
        int entryIndex = 0; // The index for the new entry in the internal array.
        int hashTableMask = 0; // The mask for the internal hash table (simplified for this test).

        // Act: Call the package-private method to directly insert the element.
        set.insertEntry(entryIndex, elementToAdd, elementHash, hashTableMask);

        // Assert: Verify that the size has increased and the element is now in the set.
        assertEquals("The set size should be 1 after inserting one element.", 1, set.size());
        assertTrue("The set should contain the newly inserted element.", set.contains(elementToAdd));
    }
}