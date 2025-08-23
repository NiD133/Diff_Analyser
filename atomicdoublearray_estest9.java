package com.google.common.util.concurrent;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link AtomicDoubleArray}.
 */
public class AtomicDoubleArrayTest {

    @Test
    public void getAndSet_returnsOldValueAndSetsNewValue() {
        // Arrange
        final double initialValue = -1583.803774;
        final double newValue = -1710.836556;
        final int indexToUpdate = 0;

        double[] sourceArray = {initialValue, 10.0, 20.0};
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(sourceArray);

        // Act
        double returnedValue = atomicArray.getAndSet(indexToUpdate, newValue);

        // Assert
        // 1. Verify that the method returned the original value.
        assertEquals(
                "getAndSet should return the value before the update.",
                initialValue,
                returnedValue,
                0.0);

        // 2. Verify that the value in the array was updated to the new value.
        double valueAfterUpdate = atomicArray.get(indexToUpdate);
        assertEquals(
                "The value in the array should be updated to the new value.",
                newValue,
                valueAfterUpdate,
                0.0);
    }
}