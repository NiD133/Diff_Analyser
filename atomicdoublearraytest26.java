package com.google.common.util.concurrent;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

/**
 * Tests for {@link AtomicDoubleArray#toString()}.
 */
public class AtomicDoubleArrayToStringTest extends JSR166TestCase {

    // A comprehensive set of double values to test string representation,
    // including special cases like NaN, infinities, and zeros.
    private static final double[] TEST_VALUES = {
        Double.NEGATIVE_INFINITY,
        -Double.MAX_VALUE,
        (double) Long.MIN_VALUE,
        (double) Integer.MIN_VALUE,
        -Math.PI,
        -1.0,
        -Double.MIN_VALUE,
        -0.0,
        +0.0,
        Double.MIN_VALUE,
        1.0,
        Math.PI,
        (double) Integer.MAX_VALUE,
        (double) Long.MAX_VALUE,
        Double.MAX_VALUE,
        Double.POSITIVE_INFINITY,
        Double.NaN,
        Float.MAX_VALUE
    };

    public void testToString_withPopulatedArray_returnsCorrectRepresentation() {
        // Arrange
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(TEST_VALUES);
        String expected = Arrays.toString(TEST_VALUES);

        // Act
        String actual = atomicArray.toString();

        // Assert
        assertEquals(
            "The string representation should match the one from Arrays.toString()",
            expected,
            actual);
    }

    public void testToString_forEmptyArrayFromSizeConstructor_returnsEmptyBrackets() {
        // Arrange: Test the constructor that takes a size.
        AtomicDoubleArray emptyArray = new AtomicDoubleArray(0);

        // Act
        String result = emptyArray.toString();

        // Assert
        assertEquals("[]", result);
    }

    public void testToString_forEmptyArrayFromArrayConstructor_returnsEmptyBrackets() {
        // Arrange: Test the constructor that takes an initial array.
        AtomicDoubleArray emptyArray = new AtomicDoubleArray(new double[0]);

        // Act
        String result = emptyArray.toString();

        // Assert
        assertEquals("[]", result);
    }
}