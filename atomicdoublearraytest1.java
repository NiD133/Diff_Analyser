package com.google.common.util.concurrent;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import com.google.common.testing.NullPointerTester;
import org.jspecify.annotations.NullUnmarked;

/**
 * Tests for {@link AtomicDoubleArray}. This class includes tests for API contracts like null-safety
 * and helpers for concurrency tests.
 */
@NullUnmarked
public class AtomicDoubleArrayTest extends JSR166TestCase {

    /** A collection of special double values for testing edge cases. */
    private static final double[] SPECIAL_VALUES = {
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

    /** A large number used as a countdown for concurrency tests. */
    static final long CONCURRENCY_TEST_COUNTDOWN = 100000;

    /**
     * The notion of equality used by AtomicDoubleArray, which compares the raw bit representations.
     */
    static boolean bitEquals(double x, double y) {
        return Double.doubleToRawLongBits(x) == Double.doubleToRawLongBits(y);
    }

    /** Asserts that two doubles are bit-wise equal, consistent with AtomicDoubleArray's logic. */
    static void assertBitEquals(double x, double y) {
        assertEquals(Double.doubleToRawLongBits(x), Double.doubleToRawLongBits(y));
    }

    /**
     * A worker for concurrency tests that repeatedly iterates over an {@link AtomicDoubleArray},
     * decrementing each positive element by 1.0 until all elements are zero. It counts the number of
     * successful decrements it performs.
     */
    static class ConcurrentDecrementer extends CheckedRunnable {

        private final AtomicDoubleArray atomicArray;
        volatile long successfulDecrements;

        ConcurrentDecrementer(AtomicDoubleArray array) {
            this.atomicArray = array;
        }

        @Override
        public void realRun() {
            // Loop until all elements in the array have been successfully decremented to zero.
            while (true) {
                boolean allElementsAreZero = true;
                // Iterate through the array, attempting to decrement each positive element.
                for (int i = 0; i < atomicArray.length(); i++) {
                    double currentValue = atomicArray.get(i);
                    assertTrue("Array values should not be negative", currentValue >= 0);

                    if (currentValue > 0) {
                        allElementsAreZero = false;
                        // Attempt to decrement the value atomically.
                        if (atomicArray.compareAndSet(i, currentValue, currentValue - 1.0)) {
                            successfulDecrements++;
                        }
                    }
                }
                // If a full pass over the array finds no positive values, the work is done.
                if (allElementsAreZero) {
                    break;
                }
            }
        }
    }

    /**
     * Verifies that all public APIs of AtomicDoubleArray correctly handle null arguments by throwing a
     * NullPointerException.
     */
    @J2ktIncompatible
    @GwtIncompatible
    public void testPublicApi_nullArguments_throwsNpe() {
        // NullPointerTester is a Guava utility that automatically checks for NPEs.
        NullPointerTester tester = new NullPointerTester();
        tester.testAllPublicStaticMethods(AtomicDoubleArray.class);
        tester.testAllPublicConstructors(AtomicDoubleArray.class);
        tester.testAllPublicInstanceMethods(new AtomicDoubleArray(1));
    }
}