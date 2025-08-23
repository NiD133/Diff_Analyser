package com.google.common.primitives;

import org.junit.Test;

/**
 * Tests for {@link SignedBytes}.
 */
public class SignedBytesTest {

    /**
     * Verifies that calling min() with a null array throws a NullPointerException.
     * This is the expected behavior as per Guava's standard contract for non-null parameters.
     */
    @Test(expected = NullPointerException.class)
    public void min_withNullArray_shouldThrowNullPointerException() {
        SignedBytes.min(null);
    }
}