package com.google.common.util.concurrent;

import org.junit.Test;

/**
 * Tests for {@link AtomicDoubleArray} focusing on boundary conditions.
 */
public class AtomicDoubleArrayTest {

    /**
     * Verifies that weakCompareAndSet() throws an IndexOutOfBoundsException
     * when the provided index is equal to the array's length, which is out of bounds.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void weakCompareAndSet_withIndexEqualToLength_throwsIndexOutOfBoundsException() {
        // Arrange: Create an array of a specific size.
        final int size = 10;
        AtomicDoubleArray array = new AtomicDoubleArray(size);

        // The first invalid index is the array's length. Valid indices are [0, size-1].
        final int outOfBoundsIndex = size;
        final double anyExpectedValue = 0.0;
        final double anyNewValue = 1.0;

        // Act & Assert: Call the method with the out-of-bounds index.
        // The @Test(expected=...) annotation asserts that an IndexOutOfBoundsException is thrown.
        array.weakCompareAndSet(outOfBoundsIndex, anyExpectedValue, anyNewValue);
    }
}