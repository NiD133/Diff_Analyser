package com.google.common.util.concurrent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Tests for {@link AtomicDoubleArray}.
 */
public class AtomicDoubleArrayTest {

    @Test
    public void getAndSet_shouldThrowException_whenIndexIsOutOfBounds() {
        // Arrange: Create an array of a specific size.
        int arraySize = 3;
        AtomicDoubleArray array = new AtomicDoubleArray(arraySize);
        int outOfBoundsIndex = 3; // Valid indices are 0, 1, 2.
        double valueToSet = 99.0;

        // Act & Assert: Verify that calling getAndSet with an out-of-bounds index
        // throws IndexOutOfBoundsException.
        IndexOutOfBoundsException thrown = assertThrows(
                IndexOutOfBoundsException.class,
                () -> array.getAndSet(outOfBoundsIndex, valueToSet));

        // Optional but recommended: Assert on the exception message for more precise verification.
        assertEquals("index " + outOfBoundsIndex, thrown.getMessage());
    }
}