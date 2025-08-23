package com.google.common.util.concurrent;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link AtomicDoubleArray}.
 */
public class AtomicDoubleArrayTest {

    private static final double DELTA = 0.01;

    @Test
    public void addAndGet_then_getAndAdd_shouldReturnCorrectValues() {
        // Arrange: Create an array initialized with all zeros.
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(8);
        final int index = 0;
        final double valueToAdd = 1775.31884;
        final double initialValue = atomicArray.get(index); // Should be 0.0

        // --- Part 1: Test addAndGet ---
        // Act: Atomically add a value and get the new result.
        double resultOfAddAndGet = atomicArray.addAndGet(index, valueToAdd);

        // Assert: addAndGet should return the *updated* value.
        double expectedValueAfterAdd = initialValue + valueToAdd;
        assertEquals(
                "addAndGet should return the new value after addition.",
                expectedValueAfterAdd,
                resultOfAddAndGet,
                DELTA);
        assertEquals(
                "The array element should be updated to the new value.",
                expectedValueAfterAdd,
                atomicArray.get(index),
                DELTA);

        // --- Part 2: Test getAndAdd ---
        // Act: Atomically get the current value and then add zero.
        // This isolates the "get" behavior of getAndAdd.
        double resultOfGetAndAdd = atomicArray.getAndAdd(index, 0.0);

        // Assert: getAndAdd should return the *previous* value.
        assertEquals(
                "getAndAdd should return the value before the addition.",
                expectedValueAfterAdd, // The value before adding 0.0
                resultOfGetAndAdd,
                DELTA);
        assertEquals(
                "The element's value should be unchanged when adding 0.0.",
                expectedValueAfterAdd,
                atomicArray.get(index),
                DELTA);
    }
}