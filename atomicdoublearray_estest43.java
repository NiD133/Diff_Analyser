package com.google.common.util.concurrent;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test for {@link AtomicDoubleArray}.
 */
public class AtomicDoubleArrayTest {

    /**
     * Test names should describe the behavior being tested. This test verifies that
     * lazySet updates an element's value as expected in a single-threaded context.
     *
     * The original test was weak because it set a value to what it already was (0.0)
     * and only asserted the array's length, not the effect of the lazySet call.
     */
    @Test
    public void lazySet_shouldUpdateValueAtIndex() {
        // Arrange: Set up the test scenario with clear, descriptive variable names.
        // Using a non-zero initial value makes the update more explicit.
        double[] initialArray = {10.0, 20.0};
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(initialArray);
        
        int indexToUpdate = 0;
        double newValue = 123.45;

        // Act: Perform the action to be tested.
        atomicArray.lazySet(indexToUpdate, newValue);

        // Assert: Verify the outcome.
        // 1. Check that the value was updated correctly. A subsequent get() in the same
        //    thread is guaranteed to observe the write from lazySet().
        assertEquals(newValue, atomicArray.get(indexToUpdate), 0.0);
        
        // 2. The original test's assertion is kept as a valid, but secondary, check
        //    to ensure the array's length remains unchanged.
        assertEquals(initialArray.length, atomicArray.length());
    }
}