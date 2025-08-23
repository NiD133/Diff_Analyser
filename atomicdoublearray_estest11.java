package com.google.common.util.concurrent;

import org.junit.Test;
import java.util.function.DoubleBinaryOperator;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests the interaction between atomic operations on {@link AtomicDoubleArray}.
 */
public class AtomicDoubleArrayConcurrencyTest {

    /**
     * Verifies that a value updated by getAndAccumulate is correctly observed
     * by a subsequent getAndAdd operation on the same element.
     */
    @Test
    public void getAndAccumulate_updatesValue_whichIsObservedBySubsequentGetAndAdd() {
        // Arrange
        final int index = 3;
        final double initialValue = 0.0; // Default value for elements in a new array
        final double accumulatorArg = 5.0;
        final double newValueFromAccumulator = -100.0;

        AtomicDoubleArray array = new AtomicDoubleArray(10);
        // Precondition: The initial value at the test index should be the default.
        assertEquals("Precondition: Initial value at index should be 0.0",
                initialValue, array.get(index), 0.0);

        DoubleBinaryOperator accumulator = mock(DoubleBinaryOperator.class);
        // Configure the mock to return a specific value when called with the current
        // value (initialValue) and the provided argument (accumulatorArg).
        when(accumulator.applyAsDouble(initialValue, accumulatorArg)).thenReturn(newValueFromAccumulator);

        // Act: First operation - getAndAccumulate
        // This updates the element at 'index' using the accumulator and returns the *previous* value.
        double previousValue = array.getAndAccumulate(index, accumulatorArg, accumulator);

        // Assert: Verify the result of getAndAccumulate
        assertEquals("getAndAccumulate should return the value before the update.",
                initialValue, previousValue, 0.0);
        assertEquals("The array element should be updated to the value from the accumulator.",
                newValueFromAccumulator, array.get(index), 0.0);

        // Act: Second operation - getAndAdd
        // This adds a delta (0.0) to the *current* value and returns the value *before* the addition.
        double currentValue = array.getAndAdd(index, 0.0);

        // Assert: Verify the result of getAndAdd
        assertEquals("getAndAdd should return the updated value from the previous step.",
                newValueFromAccumulator, currentValue, 0.0);
        assertEquals("The array element should remain unchanged after adding 0.0.",
                newValueFromAccumulator, array.get(index), 0.0);
    }
}