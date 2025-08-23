package com.google.common.util.concurrent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link AtomicDoubleArray}, focusing on the behavior of the {@code compareAndSet}
 * method.
 */
@RunWith(JUnit4.class)
public class AtomicDoubleArrayTest {

    // A comprehensive set of double values to test, including edge cases like infinities,
    // signed zeros, and NaN.
    private static final double[] INTERESTING_DOUBLES = {
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

    private static final int TEST_ARRAY_SIZE = 10;

    /**
     * Asserts that two double values are bitwise-equal.
     *
     * <p>This is necessary because {@link AtomicDoubleArray} uses {@link
     * Double#doubleToRawLongBits(double)} for its atomic operations. This comparison differs from
     * both the {@code ==} operator and {@link Double#equals(Object)}, especially for values like
     * NaN, -0.0, and +0.0.
     */
    private static void assertBitwiseEquals(double expected, double actual) {
        String message =
            String.format(
                "Expected bit representation <%s> but was <%s>",
                Long.toHexString(Double.doubleToRawLongBits(expected)),
                Long.toHexString(Double.doubleToRawLongBits(actual)));
        assertEquals(message, Double.doubleToRawLongBits(expected), Double.doubleToRawLongBits(actual));
    }

    @Test
    public void compareAndSet_updatesValue_onlyWhenExpectedValueIsCorrect() {
        // Arrange: Create an array initialized with 0.0 and define a value that will never match.
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(TEST_ARRAY_SIZE);
        final double wrongExpectedValue = 123.456; // An arbitrary value not in the array.

        // Test at the boundaries of the array for thoroughness.
        int[] indicesToTest = {0, TEST_ARRAY_SIZE - 1};

        for (int index : indicesToTest) {
            // The array is initialized to 0.0, so this is our first expected value.
            double currentExpectedValue = 0.0;

            // Sequentially update the element with various interesting double values.
            for (double newValue : INTERESTING_DOUBLES) {
                // Pre-condition: Ensure the array element has the value we expect before the test.
                assertBitwiseEquals(currentExpectedValue, atomicArray.get(index));

                // --- Test Failure Case ---
                // Act: Try to update with an INCORRECT expected value.
                boolean casFailed = atomicArray.compareAndSet(index, wrongExpectedValue, newValue);

                // Assert: The operation should fail and the value should not change.
                assertFalse(
                    "CAS should fail when the expected value is incorrect.", casFailed);
                assertBitwiseEquals(
                    "Value should not change after a failed CAS.",
                    currentExpectedValue,
                    atomicArray.get(index));

                // --- Test Success Case ---
                // Act: Try to update with the CORRECT expected value.
                boolean casSucceeded =
                    atomicArray.compareAndSet(index, currentExpectedValue, newValue);

                // Assert: The operation should succeed and the value should be updated.
                assertTrue("CAS should succeed when the expected value is correct.", casSucceeded);
                assertBitwiseEquals(
                    "Value should be updated after a successful CAS.", newValue, atomicArray.get(index));

                // Prepare for the next iteration by updating our expected value.
                currentExpectedValue = newValue;
            }
        }
    }
}