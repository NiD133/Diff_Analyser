package com.google.common.collect;

import org.junit.Test;
import java.util.Collections;

/**
 * Contains an improved test for the {@link CompactLinkedHashSet#moveLastEntry} method.
 */
public class CompactLinkedHashSet_ESTestTest18 extends CompactLinkedHashSet_ESTest_scaffolding {

    /**
     * Verifies that calling the internal {@code moveLastEntry} method with a destination index
     * that is out of bounds for the backing arrays throws an {@link ArrayIndexOutOfBoundsException}.
     *
     * <p><b>Note:</b> This is a white-box test that relies on the internal implementation detail
     * that creating a set with one element results in backing arrays of size 1.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void moveLastEntry_withOutOfBoundsIndex_throwsArrayIndexOutOfBoundsException() {
        // Arrange: Create a set with a single element. The implementation initializes
        // internal arrays (like 'predecessor' and 'successor') with a capacity of 1.
        CompactLinkedHashSet<Object> set = CompactLinkedHashSet.create(Collections.singleton(null));

        // Act: Attempt to move an entry to index 1. This index is out of bounds for the
        // internal arrays of size 1, which should trigger the exception. The mask argument (32)
        // is not relevant for this specific failure case.
        set.moveLastEntry(1, 32);

        // Assert: The test expects an ArrayIndexOutOfBoundsException, as declared in the @Test annotation.
    }
}