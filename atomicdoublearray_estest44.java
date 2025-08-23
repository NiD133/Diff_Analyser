package com.google.common.util.concurrent;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link AtomicDoubleArray}.
 */
public class AtomicDoubleArray_ESTestTest44 { // Original class name retained for context

    /**
     * Verifies that the length() method returns the correct size when the
     * AtomicDoubleArray is constructed from a source double array.
     */
    @Test
    public void length_shouldReturnCorrectSize_whenConstructedFromArray() {
        // Arrange
        double[] sourceArray = new double[6];
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(sourceArray);

        // Act
        int actualLength = atomicArray.length();

        // Assert
        assertEquals(sourceArray.length, actualLength);
    }
}