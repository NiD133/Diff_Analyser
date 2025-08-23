package com.google.common.util.concurrent;

import static java.lang.Math.max;
import static org.junit.Assert.assertThrows;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import com.google.common.testing.NullPointerTester;
import java.util.Arrays;
import org.jspecify.annotations.NullUnmarked;

public class AtomicDoubleArrayTestTest19 extends JSR166TestCase {

    // A selection of values to test, including infinities, NaN, zeros, and other boundary values.
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

    // Helper methods and other test-related members from the original class are omitted for brevity.
    // ...

    /**
     * Verifies that accumulateAndGet with `Double::max` correctly updates the element to the
     * maximum of its current value and a given value, and also returns that new maximum value.
     * This is tested for a wide range of special double values and at the boundary indices of the
     * array.
     */
    public void testAccumulateAndGet_withMaxFunction_updatesToMaxValueAndReturnsIt() {
        // Arrange
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(SIZE);
        // We test the first and last elements as representative boundary cases.
        int[] indicesToTest = {0, SIZE - 1};

        for (int index : indicesToTest) {
            // Test all combinations of initial values and values to accumulate.
            for (double initialValue : TEST_VALUES) {
                for (double valueToAccumulate : TEST_VALUES) {
                    atomicArray.set(index, initialValue);
                    double expectedResult = max(initialValue, valueToAccumulate);

                    // Act
                    double returnedValue =
                        atomicArray.accumulateAndGet(index, valueToAccumulate, Double::max);

                    // Assert
                    // 1. The method should return the new, updated value.
                    assertBitEquals(expectedResult, returnedValue);
                    // 2. The element in the array should also be updated to the new value.
                    assertBitEquals(expectedResult, atomicArray.get(index));
                }
            }
        }
    }

    // --- Helper methods and fields from the original test, required for compilation ---
    // (These are assumed to be present in the actual test class)
    private static final double[] VALUES = TEST_VALUES;

    static void assertBitEquals(double x, double y) {
        assertEquals(Double.doubleToRawLongBits(x), Double.doubleToRawLongBits(y));
    }
}