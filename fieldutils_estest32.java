package org.joda.time.field;

import org.junit.Test;
import java.math.RoundingMode;

/**
 * Unit tests for the safe division methods in {@link FieldUtils}.
 */
public class FieldUtilsSafeDivideTest {

    /**
     * Tests that safeDivide throws an ArithmeticException when the divisor is zero,
     * as division by zero is an invalid mathematical operation.
     */
    @Test(expected = ArithmeticException.class)
    public void safeDivide_shouldThrowArithmeticException_whenDividingByZero() {
        // Arrange: Define the dividend, divisor, and rounding mode.
        // The specific rounding mode is irrelevant here, as the division by zero
        // check happens before any rounding logic is applied.
        final long dividend = 100L;
        final long divisor = 0L;
        final RoundingMode roundingMode = RoundingMode.HALF_UP;

        // Act: Attempt to divide by zero.
        // Assert: The @Test(expected) annotation asserts that an ArithmeticException is thrown.
        FieldUtils.safeDivide(dividend, divisor, roundingMode);
    }
}