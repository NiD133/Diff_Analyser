package org.joda.time.field;

import org.junit.Test;
import java.math.RoundingMode;

/**
 * Contains an improved test case for the {@link FieldUtils} class.
 */
public class FieldUtils_ESTestTest58 extends FieldUtils_ESTest_scaffolding {

    /**
     * Tests that {@link FieldUtils#safeDivide(long, long, RoundingMode)} throws an
     * {@link ArithmeticException} when the division would result in an overflow.
     *
     * The canonical case for long division overflow is dividing {@link Long#MIN_VALUE}
     * by -1, as the mathematical result ({@code Long.MAX_VALUE + 1}) cannot be
     * represented as a long.
     */
    @Test(expected = ArithmeticException.class)
    public void safeDivide_withMinValueAndMinusOne_shouldThrowArithmeticException() {
        // The dividend is the smallest possible long value.
        final long dividend = Long.MIN_VALUE;

        // The divisor is -1.
        final long divisor = -1L;

        // A rounding mode is required, but it does not affect the overflow check in this edge case.
        final RoundingMode roundingMode = RoundingMode.DOWN;

        // This operation is expected to throw an ArithmeticException because the result
        // (9,223,372,036,854,775,808) is greater than Long.MAX_VALUE.
        FieldUtils.safeDivide(dividend, divisor, roundingMode);
    }
}