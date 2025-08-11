package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.math.RoundingMode;

import org.junit.Test;

/**
 * Readable tests for FieldUtils safe arithmetic operations.
 * 
 * Test style:
 * - Grouped by operation and operand types.
 * - Clear test method names describe intent/behavior.
 * - Uses assertThrows instead of try/catch for exceptional flows.
 * - Includes boundary and representative cases with explanatory comments.
 */
public class TestFieldUtils {

    // ----------------------------------------------------------------------
    // safeAdd(int, int)
    // ----------------------------------------------------------------------

    @Test
    public void safeAdd_int_returnsSumWithinBounds() {
        // Zero and symmetric operations
        assertEquals("0 + 0", 0, FieldUtils.safeAdd(0, 0));

        // Simple combinations
        assertEquals("2 + 3", 5, FieldUtils.safeAdd(2, 3));
        assertEquals("2 + (-3)", -1, FieldUtils.safeAdd(2, -3));
        assertEquals("(-2) + 3", 1, FieldUtils.safeAdd(-2, 3));
        assertEquals("(-2) + (-3)", -5, FieldUtils.safeAdd(-2, -3));

        // Boundary values within range
        assertEquals("MAX + (-1)", Integer.MAX_VALUE - 1, FieldUtils.safeAdd(Integer.MAX_VALUE, -1));
        assertEquals("MIN + 1", Integer.MIN_VALUE + 1, FieldUtils.safeAdd(Integer.MIN_VALUE, 1));

        // Large opposite signs cancel without overflow
        assertEquals("MIN + MAX", -1, FieldUtils.safeAdd(Integer.MIN_VALUE, Integer.MAX_VALUE));
        assertEquals("MAX + MIN", -1, FieldUtils.safeAdd(Integer.MAX_VALUE, Integer.MIN_VALUE));
    }

