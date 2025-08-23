package com.google.common.util.concurrent;

import static org.junit.Assert.assertEquals;

import java.util.function.DoubleUnaryOperator;
import org.junit.Test;

/**
 * Tests for {@link AtomicDoubleArray}.
 */
public class AtomicDoubleArrayTest {

    @Test
    public void updateAndGet_withIdentityFunction_doesNotChangeValueAndReturnsIt() {
        // Arrange
        // Create an array where all elements are initialized to 0.0 by default.
        double[] initialValues = new double[10];
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(initialValues);

        int indexToUpdate = 4;
        double valueBeforeUpdate = atomicArray.get(indexToUpdate); // This will be 0.0

        // The identity operator is a function that simply returns its input.
        DoubleUnaryOperator identityOperator = DoubleUnaryOperator.identity();

        // Act
        // Apply the identity operator, which should not change the underlying value.
        // The method should return the new (unchanged) value.
        double returnedValue = atomicArray.updateAndGet(indexToUpdate, identityOperator);

        // Assert
        // 1. The returned value should be the same as the original value.
        assertEquals(
            "The method should return the value after the update, which is unchanged.",
            valueBeforeUpdate,
            returnedValue,
            0.0);

        // 2. The value in the array at the specified index should also remain unchanged.
        assertEquals(
            "The value in the array should not be modified by the identity operator.",
            valueBeforeUpdate,
            atomicArray.get(indexToUpdate),
            0.0);
    }
}