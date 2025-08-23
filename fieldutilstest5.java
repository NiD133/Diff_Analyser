package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the safeMultiply(long, int) method in FieldUtils.
 */
public class FieldUtilsTest {

    //-----------------------------------------------------------------------
    // Test cases for safeMultiply(long, int)
    //-----------------------------------------------------------------------

    @Test
    public void safeMultiplyLongInt_shouldReturnCorrectProduct_forNormalValues() {
        assertEquals(0L, FieldUtils.safeMultiply(0L, 0));
        assertEquals(1L, FieldUtils.safeMultiply(1L, 1));
        assertEquals(6L, FieldUtils.safeMultiply(2L, 3));
        assertEquals(6L, FieldUtils.safeMultiply(3L, 2));
    }

    @Test
    public void safeMultiplyLongInt_shouldHandleNegativeNumbersCorrectly() {
        assertEquals(-6L, FieldUtils.safeMultiply(2L, -3));
        assertEquals(-6L, FieldUtils.safeMultiply(-2L, 3));
        assertEquals(6L, FieldUtils.safeMultiply(-2L, -3));
    }

    @Test
    public void safeMultiplyLongInt_shouldHandleEdgeCasesWithoutOverflow() {
        // Test multiplication by 1 and -1 with long limits
        assertEquals(Long.MAX_VALUE, FieldUtils.safeMultiply(Long.MAX_VALUE, 1));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeMultiply(Long.MIN_VALUE, 1));
        assertEquals(-Long.MAX_VALUE, FieldUtils.safeMultiply(Long.MAX_VALUE, -1));

        // Test a case where the result of multiplying by a negative number is a positive
        // number that fits within a long. (-1L * -2147483648) = 2147483648L
        assertEquals(-(long)Integer.MIN_VALUE, FieldUtils.safeMultiply(-1L, Integer.MIN_VALUE));
    }

    @Test(expected = ArithmeticException.class)
    public void safeMultiplyLongInt_shouldThrowException_whenMultiplyingMinValueByNegativeOne() {
        // This operation overflows because the result (2^63) is greater than Long.MAX_VALUE (2^63 - 1).
        FieldUtils.safeMultiply(Long.MIN_VALUE, -1);
    }

    @Test(expected = ArithmeticException.class)
    public void safeMultiplyLongInt_shouldThrowException_whenResultIsTooSmall() {
        // Multiplying Long.MIN_VALUE by any integer > 1 will result in an overflow (a number smaller than MIN_VALUE).
        FieldUtils.safeMultiply(Long.MIN_VALUE, 100);
    }
    
    @Test(expected = ArithmeticException.class)
    public void safeMultiplyLongInt_shouldThrowException_onMinValueAndMaxIntValue() {
        FieldUtils.safeMultiply(Long.MIN_VALUE, Integer.MAX_VALUE);
    }

    @Test(expected = ArithmeticException.class)
    public void safeMultiplyLongInt_shouldThrowException_onMaxValueAndMinValue() {
        FieldUtils.safeMultiply(Long.MAX_VALUE, Integer.MIN_VALUE);
    }
}