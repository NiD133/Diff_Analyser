package com.google.common.util.concurrent;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit tests for {@link AtomicDoubleArray}.
 */
public class AtomicDoubleArrayTest {

    @Test
    public void addAndGet_whenAddingZero_returnsUnchangedValue() {
        // Arrange
        final double initialValue = 12.34;
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(new double[]{initialValue});
        final int index = 0;
        final double deltaToAdd = 0.0;

        // Act
        double updatedValue = atomicArray.addAndGet(index, deltaToAdd);

        // Assert
        // The method should return the new value, which is unchanged.
        assertEquals(initialValue, updatedValue, 0.0);
        // The value in the array at the specified index should also remain unchanged.
        assertEquals(initialValue, atomicArray.get(index), 0.0);
    }
}