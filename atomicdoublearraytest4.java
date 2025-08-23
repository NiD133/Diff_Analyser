package com.google.common.util.concurrent;

import static org.junit.Assert.assertEquals;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import org.jspecify.annotations.NullUnmarked;

/**
 * Note: The original test class contains unused members (e.g., Counter)
 * that seem related to concurrency tests, which are not present here.
 * The class name "AtomicDoubleArrayTestTest4" is also unconventional.
 * This refactoring focuses on improving the provided test case's clarity.
 */
@NullUnmarked
@GwtIncompatible
@J2ktIncompatible
public class AtomicDoubleArrayTestTest4 extends JSR166TestCase {

    // A comprehensive set of double values for testing, including edge cases.
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

    // Unused in the provided test, likely intended for other concurrency tests.
    static final long COUNTDOWN = 100000;

    /**
     * Asserts that two double values are bit-wise equal. This is the equality
     * contract used by AtomicDoubleArray's atomic operations.
     */
    static void assertBitEquals(double expected, double actual) {
        assertEquals(
            "Expected bit-wise equality, but values differed.",
            Double.doubleToRawLongBits(expected),
            Double.doubleToRawLongBits(actual));
    }

    // Unused in the provided test, likely intended for other concurrency tests.
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

    /**
     * Verifies that the constructor that accepts a double[] creates an
     * AtomicDoubleArray with the same length and a bit-for-bit copy of the
     * source array's elements.
     */
    public void testConstructor_withArrayArgument_createsExactCopy() {
        // Arrange: Use a comprehensive set of double values as the source.
        double[] sourceArray = TEST_VALUES;

        // Act: Create the AtomicDoubleArray using the constructor under test.
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(sourceArray);

        // Assert: The new array should have the same length and content.
        assertEquals(
            "Array length should match source array length.",
            sourceArray.length,
            atomicArray.length());

        for (int i = 0; i < sourceArray.length; i++) {
            // We use assertBitEquals because AtomicDoubleArray uses bit-wise
            // comparison for its atomic operations, not standard '==' or .equals().
            assertBitEquals(sourceArray[i], atomicArray.get(i));
        }
    }
}