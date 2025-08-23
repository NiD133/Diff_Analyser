package com.google.common.util.concurrent;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import org.jspecify.annotations.NullUnmarked;

/**
 * Tests for the behavior of get() following lazySet() on an {@link AtomicDoubleArray}.
 */
@GwtIncompatible
@J2ktIncompatible
@NullUnmarked
public class AtomicDoubleArrayTestTest9 extends JSR166TestCase {

    /**
     * A comprehensive set of double values for testing, including infinities, NaN, zeros,
     * and other edge cases.
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
        Double.NaN
    };

    /**
     * Asserts that two double values are bitwise-equal, which is the equality contract
     * for AtomicDoubleArray's atomic operations.
     *
     * @param expected the expected value
     * @param actual the actual value
     */
    static void assertBitEquals(double expected, double actual) {
        long expectedBits = Double.doubleToRawLongBits(expected);
        long actualBits = Double.doubleToRawLongBits(actual);
        if (expectedBits != actualBits) {
            fail(String.format(
                "Expected bitwise equality, but was not. "
                    + "Expected: %s (0x%h) but got: %s (0x%h)",
                expected, expectedBits, actual, actualBits));
        }
    }

    /**
     * Verifies that a `get` call within the same thread that performed a `lazySet`
     * will always observe the value that was set. While `lazySet` provides no guarantee
     * about visibility to *other* threads, it guarantees immediate visibility to the
     * *same* thread.
     */
    public void testLazySet_getInSameThread_returnsUpdatedValue() {
        // Arrange
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(TEST_VALUES.length);
        final double anotherTestValue = -3.0;

        // Act & Assert: Iterate through a wide range of double values to ensure consistent behavior.
        for (int i = 0; i < TEST_VALUES.length; i++) {
            double originalTestValue = TEST_VALUES[i];

            // Step 1: Verify setting a value from the initial state (0.0).
            assertEquals(0.0, atomicArray.get(i));

            atomicArray.lazySet(i, originalTestValue);
            // A `get` in the same thread must see the `lazySet` value immediately.
            assertBitEquals(originalTestValue, atomicArray.get(i));

            // Step 2: Verify a subsequent `lazySet` on the same index.
            atomicArray.lazySet(i, anotherTestValue);
            assertBitEquals(anotherTestValue, atomicArray.get(i));
        }
    }
}