package com.google.common.util.concurrent;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the AtomicDoubleArray class.
 */
public class AtomicDoubleArrayTest {

    /**
     * Verifies that getAndSet() correctly returns the previous value at a given index
     * and successfully updates that index with the new value.
     */
    @Test
    public void getAndSet_returnsOldValue_andSetsNewValue() {
        // Arrange: Define the initial state and the values for the test.
        int indexToUpdate = 1;
        double initialValue = 540.0;
        double newValue = 2806.574;
        double[] sourceArray = {0.0, initialValue, 0.0, 0.0};

        AtomicDoubleArray atomicArray = new AtomicDoubleArray(sourceArray);

        // Act: Execute the method under test.
        double returnedValue = atomicArray.getAndSet(indexToUpdate, newValue);

        // Assert: Verify the results.
        // Check that the method returned the original value.
        assertEquals("The method should return the value that was present before the update.", initialValue, returnedValue, 0.01);

        // Check that the value in the array was updated.
        assertEquals("The value at the specified index should be updated to the new value.", newValue, atomicArray.get(indexToUpdate), 0.01);
    }
}