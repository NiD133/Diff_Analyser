package com.google.common.util.concurrent;

import static org.junit.Assert.assertEquals;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import java.util.Arrays;
import org.jspecify.annotations.NullUnmarked;

@NullUnmarked
@GwtIncompatible
@J2ktIncompatible
// The test class name is kept for context, but in a real scenario,
// it might be merged into a more general AtomicDoubleArrayTest.
public class AtomicDoubleArrayTestTest21 extends JSR166TestCase {

    // A comprehensive set of double values to test, including edge cases.
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
     * Asserts that two doubles are bitwise-equal, which is the equality contract
     * for AtomicDoubleArray.
     */
    static void assertBitEquals(double expected, double actual) {
        assertEquals(Double.doubleToRawLongBits(expected), Double.doubleToRawLongBits(actual));
    }

    /**
     * Verifies that getAndUpdate returns the previous value and correctly applies the update function.
     *
     * <p>This test exhaustively checks the behavior against a wide range of double values,
     * including infinities, NaNs, and zeros, at the boundaries of the array.
     */
    public void testGetAndUpdate_returnsPreviousValueAndAppliesUpdate() {
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(SIZE);
        // Test at the first and last indices to check for boundary errors.
        int[] indicesToTest = {0, SIZE - 1};

        for (int index : indicesToTest) {
            for (double initialValue : TEST_VALUES) {
                for (double valueToSubtract : TEST_VALUES) {
                    // Arrange: Set the element to a known initial state.
                    atomicArray.set(index, initialValue);
                    double expectedNewValue = initialValue - valueToSubtract;

                    // Act: Atomically update the value and get the old one.
                    double returnedValue =
                        atomicArray.getAndUpdate(index, currentValue -> currentValue - valueToSubtract);

                    // Assert: The method should return the value before the update.
                    assertBitEquals(initialValue, returnedValue);

                    // Assert: The element in the array should now hold the updated value.
                    double actualNewValue = atomicArray.get(index);
                    assertBitEquals(expectedNewValue, actualNewValue);
                }
            }
        }
    }

    // The following members are part of the original test class structure and are kept for
    // completeness, though they are not used by the refactored test method.
    // They are likely used by other tests in the original full test suite.

    static final long COUNTDOWN = 100000;

    /** The notion of equality used by AtomicDoubleArray. */
    static boolean bitEquals(double x, double y) {
        return Double.doubleToRawLongBits(x) == Double.doubleToRawLongBits(y);
    }

    class Counter extends CheckedRunnable {
        final AtomicDoubleArray aa;
        volatile long counts;

        Counter(AtomicDoubleArray a) {
            aa = a;
        }

        @Override
        public void realRun() {
            for (; ; ) {
                boolean done = true;
                for (int i = 0; i < aa.length(); i++) {
                    double v = aa.get(i);
                    assertTrue(v >= 0);
                    if (v != 0) {
                        done = false;
                        if (aa.compareAndSet(i, v, v - 1.0)) {
                            ++counts;
                        }
                    }
                }
                if (done) {
                    break;
                }
            }
        }
    }
}