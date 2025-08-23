package com.google.common.util.concurrent;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link AtomicDoubleArray#accumulateAndGet}.
 */
@RunWith(JUnit4.class)
public class AtomicDoubleArrayAccumulateAndGetTest {

    // A selection of double values used to test edge cases, including infinities,
    // NaN, zeros, and large/small numbers.
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
            Double.NaN
    };

    /**
     * Asserts that two double values are bit-wise equal.
     *
     * <p>This is necessary because {@link AtomicDoubleArray} uses {@link Double#doubleToRawLongBits}
     * for its equality checks, which differs from standard '==' or '.equals()' comparisons,
     * especially for NaN and -0.0 vs +0.0.
     */
    private static void assertBitwiseEquals(double expected, double actual, String message) {
        assertEquals(message, Double.doubleToRawLongBits(expected), Double.doubleToRawLongBits(actual));
    }

    @Test
    public void accumulateAndGet_withSum_updatesValueAndReturnsNewValue() {
        // Arrange
        final int arraySize = 10;
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(arraySize);
        // Test boundary indices to catch potential off-by-one errors.
        int[] indicesToTest = {0, arraySize - 1};

        // Act & Assert
        // We exhaustively test combinations of interesting double values to ensure correctness
        // across various edge cases.
        for (int index : indicesToTest) {
            for (double initialValue : INTERESTING_DOUBLES) {
                for (double valueToAdd : INTERESTING_DOUBLES) {
                    // Arrange: Set the initial state for this test iteration.
                    atomicArray.set(index, initialValue);
                    double expectedResult = initialValue + valueToAdd;

                    // Act: Call the method under test.
                    double returnedValue = atomicArray.accumulateAndGet(index, valueToAdd, Double::sum);

                    // Assert: Verify both the returned value and the stored value are correct.
                    String failureMessage = String.format(
                            "Failed for index=%d, initialValue=%s, valueToAdd=%s",
                            index, initialValue, valueToAdd);

                    assertBitwiseEquals(
                            expectedResult,
                            returnedValue,
                            "The returned value should be the new sum. " + failureMessage);

                    assertBitwiseEquals(
                            expectedResult,
                            atomicArray.get(index),
                            "The value in the array should be updated to the new sum. " + failureMessage);
                }
            }
        }
    }
}