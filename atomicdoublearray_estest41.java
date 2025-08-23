package com.google.common.util.concurrent;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit tests for {@link AtomicDoubleArray}.
 */
public class AtomicDoubleArrayTest {

    /**
     * Verifies that toString() on an array created from an empty source
     * returns the string representation of an empty list, "[]".
     */
    @Test
    public void toString_withEmptyArray_returnsEmptyBrackets() {
        // Arrange
        AtomicDoubleArray emptyArray = new AtomicDoubleArray(new double[0]);

        // Act
        String result = emptyArray.toString();

        // Assert
        assertEquals("[]", result);
    }
}