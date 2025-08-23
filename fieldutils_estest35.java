package org.joda.time.field;

import org.junit.Test;

/**
 * Unit tests for {@link FieldUtils}.
 */
public class FieldUtilsTest {

    /**
     * Tests that getWrappedValue throws an ArithmeticException when the full
     * integer range is used for the minimum and maximum values.
     * <p>
     * The method calculates the field's range as {@code maxValue - minValue + 1}.
     * When {@code minValue} is {@code Integer.MIN_VALUE} and {@code maxValue} is
     * {@code Integer.MAX_VALUE}, this calculation overflows, resulting in a range of 0.
     * The subsequent modulo operation on this zero range causes the ArithmeticException.
     */
    @Test(expected = ArithmeticException.class)
    public void getWrappedValue_withFullIntegerRange_throwsArithmeticExceptionForDivisionByZero() {
        // Define arguments that span the entire integer range
        final int value = 1; // The actual value doesn't matter for this test
        final int minValue = Integer.MIN_VALUE;
        final int maxValue = Integer.MAX_VALUE;

        // This call is expected to throw an ArithmeticException due to overflow
        // during the internal range calculation, leading to a division by zero.
        FieldUtils.getWrappedValue(value, minValue, maxValue);
    }
}