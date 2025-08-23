package com.google.common.util.concurrent;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link AtomicDoubleArray}.
 */
public class AtomicDoubleArrayTest {

    /**
     * Verifies that addAndGet() correctly adds a value to an element,
     * returns the updated value, and permanently modifies the value in the array.
     */
    @Test
    public void addAndGet_shouldUpdateValueAndReturnNewValue() {
        // Arrange
        // An array initialized with all zeros.
        double[] initialValues = new double[8];
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(initialValues);

        int indexToUpdate = 0;
        double valueToAdd = 1775.31884;
        double expectedResult = 0.0 + valueToAdd;

        // Act
        double returnedValue = atomicArray.addAndGet(indexToUpdate, valueToAdd);

        // Assert
        // 1. The method should return the new, updated value.
        assertEquals(expectedResult, returnedValue, 0.0);

        // 2. The value at the specified index in the array should be updated.
        assertEquals(expectedResult, atomicArray.get(indexToUpdate), 0.0);

        // 3. The array's length should remain unchanged.
        assertEquals(8, atomicArray.length());
    }
}