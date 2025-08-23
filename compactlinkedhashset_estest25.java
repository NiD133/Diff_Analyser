package com.google.common.collect;

import org.junit.Test;

import java.util.Collection;

/**
 * Unit tests for {@link CompactLinkedHashSet}.
 */
public class CompactLinkedHashSetTest {

    /**
     * Verifies that the create(Collection) factory method throws a NullPointerException
     * when the input collection is null, as this is a prohibited argument.
     */
    @Test(expected = NullPointerException.class)
    public void create_withNullCollection_shouldThrowNullPointerException() {
        // The cast to a Collection is necessary to resolve ambiguity between
        // create(Collection) and the varargs create(E...) method.
        CompactLinkedHashSet.create((Collection<Object>) null);
    }
}