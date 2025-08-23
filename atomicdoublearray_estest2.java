package com.google.common.util.concurrent;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Tests for {@link AtomicDoubleArray}.
 */
public class AtomicDoubleArrayTest {

    @Test
    public void weakCompareAndSet_shouldReturnFalse_whenExpectedValueIsIncorrect() {
        // Arrange
        final double initialValue = -1583.8;
        final double incorrectExpectedValue = 0.0;
        final double newValue = 690.7;
        final int index = 0;

        // The array is initialized with a specific value at the target index.
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(new double[]{initialValue});

        // Act
        // Attempt to update the value, but provide an expected value that does not match
        // the actual current value.
        boolean wasSuccessful = atomicArray.weakCompareAndSet(index, incorrectExpectedValue, newValue);

        // Assert
        // The operation should fail because the expected value (0.0) does not match the actual value.
        assertFalse("weakCompareAndSet should return false for a mismatched expected value.", wasSuccessful);

        // Verify that the value at the index remains unchanged.
        assertEquals("The value should not have been updated on failure.", initialValue, atomicArray.get(index), 0.0);
    }
}