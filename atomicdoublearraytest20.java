package com.google.common.util.concurrent;

import static org.junit.Assert.assertEquals;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import org.jspecify.annotations.NullUnmarked;

/**
 * Tests for {@link AtomicDoubleArray}.
 * This class is a continuation of tests from the JSR-166 test suite.
 */
@GwtIncompatible
@J2ktIncompatible
@NullUnmarked
public class AtomicDoubleArrayTestTest20 extends JSR166TestCase {

    /**
     * A set of representative double values, including edge cases like infinities,
     * NaN, zero, and min/max values, used for thorough testing.
     */
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

    /**
     * Asserts that two double values are bit-wise equal, which is the equality contract
     * used by AtomicDoubleArray. This is necessary because standard `==` and `.equals()`
     * have different semantics for values like NaN and -0.0.
     */
    static void assertBitEquals(double expected, double actual) {
        assertEquals(Double.doubleToRawLongBits(expected), Double.doubleToRawLongBits(actual));
    }

    /**
     * Verifies that getAndUpdate() returns the previous value and correctly applies the
     * update function to the element in the array.
     */
    public void testGetAndUpdate_returnsPreviousValueAndAppliesUpdate() {
        // Arrange: Create an array and define which indices to test.
        // Testing the first and last elements is a good smoke test for boundary conditions.
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(SIZE);
        int[] indicesToTest = {0, SIZE - 1};

        // Act & Assert:
        // Iterate through a comprehensive set of initial values and values to add.
        // This ensures the method works correctly with normal numbers, infinities, and NaN.
        for (int i : indicesToTest) {
            for (double initialValue : TEST_VALUES) {
                for (double valueToAdd : TEST_VALUES) {
                    // Arrange for this specific test case
                    atomicArray.set(i, initialValue);

                    // Act: Perform the get-and-update operation.
                    double returnedValue = atomicArray.getAndUpdate(i, current -> current + valueToAdd);

                    // Assert: Verify the returned value and the new state of the array.
                    double expectedUpdatedValue = initialValue + valueToAdd;

                    // 1. The method must return the value as it was *before* the update.
                    assertBitEquals(initialValue, returnedValue);

                    // 2. The element in the array must be updated to the new value.
                    assertBitEquals(expectedUpdatedValue, atomicArray.get(i));
                }
            }
        }
    }
}