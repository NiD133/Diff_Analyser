package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for {@link FieldUtils#safeAdd(long, long)}.
 *
 * <p>This test class verifies the behavior of safe addition for long integers,
 * including normal operations, boundary conditions, and overflow handling.
 */
public class FieldUtilsTest {

    @Test
    public void safeAdd_shouldReturnCorrectSumForStandardCases() {
        assertEquals("Adding two positive numbers", 5L, FieldUtils.safeAdd(2L, 3L));
        assertEquals("Adding two negative numbers", -5L, FieldUtils.safeAdd(-2L, -3L));
        assertEquals("Adding a positive and a smaller negative", 1L, FieldUtils.safeAdd(3L, -2L));
        assertEquals("Adding a positive and a larger negative", -1L, FieldUtils.safeAdd(2L, -3L));
        assertEquals("Adding zero to a number", 2L, FieldUtils.safeAdd(2L, 0L));
        assertEquals("Adding two zeros", 0L, FieldUtils.safeAdd(0L, 0L));
    }

    @Test
    public void safeAdd_shouldHandleBoundaryValuesWithoutOverflow() {
        assertEquals("MAX_VALUE + (-1) should not overflow",
                Long.MAX_VALUE - 1, FieldUtils.safeAdd(Long.MAX_VALUE, -1L));
        
        assertEquals("MIN_VALUE + 1 should not overflow",
                Long.MIN_VALUE + 1, FieldUtils.safeAdd(Long.MIN_VALUE, 1L));
        
        assertEquals("MAX_VALUE + MIN_VALUE should be -1",
                -1L, FieldUtils.safeAdd(Long.MAX_VALUE, Long.MIN_VALUE));
    }

    @Test(expected = ArithmeticException.class)
    public void safeAdd_shouldThrowExceptionOnPositiveOverflow() {
        // The most direct overflow case: MAX_VALUE + 1
        FieldUtils.safeAdd(Long.MAX_VALUE, 1L);
    }

    @Test(expected = ArithmeticException.class)
    public void safeAdd_shouldThrowExceptionOnPositiveOverflowWithLargeValue() {
        // A case with two large positive values
        FieldUtils.safeAdd(Long.MAX_VALUE, Long.MAX_VALUE);
    }

    @Test(expected = ArithmeticException.class)
    public void safeAdd_shouldThrowExceptionOnNegativeOverflow() {
        // The most direct underflow case: MIN_VALUE - 1
        FieldUtils.safeAdd(Long.MIN_VALUE, -1L);
    }

    @Test(expected = ArithmeticException.class)
    public void safeAdd_shouldThrowExceptionOnNegativeOverflowWithLargeValue() {
        // A case with two large negative values
        FieldUtils.safeAdd(Long.MIN_VALUE, Long.MIN_VALUE);
    }
}