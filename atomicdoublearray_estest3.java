package com.google.common.util.concurrent;

import static org.junit.Assert.assertEquals;

import java.util.function.DoubleUnaryOperator;
import org.junit.Test;

/**
 * Unit tests for {@link AtomicDoubleArray}.
 */
public class AtomicDoubleArrayTest {

    /**
     * This test verifies a sequence of atomic operations. First, it calls {@code getAndAdd}
     * to modify a value and checks its return value. Then, it calls {@code updateAndGet} on
     * the same element to ensure the state is correctly handled between operations.
     */
    @Test
    public void getAndAdd_followedByUpdateAndGet_modifiesArrayAndReturnsCorrectValues() {
        // Arrange: Create an array where all elements are initially 0.0.
        final int indexToUpdate = 4;
        final double valueToAdd = 4.0;
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(12);

        // Act & Assert: Part 1 - Test getAndAdd
        // This should return the old value (0.0) and update the element at the index to 4.0.
        double valueBeforeAdd = atomicArray.getAndAdd(indexToUpdate, valueToAdd);

        assertEquals("getAndAdd should return the value before the addition.", 0.0, valueBeforeAdd, 0.0);
        assertEquals("The value at the index should be updated after getAndAdd.", 4.0, atomicArray.get(indexToUpdate), 0.0);

        // Act & Assert: Part 2 - Test updateAndGet on the modified array
        // The identity function x -> x should not change the current value (4.0).
        // updateAndGet should return the new (which is the same) value.
        DoubleUnaryOperator identityOperator = DoubleUnaryOperator.identity();
        double valueAfterUpdate = atomicArray.updateAndGet(indexToUpdate, identityOperator);

        assertEquals("updateAndGet should return the updated value.", 4.0, valueAfterUpdate, 0.0);
        assertEquals("The value should be unchanged after applying the identity function.", 4.0, atomicArray.get(indexToUpdate), 0.0);
    }
}