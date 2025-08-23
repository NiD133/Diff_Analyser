package com.google.common.util.concurrent;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link AtomicDoubleArray}.
 */
public class AtomicDoubleArrayTest {

    /**
     * Tests that weakCompareAndSet successfully updates an element and returns true
     * when the expected value matches the current value.
     */
    @Test
    public void weakCompareAndSet_whenValueMatches_updatesElementAndReturnsTrue() {
        // Arrange: Create an array where all elements are initially 0.0.
        double[] initialValues = new double[9];
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(initialValues);

        int indexToUpdate = 0;
        double expectedValue = 0.0;
        double newValue = -1.0;

        // Act: Perform the weak compare-and-set operation.
        boolean wasSuccessful = atomicArray.weakCompareAndSet(indexToUpdate, expectedValue, newValue);

        // Assert: Verify the operation was successful and the value was updated.
        assertTrue("weakCompareAndSet should return true on a successful update.", wasSuccessful);

        // The most crucial assertion: verify the element was actually updated.
        // A small delta is used for floating-point comparisons.
        assertEquals("The element at the updated index should be the new value.",
                newValue, atomicArray.get(indexToUpdate), 0.0);

        // The original test checked the length, which is reasonable to keep.
        assertEquals("Array length should remain unchanged.", 9, atomicArray.length());
    }
}