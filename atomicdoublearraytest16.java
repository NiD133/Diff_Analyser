package com.google.common.util.concurrent;

import static org.junit.Assert.assertEquals;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import java.util.function.DoubleBinaryOperator;
import org.jspecify.annotations.NullUnmarked;

@GwtIncompatible
@J2ktIncompatible
@NullUnmarked
public class AtomicDoubleArrayTestTest16 extends JSR166TestCase {

    /**
     * A wide range of doubles for comprehensive testing, including infinities,
     * max/min values, zero, subnormals, and NaN.
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
     * Asserts that two double values are bitwise-equal, which is the
     * equality contract used by AtomicDoubleArray. A custom message is used
     * for clearer test failure reports.
     */
    private static void assertBitEquals(String message, double expected, double actual) {
        assertEquals(message, Double.doubleToRawLongBits(expected), Double.doubleToRawLongBits(actual));
    }

    /**
     * Verifies that getAndAccumulate with a sum function returns the previous value
     * and correctly updates the element to the new sum.
     *
     * <p>This test is performed across a comprehensive set of special and boundary
     * double values to ensure correctness in edge cases.
     */
    public void testGetAndAccumulate_withSum_returnsOldValueAndStoresNewSum() {
        // Test on the first and last elements of the array as representative indices.
        int[] indicesToTest = {0, SIZE - 1};

        for (int index : indicesToTest) {
            for (double initialValue : TEST_VALUES) {
                for (double valueToAdd : TEST_VALUES) {
                    // Arrange
                    AtomicDoubleArray atomicArray = new AtomicDoubleArray(SIZE);
                    atomicArray.set(index, initialValue);
                    DoubleBinaryOperator sum = Double::sum;

                    // Act
                    double returnedValue = atomicArray.getAndAccumulate(index, valueToAdd, sum);

                    // Assert
                    // 1. The method must return the value as it was *before* the operation.
                    assertBitEquals(
                        "getAndAccumulate should return the previous value",
                        initialValue,
                        returnedValue);

                    // 2. The element in the array must be updated to the new sum.
                    double finalValue = atomicArray.get(index);
                    double expectedFinalValue = initialValue + valueToAdd;
                    assertBitEquals(
                        "The element should be updated to the new sum",
                        expectedFinalValue,
                        finalValue);
                }
            }
        }
    }
}