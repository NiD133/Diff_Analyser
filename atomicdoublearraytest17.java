package com.google.common.util.concurrent;

import static java.lang.Math.max;

/**
 * Tests for {@link AtomicDoubleArray}. This class focuses on the {@code getAndAccumulate} method.
 */
public class AtomicDoubleArrayTestTest17 extends JSR166TestCase {

    /** A wide range of special double values to ensure comprehensive testing of edge cases. */
    private static final double[] SPECIAL_DOUBLE_VALUES = {
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
     * Asserts that two double values are bitwise-equal.
     *
     * <p>This is necessary because {@code AtomicDoubleArray} uses {@link Double#doubleToRawLongBits}
     * for comparisons, which has different semantics than {@code ==} or {@link Double#equals(Object)}
     * (e.g., for NaN and -0.0).
     */
    private static void assertBitEquals(String message, double expected, double actual) {
        assertEquals(
            message, Double.doubleToRawLongBits(expected), Double.doubleToRawLongBits(actual));
    }

    /**
     * Verifies that getAndAccumulate with {@code Double::max} correctly returns the previous value
     * and atomically updates the element to the maximum of the old and new values.
     *
     * <p>This test is exhaustive, checking all combinations of special double values (infinity, NaN,
     * zero, etc.) at the boundaries of the array.
     */
    public void testGetAndAccumulate_withMax_returnsPreviousValueAndStoresNewMax() {
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(SIZE);
        // Test at the start and end of the array to check for boundary errors.
        int[] indicesToTest = {0, SIZE - 1};

        for (int i : indicesToTest) {
            // Test all combinations of special double values to cover edge cases.
            for (double initialValue : SPECIAL_DOUBLE_VALUES) {
                for (double valueToAccumulate : SPECIAL_DOUBLE_VALUES) {
                    // Arrange: Set the initial state of the array element.
                    atomicArray.set(i, initialValue);
                    double expectedFinalValue = max(initialValue, valueToAccumulate);

                    // Act: Call the method under test.
                    double returnedValue =
                        atomicArray.getAndAccumulate(i, valueToAccumulate, Double::max);

                    // Assert: Verify the returned value and the new state of the array.
                    double finalValueInArray = atomicArray.get(i);
                    String failureDetails =
                        String.format(
                            "\n  at index %d with initialValue=%s, valueToAccumulate=%s",
                            i, initialValue, valueToAccumulate);

                    assertBitEquals(
                        "getAndAccumulate should return the value before the update." + failureDetails,
                        initialValue,
                        returnedValue);

                    assertBitEquals(
                        "The array element should be updated to the maximum of the two values."
                            + failureDetails,
                        expectedFinalValue,
                        finalValueInArray);
                }
            }
        }
    }
}