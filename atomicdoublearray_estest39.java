package com.google.common.util.concurrent;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link AtomicDoubleArray}.
 */
public class AtomicDoubleArrayTest {

    /**
     * Verifies that the length() method returns the correct size when the
     * AtomicDoubleArray is constructed from an existing double array.
     */
    @Test
    public void length_whenCreatedFromArray_returnsCorrectLength() {
        // Arrange: Create a source array and the AtomicDoubleArray under test.
        double[] sourceArray = new double[2];
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(sourceArray);
        int expectedLength = sourceArray.length;

        // Act: Get the length of the AtomicDoubleArray.
        int actualLength = atomicArray.length();

        // Assert: Verify that the reported length matches the source array's length.
        assertEquals(expectedLength, actualLength);
    }
}