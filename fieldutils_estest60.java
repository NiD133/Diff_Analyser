package org.joda.time.field;

import org.junit.Test;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    /**
     * Verifies that {@code safeDivide} throws an {@link ArithmeticException} when
     * attempting to divide {@code Long.MIN_VALUE} by -1. This is a classic
     * integer overflow edge case, as the result (2^63) is one greater than
     * {@code Long.MAX_VALUE} (2^63 - 1) and cannot be represented in a long.
     */
    @Test(expected = ArithmeticException.class)
    public void safeDivide_whenDividingLongMinValueByMinusOne_throwsArithmeticException() {
        // This operation is expected to throw an exception because the result overflows the long type.
        FieldUtils.safeDivide(Long.MIN_VALUE, -1L);
    }
}