package com.google.common.collect;

import org.junit.Test;

/**
 * This test class focuses on the behavior of the internal resizing logic of
 * {@link CompactLinkedHashSet}.
 */
// The original test class name is preserved to maintain context.
public class CompactLinkedHashSet_ESTestTest32 extends CompactLinkedHashSet_ESTest_scaffolding {

    /**
     * Verifies that calling resizeEntries() with a negative capacity throws a
     * NegativeArraySizeException. This method is an internal implementation detail,
     * but its contract regarding invalid arguments should be upheld.
     */
    @Test(expected = NegativeArraySizeException.class)
    public void resizeEntries_whenCapacityIsNegative_shouldThrowNegativeArraySizeException() {
        // Arrange: Create a set instance. Its contents are not relevant for this test.
        CompactLinkedHashSet<Object> set = CompactLinkedHashSet.create();
        int negativeCapacity = -1;

        // Act: Attempt to resize the internal arrays to a negative capacity.
        // Assert: A NegativeArraySizeException is expected, as declared by the @Test annotation.
        set.resizeEntries(negativeCapacity);
    }
}