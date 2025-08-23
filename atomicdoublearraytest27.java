package com.google.common.util.concurrent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link AtomicDoubleArray} focusing on its handling of special double values.
 */
public class AtomicDoubleArrayTestTest27 extends JSR166TestCase {

    /**
     * Asserts that two double values are bitwise equal, which is the comparison method used by
     * {@link AtomicDoubleArray}. This is different from the `==` operator, which treats -0.0 and
     * +0.0 as equal.
     */
    private static void assertBitEquals(String message, double expected, double actual) {
        assertEquals(
                message,
                Double.doubleToRawLongBits(expected),
                Double.doubleToRawLongBits(actual));
    }

    /**
     * Verifies that compareAndSet and weakCompareAndSet distinguish between positive zero (+0.0) and
     * negative zero (-0.0), because they operate on the raw bit representation of doubles.
     */
    public void testCompareAndSet_distinguishesBetweenPositiveAndNegativeZero() {
        // === Arrange ===
        final double POSITIVE_ZERO = +0.0;
        final double NEGATIVE_ZERO = -0.0;
        final double ARBITRARY_NEW_VALUE = 7.0;

        // An AtomicDoubleArray is initialized with +0.0 at all positions by default.
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(SIZE);

        // Test the behavior on the first and last elements to check boundary conditions.
        for (int i : new int[] {0, SIZE - 1}) {
            String description = "Testing at index " + i;

            // === Act & Assert: Step 1 ===
            // Verify that trying to swap from an unexpected value (-0.0) fails.
            assertBitEquals(
                    description + ": initial value should be +0.0", POSITIVE_ZERO, atomicArray.get(i));

            assertFalse(
                    description + ": CAS should fail when expecting -0.0 but current is +0.0",
                    atomicArray.compareAndSet(i, NEGATIVE_ZERO, ARBITRARY_NEW_VALUE));
            assertFalse(
                    description + ": weak CAS should also fail for the same reason",
                    atomicArray.weakCompareAndSet(i, NEGATIVE_ZERO, ARBITRARY_NEW_VALUE));

            assertBitEquals(
                    description + ": value should remain +0.0 after failed CAS",
                    POSITIVE_ZERO,
                    atomicArray.get(i));

            // === Act & Assert: Step 2 ===
            // Successfully swap the value from +0.0 to -0.0.
            assertTrue(
                    description + ": CAS from +0.0 to -0.0 should succeed",
                    atomicArray.compareAndSet(i, POSITIVE_ZERO, NEGATIVE_ZERO));

            assertBitEquals(
                    description + ": value should now be -0.0", NEGATIVE_ZERO, atomicArray.get(i));

            // === Act & Assert: Step 3 ===
            // Verify that a subsequent swap from +0.0 fails, as the value is now -0.0.
            assertFalse(
                    description + ": CAS should fail when expecting +0.0 but current is -0.0",
                    atomicArray.compareAndSet(i, POSITIVE_ZERO, ARBITRARY_NEW_VALUE));
            assertFalse(
                    description + ": weak CAS should also fail for the same reason",
                    atomicArray.weakCompareAndSet(i, POSITIVE_ZERO, ARBITRARY_NEW_VALUE));

            assertBitEquals(
                    description + ": value should remain -0.0 after failed CAS",
                    NEGATIVE_ZERO,
                    atomicArray.get(i));
        }
    }
}