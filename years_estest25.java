package org.joda.time;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the Years class, focusing on edge cases.
 */
public class YearsTest {

    /**
     * Verifies that subtracting MIN_VALUE from a Years object throws an ArithmeticException.
     * This occurs because the subtraction operation involves negating the subtrahend (the
     * argument to minus()), and Integer.MIN_VALUE cannot be negated without causing an
     * integer overflow.
     */
    @Test
    public void minus_whenSubtractingMinValue_throwsArithmeticException() {
        Years minValueYears = Years.MIN_VALUE;

        try {
            // This operation is equivalent to: MIN_VALUE - MIN_VALUE
            // Internally, this is calculated as safeAdd(MIN_VALUE, -MIN_VALUE).
            // The negation of Integer.MIN_VALUE overflows.
            minValueYears.minus(minValueYears);
            fail("Expected an ArithmeticException to be thrown due to integer overflow.");
        } catch (ArithmeticException e) {
            // Assert that the correct exception was thrown for the correct reason.
            assertEquals("Integer.MIN_VALUE cannot be negated", e.getMessage());
        }
    }
}