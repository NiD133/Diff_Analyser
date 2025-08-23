package com.google.common.util.concurrent;

import org.junit.Test;
import java.util.function.DoubleBinaryOperator;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link AtomicDoubleArray}.
 * This class contains the improved version of the original test case.
 */
public class AtomicDoubleArrayTest {

    /**
     * Tests that getAndAccumulate returns the value at an index *before* the update
     * and that the element is subsequently updated using the provided function.
     */
    @Test
    public void getAndAccumulate_shouldReturnPreviousValueAndUpdateElement() {
        // Arrange
        final int index = 0;
        final double initialValue = 100.0;
        final double valueToAccumulate = 50.0;
        final double expectedFinalValue = 2.0; // The result of initialValue / valueToAccumulate

        double[] initialArray = {initialValue, 200.0, 300.0};
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(initialArray);

        // Create a mock accumulator function that divides the current value by the new value.
        DoubleBinaryOperator mockAccumulator = mock(DoubleBinaryOperator.class);
        doReturn(expectedFinalValue)
                .when(mockAccumulator)
                .applyAsDouble(initialValue, valueToAccumulate);

        // Act
        // Call getAndAccumulate, which should return the value *before* the accumulation.
        double previousValue = atomicArray.getAndAccumulate(index, valueToAccumulate, mockAccumulator);

        // Assert
        // 1. Verify that the returned value is the original value at the index.
        assertEquals(
                "getAndAccumulate should return the value before the update.",
                initialValue,
                previousValue,
                0.0);

        // 2. Verify that the accumulator function was called with the correct arguments.
        verify(mockAccumulator).applyAsDouble(initialValue, valueToAccumulate);

        // 3. Verify that the value in the array was updated to the result of the accumulator.
        assertEquals(
                "The element in the array should be updated to the accumulated value.",
                expectedFinalValue,
                atomicArray.get(index),
                0.0);
    }
}