package com.google.common.collect;

import org.junit.Test;

/**
 * This class contains tests for the internal behavior of {@link CompactLinkedHashSet}.
 * The original test was auto-generated and has been improved for clarity.
 */
public class CompactLinkedHashSet_ESTestTest35 extends CompactLinkedHashSet_ESTest_scaffolding {

    /**
     * Verifies that calling getSuccessor() with an out-of-bounds index on an empty set
     * throws an ArrayIndexOutOfBoundsException. This tests the boundary conditions of
     * the internal array access.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void getSuccessor_onEmptySet_throwsExceptionForOutOfBoundsIndex() {
        // Arrange: Create an empty set. A newly created CompactLinkedHashSet has internal arrays
        // with a small default capacity (3 in this implementation).
        CompactLinkedHashSet<Integer> set = CompactLinkedHashSet.create();

        // Act & Assert: Attempting to access an entry at an index that is outside the bounds
        // of the allocated internal arrays should result in an exception. Index 3 is
        // chosen because it is out of bounds for the default initial array size.
        set.getSuccessor(3);
    }
}