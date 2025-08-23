package com.google.common.util.concurrent;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.function.DoubleBinaryOperator;
import org.junit.Test;

/**
 * Tests for {@link AtomicDoubleArray}.
 */
public class AtomicDoubleArrayTest {

    /**
     * Tests that accumulateAndGet correctly applies the given function to the current value and the
     * given update value, updates the element at the specified index, and returns the new value.
     */
    @Test
    public void accumulateAndGet_appliesOperator_returnsNewValue() {
        // Arrange
        // The array is initialized with all elements as 0.0.
        AtomicDoubleArray array = new AtomicDoubleArray(5);
        int indexToUpdate = 2;
        double initialValueAtIndex = 0.0; // The default value
        double updateValue = 42.0;
        double expectedNewValue = -100.0;

        // Create a mock accumulator function.
        DoubleBinaryOperator mockAccumulator = mock(DoubleBinaryOperator.class);

        // Define the mock's behavior: when called with the current value at the index
        // and the provided update value, it should return our expected new value.
        when(mockAccumulator.applyAsDouble(initialValueAtIndex, updateValue))
                .thenReturn(expectedNewValue);

        // Act
        // Call the method under test.
        double returnedValue = array.accumulateAndGet(indexToUpdate, updateValue, mockAccumulator);

        // Assert
        // 1. The method should return the new value calculated by the operator.
        assertEquals(expectedNewValue, returnedValue, 0.0);

        // 2. The value in the array at the specified index should be updated to the new value.
        assertEquals(expectedNewValue, array.get(indexToUpdate), 0.0);

        // 3. Verify that the accumulator function was called exactly once with the correct arguments.
        verify(mockAccumulator).applyAsDouble(initialValueAtIndex, updateValue);
    }
}