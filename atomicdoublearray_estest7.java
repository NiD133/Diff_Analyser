package com.google.common.util.concurrent;

import static org.junit.Assert.assertEquals;

import java.util.function.DoubleUnaryOperator;
import org.junit.Test;

/**
 * Tests for {@link AtomicDoubleArray}.
 */
public class AtomicDoubleArrayTest {

    /**
     * Verifies that getAndUpdate() returns the original value from the array before applying the
     * update function.
     */
    @Test
    public void getAndUpdate_withIdentityFunction_returnsOriginalValueAndStateIsUnchanged() {
        // Arrange
        final double originalValue = -1583.803774;
        final int indexToUpdate = 0;
        double[] sourceArray = {originalValue, 10.0, 20.0}; // A small, representative array
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(sourceArray);

        // The identity function returns its input argument, so the value in the array should not change.
        DoubleUnaryOperator identityFunction = DoubleUnaryOperator.identity();

        // Act
        // getAndUpdate atomically updates the value and returns the value *before* the update.
        double previousValue = atomicArray.getAndUpdate(indexToUpdate, identityFunction);

        // Assert
        // 1. Verify that the method returned the original value.
        assertEquals("getAndUpdate should return the value before the update",
                originalValue, previousValue, 0.0);

        // 2. Verify that the value in the array is unchanged, as expected with an identity function.
        assertEquals("The value in the array should be unchanged after an identity update",
                originalValue, atomicArray.get(indexToUpdate), 0.0);
    }
}