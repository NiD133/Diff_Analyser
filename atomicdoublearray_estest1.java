package com.google.common.util.concurrent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit tests for {@link AtomicDoubleArray}.
 */
public class AtomicDoubleArrayTest {

    @Test
    public void compareAndSet_succeedsWhenValueIsExpected() {
        // Arrange
        final int indexToUpdate = 5;
        final double initialValue = 0.0; // All elements are initialized to 0.0 by default
        final double newValue = 123.45;
        AtomicDoubleArray array = new AtomicDoubleArray(10);

        // Act
        boolean wasSuccessful = array.compareAndSet(indexToUpdate, initialValue, newValue);

        // Assert
        assertTrue("compareAndSet should return true on a successful update.", wasSuccessful);
        assertEquals(
            "The value at the index should be updated to the new value.",
            newValue,
            array.get(indexToUpdate),
            0.0); // The third argument is the tolerance for double comparison
    }
}