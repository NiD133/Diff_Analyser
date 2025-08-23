package com.google.common.primitives;

import org.junit.Test;

/**
 * Unit tests for {@link SignedBytes}.
 */
public class SignedBytesTest {

    /**
     * Tests that calling SignedBytes.max() with a null array throws a NullPointerException,
     * as null is not a valid input for this method.
     */
    @Test(expected = NullPointerException.class)
    public void max_withNullArray_shouldThrowNullPointerException() {
        SignedBytes.max(null);
    }
}