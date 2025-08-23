package com.google.common.util.concurrent;

import org.junit.Test;
import java.util.function.DoubleBinaryOperator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * This test verifies the behavior of a sequence of atomic operations on an AtomicDoubleArray.
 * It ensures that:
 * 1. `getAndAccumulate` correctly updates an element's value and returns the *previous* value.
 * 2. A subsequent `compareAndSet` operation using the original value fails, which confirms
 *    that the element's value was indeed modified by the first operation.
 */
public class AtomicDoubleArrayTest {

    @Test
    public void getAndAccumulate_updatesValueAndReturnsPrevious_causingSubsequentCasToFail() {
        // Arrange: Set up the initial state and mock dependencies.
        final int indexToTest = 0;
        final double initialValue = 0.0;
        final double accumulatedValue = -1.0;
        final double accumulatorInput = 100.0; // An arbitrary value for the accumulator function.

        double[] initialArray = {initialValue, 1.0, 2.0};
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(initialArray);

        // Create a mock accumulator that always returns a fixed value.
        DoubleBinaryOperator mockAccumulator = mock(DoubleBinaryOperator.class);
        doReturn(accumulatedValue)
                .when(mockAccumulator)
                .applyAsDouble(initialValue, accumulatorInput);

        // Act 1: Call getAndAccumulate. This should update the value at the index
        // and return the value that was present *before* the update.
        double previousValue = atomicArray.getAndAccumulate(indexToTest, accumulatorInput, mockAccumulator);

        // Assert 1: Verify the immediate results of getAndAccumulate.
        assertEquals("getAndAccumulate should return the previous value.",
                initialValue, previousValue, 0.0);
        assertEquals("The value at the index should be updated by the accumulator.",
                accumulatedValue, atomicArray.get(indexToTest), 0.0);
        verify(mockAccumulator).applyAsDouble(initialValue, accumulatorInput);

        // Act 2: Attempt to compare-and-set using the original value. This should fail
        // because the value has been updated to `accumulatedValue`.
        boolean casSucceeded = atomicArray.compareAndSet(indexToTest, initialValue, 99.0);

        // Assert 2: Verify that the compare-and-set operation failed as expected.
        assertFalse("compareAndSet with the old value should fail after accumulation.", casSucceeded);
        assertEquals("The value should remain unchanged after the failed CAS.",
                accumulatedValue, atomicArray.get(indexToTest), 0.0);
    }
}