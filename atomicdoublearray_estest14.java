package com.google.common.util.concurrent;

import org.junit.Test;
import java.util.function.DoubleBinaryOperator;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link AtomicDoubleArray}.
 */
public class AtomicDoubleArrayTest {

    /**
     * Verifies that getAndAccumulate returns the previous value at an index
     * and updates the element to the value produced by the accumulator function.
     */
    @Test
    public void getAndAccumulate_shouldReturnPreviousValue_andUpdateElementWithAccumulatorResult() {
        // Arrange
        double[] initialValues = {10.0, 20.0, 30.0};
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(initialValues);

        int indexToUpdate = 1;
        double expectedPreviousValue = 20.0;
        double valueFromAccumulator = 99.0; // The new value to be set in the array.
        
        // Create a mock accumulator function that always returns a fixed result.
        // The input arguments to the function don't matter for this test's purpose.
        DoubleBinaryOperator mockAccumulator = mock(DoubleBinaryOperator.class);
        when(mockAccumulator.applyAsDouble(anyDouble(), anyDouble())).thenReturn(valueFromAccumulator);

        // Act
        // The 'x' parameter (5.0) is arbitrary since the mock controls the outcome.
        double returnedValue = atomicArray.getAndAccumulate(indexToUpdate, 5.0, mockAccumulator);

        // Assert
        // 1. Verify the method returned the value at the index *before* the update.
        assertEquals(
            "getAndAccumulate should return the value before the update",
            expectedPreviousValue,
            returnedValue,
            0.0
        );

        // 2. Verify the value at the index was updated to the result from the accumulator.
        double valueAfterUpdate = atomicArray.get(indexToUpdate);
        assertEquals(
            "The element at the index should be updated to the accumulator's result",
            valueFromAccumulator,
            valueAfterUpdate,
            0.0
        );
    }
}