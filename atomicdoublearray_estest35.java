package com.google.common.util.concurrent;

import org.junit.Test;

/**
 * Tests for {@link AtomicDoubleArray}.
 */
public class AtomicDoubleArrayTest {

    /**
     * Verifies that the constructor throws a NullPointerException when
     * initialized with a null array, as specified by its contract.
     */
    @Test(expected = NullPointerException.class)
    public void constructor_withNullArray_shouldThrowNullPointerException() {
        // Attempting to create an AtomicDoubleArray from a null source array
        // is expected to fail with a NullPointerException.
        new AtomicDoubleArray((double[]) null);
    }
}