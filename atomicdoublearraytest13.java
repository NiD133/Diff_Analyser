package com.google.common.util.concurrent;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link AtomicDoubleArray#getAndSet(int, double)}.
 */
@RunWith(JUnit4.class)
public class AtomicDoubleArrayGetAndSetTest {

    private static final int ARRAY_SIZE = 10;

    // A selection of representative double values to test with, including edge cases.
    private static final double[] TEST_VALUES = {
        Double.NEGATIVE_INFINITY,
        -Double.MAX_VALUE,
        -1.0,
        -0.0, // Negative zero
        +0.0, // Positive zero
        1.0,
        Double.MAX_VALUE,
        Double.POSITIVE_INFINITY,
        Double.NaN,
        Math.PI // An irrational number
    };

    /**
     * Asserts that two doubles are bitwise-equal.
     *
     * <p>This is the notion of equality used by {@link AtomicDoubleArray}, which is based on
     * {@link Double#doubleToRawLongBits(double)}. It differs from both the {@code ==} operator and
     * {@link Double#equals(Object)}, especially for NaN and +/-0.0.
     */
    private static void assertBitEquals(String message, double expected, double actual) {
        assertEquals(
            message,
            Double.doubleToRawLongBits(expected),
            Double.doubleToRawLongBits(actual));
    }

    @Test
    public void getAndSet_returnsPreviousValueAndSetsNewValue() {
        // ARRANGE: Create an array initialized with all 0.0 values.
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(ARRAY_SIZE);
        int firstIndex = 0;
        int lastIndex = ARRAY_SIZE - 1;
        int[] indicesToTest = {firstIndex, lastIndex};

        // Test on the first and last elements as boundary checks.
        for (int index : indicesToTest) {
            // The initial value at any index is 0.0.
            double expectedPreviousValue = 0.0;

            // ACT & ASSERT: For each test value, call getAndSet and verify its behavior.
            for (double newValue : TEST_VALUES) {
                // ACT: Set a new value and get the old one.
                double returnedValue = atomicArray.getAndSet(index, newValue);

                // ASSERT: The method must return the value that was previously at the index.
                assertBitEquals(
                    "getAndSet should return the previous value",
                    expectedPreviousValue,
                    returnedValue);

                // ASSERT: The new value must now be stored at the index.
                assertBitEquals(
                    "The new value should have been set in the array",
                    newValue,
                    atomicArray.get(index));

                // Update the expected previous value for the next iteration's check.
                expectedPreviousValue = newValue;
            }
        }
    }
}