    @Test
    public void safeAdd_int_throwsOnOverflow() {
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MAX_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MAX_VALUE, 100));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MAX_VALUE, Integer.MAX_VALUE));

        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MIN_VALUE, -1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MIN_VALUE, -100));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MIN_VALUE, Integer.MIN_VALUE));
    }

    // ----------------------------------------------------------------------
    // safeAdd(long, long)
    // ----------------------------------------------------------------------

    @Test
    public void safeAdd_long_returnsSumWithinBounds() {
        // Zero and symmetric operations
        assertEquals("0L + 0L", 0L, FieldUtils.safeAdd(0L, 0L));

        // Simple combinations
        assertEquals("2L + 3L", 5L, FieldUtils.safeAdd(2L, 3L));
        assertEquals("2L + (-3L)", -1L, FieldUtils.safeAdd(2L, -3L));
        assertEquals("(-2L) + 3L", 1L, FieldUtils.safeAdd(-2L, 3L));
        assertEquals("(-2L) + (-3L)", -5L, FieldUtils.safeAdd(-2L, -3L));

        // Boundary values within range
        assertEquals("MAX + (-1L)", Long.MAX_VALUE - 1L, FieldUtils.safeAdd(Long.MAX_VALUE, -1L));
        assertEquals("MIN + 1L", Long.MIN_VALUE + 1L, FieldUtils.safeAdd(Long.MIN_VALUE, 1L));

        // Large opposite signs cancel without overflow
        assertEquals("MIN + MAX", -1L, FieldUtils.safeAdd(Long.MIN_VALUE, Long.MAX_VALUE));
        assertEquals("MAX + MIN", -1L, FieldUtils.safeAdd(Long.MAX_VALUE, Long.MIN_VALUE));
    }

    @Test
    public void safeAdd_long_throwsOnOverflow() {
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Long.MAX_VALUE, 1L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Long.MAX_VALUE, 100L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Long.MAX_VALUE, Long.MAX_VALUE));

        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Long.MIN_VALUE, -1L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Long.MIN_VALUE, -100L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Long.MIN_VALUE, Long.MIN_VALUE));
    }

    // ----------------------------------------------------------------------
    // safeSubtract(long, long)
    // ----------------------------------------------------------------------

    @Test
    public void safeSubtract_long_returnsDifferenceWithinBounds() {
        assertEquals("0L - 0L", 0L, FieldUtils.safeSubtract(0L, 0L));

        assertEquals("2L - 3L", -1L, FieldUtils.safeSubtract(2L, 3L));
        assertEquals("2L - (-3L)", 5L, FieldUtils.safeSubtract(2L, -3L));
        assertEquals("(-2L) - 3L", -5L, FieldUtils.safeSubtract(-2L, 3L));
        assertEquals("(-2L) - (-3L)", 1L, FieldUtils.safeSubtract(-2L, -3L));

        assertEquals("MAX - 1L", Long.MAX_VALUE - 1L, FieldUtils.safeSubtract(Long.MAX_VALUE, 1L));
        assertEquals("MIN - (-1L)", Long.MIN_VALUE + 1L, FieldUtils.safeSubtract(Long.MIN_VALUE, -1L));

        assertEquals("MIN - MIN", 0L, FieldUtils.safeSubtract(Long.MIN_VALUE, Long.MIN_VALUE));
        assertEquals("MAX - MAX", 0L, FieldUtils.safeSubtract(Long.MAX_VALUE, Long.MAX_VALUE));
    }

    @Test
    public void safeSubtract_long_throwsOnOverflow() {
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Long.MIN_VALUE, 1L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Long.MIN_VALUE, 100L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Long.MIN_VALUE, Long.MAX_VALUE));

        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Long.MAX_VALUE, -1L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Long.MAX_VALUE, -100L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Long.MAX_VALUE, Long.MIN_VALUE));
    }

    // ----------------------------------------------------------------------
    // safeMultiply(long, long)
    // ----------------------------------------------------------------------

    @Test
    public void safeMultiply_long_long_returnsProductWithinBounds() {
        // Zeros and ones
        assertEquals("0L * 0L", 0L, FieldUtils.safeMultiply(0L, 0L));
        assertEquals("1L * 1L", 1L, FieldUtils.safeMultiply(1L, 1L));
        assertEquals("1L * 3L", 3L, FieldUtils.safeMultiply(1L, 3L));
        assertEquals("3L * 1L", 3L, FieldUtils.safeMultiply(3L, 1L));

        // Signs
        assertEquals("2L * 3L", 6L, FieldUtils.safeMultiply(2L, 3L));
        assertEquals("2L * (-3L)", -6L, FieldUtils.safeMultiply(2L, -3L));
        assertEquals("(-2L) * 3L", -6L, FieldUtils.safeMultiply(-2L, 3L));
        assertEquals("(-2L) * (-3L)", 6L, FieldUtils.safeMultiply(-2L, -3L));

        // Identity edge cases
        assertEquals("MAX * 1L", Long.MAX_VALUE, FieldUtils.safeMultiply(Long.MAX_VALUE, 1L));
        assertEquals("MIN * 1L", Long.MIN_VALUE, FieldUtils.safeMultiply(Long.MIN_VALUE, 1L));
        assertEquals("MAX * (-1L)", -Long.MAX_VALUE, FieldUtils.safeMultiply(Long.MAX_VALUE, -1L));
    }

    @Test
    public void safeMultiply_long_long_throwsOnOverflow() {
        // MIN * (-1) cannot be represented in two's-complement 64-bit
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MIN_VALUE, -1L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(-1L, Long.MIN_VALUE));

        // Representative large cases
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MIN_VALUE, 100L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MIN_VALUE, Long.MAX_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MAX_VALUE, Long.MIN_VALUE));
    }

    // ----------------------------------------------------------------------
    // safeMultiply(long, int)
    // ----------------------------------------------------------------------

    @Test
    public void safeMultiply_long_int_returnsProductWithinBounds() {
        // Zeros and ones
        assertEquals("0L * 0", 0L, FieldUtils.safeMultiply(0L, 0));
        assertEquals("1L * 1", 1L, FieldUtils.safeMultiply(1L, 1));
        assertEquals("1L * 3", 3L, FieldUtils.safeMultiply(1L, 3));
        assertEquals("3L * 1", 3L, FieldUtils.safeMultiply(3L, 1));

        // Signs
        assertEquals("2L * 3", 6L, FieldUtils.safeMultiply(2L, 3));
        assertEquals("2L * (-3)", -6L, FieldUtils.safeMultiply(2L, -3));
        assertEquals("(-2L) * 3", -6L, FieldUtils.safeMultiply(-2L, 3));
        assertEquals("(-2L) * (-3)", 6L, FieldUtils.safeMultiply(-2L, -3));

        // Known safe identity cases
        assertEquals("MAX * 1", Long.MAX_VALUE, FieldUtils.safeMultiply(Long.MAX_VALUE, 1));
        assertEquals("MIN * 1", Long.MIN_VALUE, FieldUtils.safeMultiply(Long.MIN_VALUE, 1));
        assertEquals("MAX * (-1)", -Long.MAX_VALUE, FieldUtils.safeMultiply(Long.MAX_VALUE, -1));

        // Cross-width boundary: -1L * Integer.MIN_VALUE = 2147483648L
        assertEquals("-1L * Integer.MIN_VALUE", -1L * Integer.MIN_VALUE, FieldUtils.safeMultiply(-1L, Integer.MIN_VALUE));
    }

    @Test
    public void safeMultiply_long_int_throwsOnOverflow() {
        // MIN * (-1) cannot be represented
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MIN_VALUE, -1));

        // Representative large cases
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MIN_VALUE, 100));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MIN_VALUE, Integer.MAX_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MAX_VALUE, Integer.MIN_VALUE));
    }

    // ----------------------------------------------------------------------
    // safeDivide(long, long)
    // ----------------------------------------------------------------------

    @Test
    public void safeDivide_long_long_returnsQuotientWithTruncationTowardZero() {
        // Basic exact division
        assertEquals("1L / 1L", 1L, FieldUtils.safeDivide(1L, 1L));
        assertEquals("3L / 3L", 1L, FieldUtils.safeDivide(3L, 3L));
        assertEquals("3L / 1L", 3L, FieldUtils.safeDivide(3L, 1L));

        // Truncation toward zero for non-exact division
        assertEquals("1L / 3L", 0L, FieldUtils.safeDivide(1L, 3L));
        assertEquals("5L / 3L", 1L, FieldUtils.safeDivide(5L, 3L));
        assertEquals("5L / (-3L)", -1L, FieldUtils.safeDivide(5L, -3L));
        assertEquals("(-5L) / 3L", -1L, FieldUtils.safeDivide(-5L, 3L));
        assertEquals("(-5L) / (-3L)", 1L, FieldUtils.safeDivide(-5L, -3L));

        // More cases around truncation
        assertEquals("6L / 3L", 2L, FieldUtils.safeDivide(6L, 3L));
        assertEquals("6L / (-3L)", -2L, FieldUtils.safeDivide(6L, -3L));
        assertEquals("(-6L) / 3L", -2L, FieldUtils.safeDivide(-6L, 3L));
        assertEquals("(-6L) / (-3L)", 2L, FieldUtils.safeDivide(-6L, -3L));

        assertEquals("7L / 3L", 2L, FieldUtils.safeDivide(7L, 3L));
        assertEquals("7L / (-3L)", -2L, FieldUtils.safeDivide(7L, -3L));
        assertEquals("(-7L) / 3L", -2L, FieldUtils.safeDivide(-7L, 3L));
        assertEquals("(-7L) / (-3L)", 2L, FieldUtils.safeDivide(-7L, -3L));

        // Identity and sign behavior
        assertEquals("MAX / 1L", Long.MAX_VALUE, FieldUtils.safeDivide(Long.MAX_VALUE, 1L));
        assertEquals("MIN / 1L", Long.MIN_VALUE, FieldUtils.safeDivide(Long.MIN_VALUE, 1L));
        assertEquals("MAX / (-1L)", -Long.MAX_VALUE, FieldUtils.safeDivide(Long.MAX_VALUE, -1L));
    }

    @Test
    public void safeDivide_long_long_throwsOnOverflowOrDivideByZero() {
        // Overflow: MIN / -1 cannot be represented
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeDivide(Long.MIN_VALUE, -1L));

        // Division by zero
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeDivide(1L, 0L));
    }

    // ----------------------------------------------------------------------
    // safeDivide(long, long, RoundingMode)
    // ----------------------------------------------------------------------

    @Test
    public void safeDivide_withRoundingMode_appliesRequestedRounding() {
        // Exact division with UNNECESSARY
        assertEquals("15 / 5 (UNNECESSARY)", 3L, FieldUtils.safeDivide(15L, 5L, RoundingMode.UNNECESSARY));

        // Non-exact positive values
        assertEquals("FLOOR(179/3)", 59L, FieldUtils.safeDivide(179L, 3L, RoundingMode.FLOOR));
        assertEquals("CEILING(179/3)", 60L, FieldUtils.safeDivide(179L, 3L, RoundingMode.CEILING));
        assertEquals("HALF_UP(179/3)", 60L, FieldUtils.safeDivide(179L, 3L, RoundingMode.HALF_UP));
        assertEquals("HALF_DOWN(179/3)", 60L, FieldUtils.safeDivide(179L, 3L, RoundingMode.HALF_DOWN));

        // Non-exact negative values to ensure symmetric rounding behavior
        assertEquals("HALF_UP(-179/3)", -60L, FieldUtils.safeDivide(-179L, 3L, RoundingMode.HALF_UP));
        assertEquals("HALF_DOWN(-179/3)", -60L, FieldUtils.safeDivide(-179L, 3L, RoundingMode.HALF_DOWN));

        // Identity and sign behavior with UNNECESSARY
        assertEquals("MAX / 1 (UNNECESSARY)", Long.MAX_VALUE, FieldUtils.safeDivide(Long.MAX_VALUE, 1L, RoundingMode.UNNECESSARY));
        assertEquals("MIN / 1 (UNNECESSARY)", Long.MIN_VALUE, FieldUtils.safeDivide(Long.MIN_VALUE, 1L, RoundingMode.UNNECESSARY));
        assertEquals("MAX / -1 (UNNECESSARY)", -Long.MAX_VALUE, FieldUtils.safeDivide(Long.MAX_VALUE, -1L, RoundingMode.UNNECESSARY));
    }

    @Test
    public void safeDivide_withRoundingMode_throwsOnOverflowOrDivideByZero() {
        // Overflow with UNNECESSARY rounding (same overflow rule applies)
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeDivide(Long.MIN_VALUE, -1L, RoundingMode.UNNECESSARY));

        // Division by zero regardless of rounding mode
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeDivide(1L, 0L, RoundingMode.UNNECESSARY));
    }
}