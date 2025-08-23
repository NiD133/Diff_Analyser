package com.google.common.util.concurrent;

import org.junit.Test;
import java.util.function.DoubleBinaryOperator;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Understandable tests for {@link AtomicDoubleArray}.
 */
public class AtomicDoubleArrayTest {

    /**
     * Verifies that accumulateAndGet correctly applies the given accumulator function,
     * updates the element at the specified index, and returns the new value.
     */
    @Test
    public void accumulateAndGet_shouldUpdateElementAndReturnNewValue() {
        // Arrange
        // 1. Set up the initial state of the array.
        double[] initialValues = {0.0, 10.0, 20.0};
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(initialValues);

        // 2. Define test parameters with descriptive names.
        final int indexToUpdate = 0;
        final double operand = 1814.345; // The 'x' value for the accumulator.
        final double expectedResult = 50.0; // The fixed value our mock accumulator will return.

        // 3. Mock the accumulator function to control its behavior.
        //    This isolates the test to the logic of `accumulateAndGet` itself,
        //    ensuring it correctly uses the result from the provided function.
        DoubleBinaryOperator mockAccumulator = mock(DoubleBinaryOperator.class);
        when(mockAccumulator.applyAsDouble(anyDouble(), anyDouble())).thenReturn(expectedResult);

        // Act
        // Execute the method under test.
        double returnedValue = atomicArray.accumulateAndGet(indexToUpdate, operand, mockAccumulator);

        // Assert
        // The method should return the new value produced by the accumulator.
        assertEquals(
                "The method should return the result from the accumulator function.",
                expectedResult,
                returnedValue,
                0.0);

        // The element in the array should also be updated to the new value.
        assertEquals(
                "The array element at the specified index should be updated.",
                expectedResult,
                atomicArray.get(indexToUpdate),
                0.0);
    }
}