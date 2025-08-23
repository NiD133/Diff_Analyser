package com.google.common.util.concurrent;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Contains tests for the {@link AtomicDoubleArray#set(int, double)} method.
 */
public class AtomicDoubleArraySetTest {

    @Test
    public void set_updatesValueAtGivenIndex_andLengthIsUnchanged() {
        // Arrange: Create an AtomicDoubleArray and define the update parameters.
        double[] sourceArray = {0.0, 0.0, 0.0, 0.0, 0.0};
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(sourceArray);

        int indexToSet = 2;
        double newValue = 123.45;
        final double delta = 0.0; // Use a delta of 0.0 for exact double comparisons.

        // Act: Set a new value at the specified index.
        atomicArray.set(indexToSet, newValue);

        // Assert: Verify the value was updated and the array length is preserved.
        // 1. The most important check: The value at the index was correctly updated.
        assertEquals("The value at the specified index should be updated.",
                newValue, atomicArray.get(indexToSet), delta);

        // 2. The original test's check: The length of the array remains unchanged.
        assertEquals("The array length should not change after a set operation.",
                sourceArray.length, atomicArray.length());
    }
}