package com.google.common.util.concurrent;

import static org.junit.Assert.assertFalse;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import org.jspecify.annotations.NullUnmarked;

/**
 * Tests for {@link AtomicDoubleArray#weakCompareAndSet(int, double, double)}.
 */
@NullUnmarked
@GwtIncompatible
@J2ktIncompatible
public class AtomicDoubleArrayWeakCompareAndSetTest extends JSR166TestCase {

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
     * Asserts that two double values are bitwise equal, which is the equality contract for
     * AtomicDoubleArray.
     */
    private static void assertBitEquals(String message, double expected, double actual) {
        assertEquals(message, Double.doubleToRawLongBits(expected), Double.doubleToRawLongBits(actual));
    }

    private static void assertBitEquals(double expected, double actual) {
        assertEquals(Double.doubleToRawLongBits(expected), Double.doubleToRawLongBits(actual));
    }

    /**
     * Verifies that weakCompareAndSet successfully updates the value when the expected value is
     * correct, and fails without modifying the value when the expected value is incorrect. The success
     * case is tested in a loop to account for potential spurious failures.
     */
    public void testWeakCompareAndSet_updatesValueWhenExpectedMatches_andFailsWhenMismatched() {
        // Arrange
        final AtomicDoubleArray atomicArray = new AtomicDoubleArray(SIZE);
        // A value guaranteed not to be in the array, to test mismatched expectations.
        final double wrongExpectedValue = Math.E + Math.PI;

        // Test on first and last elements to check for boundary errors.
        for (int i : new int[] {0, SIZE - 1}) {
            double currentValue = 0.0; // The array is initialized with 0.0.

            // Iterate through a set of special double values.
            for (double newValue : TEST_VALUES) {
                // Pre-condition: Verify the value at index i is what we expect.
                assertBitEquals(
                    "Test setup failed: array value is not the expected initial value.",
                    currentValue,
                    atomicArray.get(i));

                // --- Scenario 1: Failure case (mismatched expected value) ---
                // Act: Attempt to set the new value using an incorrect expected value.
                boolean failedResult = atomicArray.weakCompareAndSet(i, wrongExpectedValue, newValue);

                // Assert: The operation should fail and the value should remain unchanged.
                assertFalse(
                    "weakCompareAndSet should return false for a mismatched expected value.", failedResult);
                assertBitEquals(
                    "The array value should not change on a failed weakCompareAndSet.",
                    currentValue,
                    atomicArray.get(i));

                // --- Scenario 2: Success case (matching expected value) ---
                // Act: Repeatedly attempt to set the new value with the correct expected value.
                // A loop is necessary because weakCompareAndSet can fail spuriously even if the
                // value matches, so we must retry until it succeeds.
                while (!atomicArray.weakCompareAndSet(i, currentValue, newValue)) {
                    // Loop until the operation succeeds.
                }

                // Assert: The value should now be updated.
                assertBitEquals(
                    "The array value should be updated after a successful weakCompareAndSet.",
                    newValue,
                    atomicArray.get(i));

                // Prepare for the next iteration by updating the current value.
                currentValue = newValue;
            }
        }
    }
}