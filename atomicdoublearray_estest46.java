package com.google.common.util.concurrent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Tests for {@link AtomicDoubleArray}.
 */
public class AtomicDoubleArrayTest {

    @Test
    public void getAndAdd_withNegativeIndex_throwsIndexOutOfBoundsException() {
        // Arrange: Create an array and define an invalid (negative) index.
        // The array size and value to add are arbitrary for this test.
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(10);
        int negativeIndex = -200;
        double valueToAdd = 1.0;

        // Act & Assert: Verify that calling getAndAdd with the negative index throws the
        // correct exception with the expected message.
        IndexOutOfBoundsException thrown = assertThrows(
            IndexOutOfBoundsException.class,
            () -> atomicArray.getAndAdd(negativeIndex, valueToAdd)
        );

        // The exception message format "index <index>" is part of the contract
        // of the underlying java.util.concurrent.atomic.AtomicLongArray.
        assertEquals("index " + negativeIndex, thrown.getMessage());
    }
}