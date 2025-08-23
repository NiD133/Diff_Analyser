package org.joda.time.field;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for {@link FieldUtils#safeDivide(long, long)}.
 */
class FieldUtils_SafeDivideLongTest {

    @Test
    void safeDivide_performsStandardDivisionAndTruncation() {
        // Basic cases
        assertEquals(1L, FieldUtils.safeDivide(3L, 3L));
        assertEquals(3L, FieldUtils.safeDivide(3L, 1L));
        assertEquals(2L, FieldUtils.safeDivide(6L, 3L));

        // Division with positive operands truncates toward zero
        assertEquals(0L, FieldUtils.safeDivide(1L, 3L));
        assertEquals(1L, FieldUtils.safeDivide(5L, 3L));
        assertEquals(2L, FieldUtils.safeDivide(7L, 3L));
    }

    @Test
    void safeDivide_withNegativeOperands_truncatesTowardZero() {
        // Dividend and divisor with different signs
        assertEquals(-1L, FieldUtils.safeDivide(5L, -3L));
        assertEquals(-1L, FieldUtils.safeDivide(-5L, 3L));
        assertEquals(-2L, FieldUtils.safeDivide(7L, -3L));
        assertEquals(-2L, FieldUtils.safeDivide(-7L, 3L));

        // Dividend and divisor with same negative sign
        assertEquals(1L, FieldUtils.safeDivide(-5L, -3L));
        assertEquals(2L, FieldUtils.safeDivide(-6L, -3L));
        assertEquals(2L, FieldUtils.safeDivide(-7L, -3L));
    }

    @Test
    void safeDivide_byOne_returnsDividend() {
        assertEquals(Long.MAX_VALUE, FieldUtils.safeDivide(Long.MAX_VALUE, 1L));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeDivide(Long.MIN_VALUE, 1L));
    }

    @Test
    void safeDivide_byNegativeOne_returnsNegatedDividend() {
        assertEquals(-Long.MAX_VALUE, FieldUtils.safeDivide(Long.MAX_VALUE, -1L));
    }

    @Test
    void safeDivide_byZero_throwsArithmeticException() {
        assertThrows(ArithmeticException.class, () -> {
            FieldUtils.safeDivide(1L, 0L);
        });
    }

    @Test
    void safeDivide_whenMinValueDividedByNegativeOne_throwsArithmeticExceptionForOverflow() {
        // This operation overflows because -Long.MIN_VALUE is larger than Long.MAX_VALUE
        assertThrows(ArithmeticException.class, () -> {
            FieldUtils.safeDivide(Long.MIN_VALUE, -1L);
        });
    }
}