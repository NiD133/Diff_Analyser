package com.google.common.util.concurrent;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link AtomicDoubleArray}.
 */
public class AtomicDoubleArrayTest {

    @Test
    public void getAndSet_shouldReturnPreviousValueAndUpdateArray() {
        // Arrange
        final int indexToUpdate = 1;
        final double initialValue = 540.0;
        final double newValue = 2806.57;

        // Initialize an array with a specific value at the index we plan to test.
        double[] sourceArray = {0.0, initialValue, 0.0, 0.0};
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(sourceArray);

        // Act
        // Atomically set a new value and get the old one back.
        double previousValue = atomicArray.getAndSet(indexToUpdate, newValue);

        // Assert
        // 1. Verify that the method returned the original value.
        assertEquals("getAndSet should return the value before the update",
            initialValue, previousValue, 0.0);

        // 2. Verify that the value at the index was successfully updated.
        assertEquals("The value in the array should be updated to the new value",
            newValue, atomicArray.get(indexToUpdate), 0.0);
    }
}