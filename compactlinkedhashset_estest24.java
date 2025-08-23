package com.google.common.collect;

import org.junit.Test;

/**
 * Tests for {@link CompactLinkedHashSet#create(E...)}.
 */
public class CompactLinkedHashSet_ESTestTest24 extends CompactLinkedHashSet_ESTest_scaffolding {

    /**
     * Verifies that calling the varargs factory method {@code create(E...)} with a null
     * array argument throws a {@link NullPointerException}.
     */
    @Test(expected = NullPointerException.class)
    public void create_withNullElementsArray_shouldThrowNullPointerException() {
        // The cast to Object[] is necessary to resolve the ambiguity between
        // create(Collection<E>) and the varargs create(E...). Passing a null
        // array to the varargs method is expected to cause a NullPointerException.
        CompactLinkedHashSet.create((Object[]) null);
    }
}