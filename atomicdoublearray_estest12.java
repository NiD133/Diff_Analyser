package com.google.common.util.concurrent;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.function.DoubleBinaryOperator;
import org.junit.Test;

/**
 * Tests for {@link AtomicDoubleArray}.
 */
public class AtomicDoubleArrayTest {

    @Test
    public void getAndAccumulate_shouldReturnPreviousValueAndUpdateArray() {
        // Arrange
        final int indexToUpdate = 1;
        final double initialValue = 354.258;
        final double newValueFromAccumulator = -1.0;

        double[] initialArray = {0.0, initialValue, 0.0};
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(initialArray);

        // Create a mock accumulator function that always returns a fixed new value.
        DoubleBinaryOperator mockAccumulator = mock(DoubleBinaryOperator.class);
        when(mockAccumulator.applyAsDouble(anyDouble(), anyDouble())).thenReturn(newValueFromAccumulator);

        // Act
        // Call the method under test. It should return the value at the index *before* the update.
        double previousValue = atomicArray.getAndAccumulate(indexToUpdate, -2032.2, mockAccumulator);

        // Assert
        // 1. Verify that the method returned the original value.
        assertEquals("getAndAccumulate should return the value before the update",
                initialValue, previousValue, 0.01);

        // 2. Verify that the value in the array was updated to the result of the accumulator.
        assertEquals("The value in the array should be updated by the accumulator",
                newValueFromAccumulator, atomicArray.get(indexToUpdate), 0.01);
    }
}