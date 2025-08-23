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

    @Test
    public void accumulateAndGet_shouldApplyFunctionAndReturnUpdatedValue() {
        // Arrange
        // Create an array with a single element, initially 0.0.
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(1);
        double updateValue = 5.0; // The 'x' parameter for the accumulator function.
        double expectedResult = 42.0; // A distinct value the mock function will return.

        // Create a mock accumulator function. We don't care about the inputs in this test,
        // only that the function's result is correctly returned by accumulateAndGet.
        DoubleBinaryOperator mockAccumulator = mock(DoubleBinaryOperator.class);
        when(mockAccumulator.applyAsDouble(anyDouble(), anyDouble())).thenReturn(expectedResult);

        // Act
        // Call the method under test. It should apply the mockAccumulator and return its result.
        double actualResult = atomicArray.accumulateAndGet(0, updateValue, mockAccumulator);

        // Assert
        // Verify that the method returned the value produced by our mock function.
        // A delta of 0.0 asserts for bit-wise equality.
        assertEquals(expectedResult, actualResult, 0.0);
    }
}