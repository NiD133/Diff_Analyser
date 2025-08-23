package com.google.common.collect;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Collections;

/**
 * This test class focuses on the behavior of the internal {@code init} method
 * in {@link CompactLinkedHashSet}.
 */
public class CompactLinkedHashSet_ESTestTest3 extends CompactLinkedHashSet_ESTest_scaffolding {

    /**
     * Verifies that calling the internal `init()` method on an existing, non-empty set
     * effectively clears and resets it.
     */
    @Test
    public void init_onExistingSet_clearsAndResetsTheSet() {
        // Arrange: Create a non-empty set. The original test used an array of two nulls,
        // which results in a set containing a single null element. We'll create that
        // state explicitly for clarity.
        CompactLinkedHashSet<Object> set = CompactLinkedHashSet.create(Collections.singleton(null));
        assertFalse("Precondition: Set should not be empty before the test action.", set.isEmpty());
        assertTrue("Precondition: Set should contain the initial element.", set.contains(null));

        // Act: Call the package-private init() method. This is expected to re-initialize
        // the set's internal state, effectively clearing it.
        set.init(0);

        // Assert: The set should now be empty and no longer contain its original element.
        assertTrue("Set should be empty after re-initialization.", set.isEmpty());
        assertEquals("Set size should be 0 after re-initialization.", 0, set.size());
        assertFalse("Set should no longer contain its original element.", set.contains(null));

        // The original test asserted that the set did not contain the integer 0.
        // This is also true for any empty set, so we include a similar check.
        assertFalse("An empty set should not contain an arbitrary element.", set.contains(0));
    }
}