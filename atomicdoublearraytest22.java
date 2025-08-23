package com.google.common.util.concurrent;

import static org.junit.Assert.assertThrows;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import com.google.common.testing.NullPointerTester;
import java.util.Arrays;
import org.jspecify.annotations.NullUnmarked;

public class AtomicDoubleArrayTestTest22 extends JSR166TestCase {

    // A wide range of special and normal double values for thorough testing.
    private static final double[] VALUES = {
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

    // Unused in the improved test method, but kept for context of the original suite.
    static final long COUNTDOWN = 100000;

    /** The notion of equality used by AtomicDoubleArray. */
    static boolean bitEquals(double x, double y) {
        return Double.doubleToRawLongBits(x) == Double.doubleToRawLongBits(y);
    }

    /** Asserts that two doubles are bit-wise equal. */
    static void assertBitEquals(double x, double y) {
        assertEquals(Double.doubleToRawLongBits(x), Double.doubleToRawLongBits(y));
    }

    // Unused in the improved test method, but kept for context of the original suite.
    class Counter extends CheckedRunnable {
        // ... (original implementation)
    }

    /**
     * Tests that updateAndGet correctly applies a function (summation) and returns the new, updated
     * value, while also persisting that new value in the array.
     */
    public void testUpdateAndGet_appliesSumAndReturnsUpdatedValue() {
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(SIZE);

        // Test against a wide range of values at the boundaries of the array.
        for (int index : new int[] {0, SIZE - 1}) {
            for (double initialValue : VALUES) {
                for (double valueToAdd : VALUES) {
                    // Arrange: Set a starting value in the array.
                    atomicArray.set(index, initialValue);
                    double expectedResult = initialValue + valueToAdd;

                    // Act: Atomically update the value by applying the summation function.
                    double updatedResult =
                        atomicArray.updateAndGet(index, currentValue -> currentValue + valueToAdd);

                    // Assert: The method must return the new value, and the array element must
                    // also be updated to the new value.

                    // We use bit-wise comparison because AtomicDoubleArray's contract is based on
                    // Double.doubleToRawLongBits, which handles all double values (including -0.0
                    // and NaN) predictably.
                    assertBitEquals(expectedResult, updatedResult);
                    assertBitEquals(expectedResult, atomicArray.get(index));
                }
            }
        }
    }
}