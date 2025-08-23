package com.google.common.util.concurrent;

import org.junit.Test;

/**
 * Tests for {@link AtomicDoubleArray}.
 */
public class AtomicDoubleArrayTest {

    /**
     * Verifies that compareAndSet throws an IndexOutOfBoundsException when the index
     * is outside the bounds of the array.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void compareAndSet_withOutOfBoundsIndex_throwsIndexOutOfBoundsException() {
        // Arrange: Create an empty array, which has a size of 0.
        AtomicDoubleArray array = new AtomicDoubleArray(0);
        int outOfBoundsIndex = 44;
        double anyValue = 1.0;

        // Act: Attempt to access an index that does not exist.
        // The test succeeds if this call throws the expected IndexOutOfBoundsException.
        array.compareAndSet(outOfBoundsIndex, anyValue, anyValue);
    }
}