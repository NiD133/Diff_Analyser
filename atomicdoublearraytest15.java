package com.google.common.util.concurrent;

import static org.junit.Assert.assertEquals;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;

/**
 * Tests for {@link AtomicDoubleArray#addAndGet(int, double)}.
 */
@GwtIncompatible
@J2ktIncompatible
public class AtomicDoubleArrayTest extends JSR166TestCase {

    /**
     * A comprehensive set of double values for testing, including infinities, max/min values,
     * zero, NaN, and other edge cases.
     */
    private static final double[] SPECIAL_DOUBLE_VALUES = {
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
        Float.MAX_VALUE // Included to test precision across types
    };

    /**
     * Asserts that two double values are bit-wise equal, which is the notion of equality used by
     * {@link AtomicDoubleArray}.
     */
    private static void assertBitEquals(double expected, double actual) {
        assertEquals(
            "Expected bit representation " + Double.doubleToRawLongBits(expected)
                + " but got " + Double.doubleToRawLongBits(actual),
            Double.doubleToRawLongBits(expected),
            Double.doubleToRawLongBits(actual));
    }

    /**
     * Verifies that addAndGet correctly updates an element and returns the new value. This test is
     * exhaustive, checking all combinations of special double values for both the initial value and
     * the delta, and testing at boundary indices of the array.
     */
    public void testAddAndGet_withSpecialValues_updatesArrayAndReturnsNewValue() {
        AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
        // Test the first and last elements to check for boundary-related issues.
        int[] indicesToTest = {0, SIZE - 1};

        for (int index : indicesToTest) {
            for (double initialValue : SPECIAL_DOUBLE_VALUES) {
                for (double delta : SPECIAL_DOUBLE_VALUES) {
                    // Arrange: Set the initial value in the array.
                    array.set(index, initialValue);
                    double expectedSum = initialValue + delta;

                    // Act: Add the delta and capture the returned value.
                    double returnedValue = array.addAndGet(index, delta);

                    // Assert: Verify both the returned value and the stored value in the array.
                    // 1. The method must return the new, updated value.
                    assertBitEquals(expectedSum, returnedValue);

                    // 2. The element in the array must be updated to the new value.
                    double valueInArray = array.get(index);
                    assertBitEquals(expectedSum, valueInArray);
                }
            }
        }
    }
}