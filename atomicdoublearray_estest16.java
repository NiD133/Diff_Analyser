package com.google.common.util.concurrent;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link AtomicDoubleArray}.
 */
public class AtomicDoubleArrayTest {

    /**
     * Tests that calling addAndGet with a zero delta does not change the element's value
     * and returns the original value.
     */
    @Test
    public void addAndGet_withZeroDelta_returnsOriginalValueAndDoesNotChangeElement() {
        // Arrange
        final double originalValue = -970.960157577528;
        final int indexToUpdate = 0;
        double[] sourceArray = new double[6];
        sourceArray[indexToUpdate] = originalValue;

        AtomicDoubleArray atomicArray = new AtomicDoubleArray(sourceArray);

        // Act
        double result = atomicArray.addAndGet(indexToUpdate, 0.0);

        // Assert
        // The returned value should be identical to the original value.
        // A delta of 0.0 is used because adding 0.0 should result in a bit-for-bit identical double.
        assertEquals(originalValue, result, 0.0);

        // For completeness, verify that the value in the array is also unchanged.
        assertEquals(originalValue, atomicArray.get(indexToUpdate), 0.0);
    }
}