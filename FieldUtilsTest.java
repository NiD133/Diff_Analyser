package org.joda.time.field;

import java.math.RoundingMode;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit tests for FieldUtils class.
 * This class tests various arithmetic operations provided by FieldUtils
 * to ensure they handle edge cases and overflow conditions correctly.
 * 
 * @see FieldUtils
 */
public class TestFieldUtils extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestFieldUtils.class);
    }

    public TestFieldUtils(String name) {
        super(name);
    }

    /**
     * Tests the safe addition of two integers.
     */
    public void testSafeAddInt() {
        // Basic addition tests
        assertEquals(0, FieldUtils.safeAdd(0, 0));
        assertEquals(5, FieldUtils.safeAdd(2, 3));
        assertEquals(-1, FieldUtils.safeAdd(2, -3));
        assertEquals(1, FieldUtils.safeAdd(-2, 3));
        assertEquals(-5, FieldUtils.safeAdd(-2, -3));

        // Edge cases for integer boundaries
        assertEquals(Integer.MAX_VALUE - 1, FieldUtils.safeAdd(Integer.MAX_VALUE, -1));
        assertEquals(Integer.MIN_VALUE + 1, FieldUtils.safeAdd(Integer.MIN_VALUE, 1));

        // Overflow tests
        assertOverflow(() -> FieldUtils.safeAdd(Integer.MAX_VALUE, 1));
        assertOverflow(() -> FieldUtils.safeAdd(Integer.MAX_VALUE, 100));
        assertOverflow(() -> FieldUtils.safeAdd(Integer.MAX_VALUE, Integer.MAX_VALUE));
        assertOverflow(() -> FieldUtils.safeAdd(Integer.MIN_VALUE, -1));
        assertOverflow(() -> FieldUtils.safeAdd(Integer.MIN_VALUE, -100));
        assertOverflow(() -> FieldUtils.safeAdd(Integer.MIN_VALUE, Integer.MIN_VALUE));
    }

    /**
     * Tests the safe addition of two long integers.
     */
    public void testSafeAddLong() {
        // Basic addition tests
        assertEquals(0L, FieldUtils.safeAdd(0L, 0L));
        assertEquals(5L, FieldUtils.safeAdd(2L, 3L));
        assertEquals(-1L, FieldUtils.safeAdd(2L, -3L));
        assertEquals(1L, FieldUtils.safeAdd(-2L, 3L));
        assertEquals(-5L, FieldUtils.safeAdd(-2L, -3L));

        // Edge cases for long boundaries
        assertEquals(Long.MAX_VALUE - 1, FieldUtils.safeAdd(Long.MAX_VALUE, -1L));
        assertEquals(Long.MIN_VALUE + 1, FieldUtils.safeAdd(Long.MIN_VALUE, 1L));

        // Overflow tests
        assertOverflow(() -> FieldUtils.safeAdd(Long.MAX_VALUE, 1L));
        assertOverflow(() -> FieldUtils.safeAdd(Long.MAX_VALUE, 100L));
        assertOverflow(() -> FieldUtils.safeAdd(Long.MAX_VALUE, Long.MAX_VALUE));
        assertOverflow(() -> FieldUtils.safeAdd(Long.MIN_VALUE, -1L));
        assertOverflow(() -> FieldUtils.safeAdd(Long.MIN_VALUE, -100L));
        assertOverflow(() -> FieldUtils.safeAdd(Long.MIN_VALUE, Long.MIN_VALUE));
    }

    /**
     * Tests the safe subtraction of two long integers.
     */
    public void testSafeSubtractLong() {
        // Basic subtraction tests
        assertEquals(0L, FieldUtils.safeSubtract(0L, 0L));
        assertEquals(-1L, FieldUtils.safeSubtract(2L, 3L));
        assertEquals(5L, FieldUtils.safeSubtract(2L, -3L));
        assertEquals(-5L, FieldUtils.safeSubtract(-2L, 3L));
        assertEquals(1L, FieldUtils.safeSubtract(-2L, -3L));

        // Edge cases for long boundaries
        assertEquals(Long.MAX_VALUE - 1, FieldUtils.safeSubtract(Long.MAX_VALUE, 1L));
        assertEquals(Long.MIN_VALUE + 1, FieldUtils.safeSubtract(Long.MIN_VALUE, -1L));

        // Overflow tests
        assertOverflow(() -> FieldUtils.safeSubtract(Long.MIN_VALUE, 1L));
        assertOverflow(() -> FieldUtils.safeSubtract(Long.MIN_VALUE, 100L));
        assertOverflow(() -> FieldUtils.safeSubtract(Long.MIN_VALUE, Long.MAX_VALUE));
        assertOverflow(() -> FieldUtils.safeSubtract(Long.MAX_VALUE, -1L));
        assertOverflow(() -> FieldUtils.safeSubtract(Long.MAX_VALUE, -100L));
        assertOverflow(() -> FieldUtils.safeSubtract(Long.MAX_VALUE, Long.MIN_VALUE));
    }

    /**
     * Tests the safe multiplication of two long integers.
     */
    public void testSafeMultiplyLongLong() {
        // Basic multiplication tests
        assertEquals(0L, FieldUtils.safeMultiply(0L, 0L));
        assertEquals(1L, FieldUtils.safeMultiply(1L, 1L));
        assertEquals(3L, FieldUtils.safeMultiply(1L, 3L));
        assertEquals(3L, FieldUtils.safeMultiply(3L, 1L));
        assertEquals(6L, FieldUtils.safeMultiply(2L, 3L));
        assertEquals(-6L, FieldUtils.safeMultiply(2L, -3L));
        assertEquals(-6L, FieldUtils.safeMultiply(-2L, 3L));
        assertEquals(6L, FieldUtils.safeMultiply(-2L, -3L));

        // Edge cases for long boundaries
        assertEquals(Long.MAX_VALUE, FieldUtils.safeMultiply(Long.MAX_VALUE, 1L));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeMultiply(Long.MIN_VALUE, 1L));
        assertEquals(-Long.MAX_VALUE, FieldUtils.safeMultiply(Long.MAX_VALUE, -1L));

        // Overflow tests
        assertOverflow(() -> FieldUtils.safeMultiply(Long.MIN_VALUE, -1L));
        assertOverflow(() -> FieldUtils.safeMultiply(-1L, Long.MIN_VALUE));
        assertOverflow(() -> FieldUtils.safeMultiply(Long.MIN_VALUE, 100L));
        assertOverflow(() -> FieldUtils.safeMultiply(Long.MIN_VALUE, Long.MAX_VALUE));
        assertOverflow(() -> FieldUtils.safeMultiply(Long.MAX_VALUE, Long.MIN_VALUE));
    }

    /**
     * Tests the safe multiplication of a long and an integer.
     */
    public void testSafeMultiplyLongInt() {
        // Basic multiplication tests
        assertEquals(0L, FieldUtils.safeMultiply(0L, 0));
        assertEquals(1L, FieldUtils.safeMultiply(1L, 1));
        assertEquals(3L, FieldUtils.safeMultiply(1L, 3));
        assertEquals(3L, FieldUtils.safeMultiply(3L, 1));
        assertEquals(6L, FieldUtils.safeMultiply(2L, 3));
        assertEquals(-6L, FieldUtils.safeMultiply(2L, -3));
        assertEquals(-6L, FieldUtils.safeMultiply(-2L, 3));
        assertEquals(6L, FieldUtils.safeMultiply(-2L, -3));

        // Edge cases for long boundaries
        assertEquals(-1L * Integer.MIN_VALUE, FieldUtils.safeMultiply(-1L, Integer.MIN_VALUE));
        assertEquals(Long.MAX_VALUE, FieldUtils.safeMultiply(Long.MAX_VALUE, 1));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeMultiply(Long.MIN_VALUE, 1));
        assertEquals(-Long.MAX_VALUE, FieldUtils.safeMultiply(Long.MAX_VALUE, -1));

        // Overflow tests
        assertOverflow(() -> FieldUtils.safeMultiply(Long.MIN_VALUE, -1));
        assertOverflow(() -> FieldUtils.safeMultiply(Long.MIN_VALUE, 100));
        assertOverflow(() -> FieldUtils.safeMultiply(Long.MIN_VALUE, Integer.MAX_VALUE));
        assertOverflow(() -> FieldUtils.safeMultiply(Long.MAX_VALUE, Integer.MIN_VALUE));
    }

    /**
     * Tests the safe division of two long integers.
     */
    public void testSafeDivideLongLong() {
        // Basic division tests
        assertEquals(1L, FieldUtils.safeDivide(1L, 1L));
        assertEquals(1L, FieldUtils.safeDivide(3L, 3L));
        assertEquals(0L, FieldUtils.safeDivide(1L, 3L));
        assertEquals(3L, FieldUtils.safeDivide(3L, 1L));
        assertEquals(1L, FieldUtils.safeDivide(5L, 3L));
        assertEquals(-1L, FieldUtils.safeDivide(5L, -3L));
        assertEquals(-1L, FieldUtils.safeDivide(-5L, 3L));
        assertEquals(1L, FieldUtils.safeDivide(-5L, -3L));
        assertEquals(2L, FieldUtils.safeDivide(6L, 3L));
        assertEquals(-2L, FieldUtils.safeDivide(6L, -3L));
        assertEquals(-2L, FieldUtils.safeDivide(-6L, 3L));
        assertEquals(2L, FieldUtils.safeDivide(-6L, -3L));
        assertEquals(2L, FieldUtils.safeDivide(7L, 3L));
        assertEquals(-2L, FieldUtils.safeDivide(7L, -3L));
        assertEquals(-2L, FieldUtils.safeDivide(-7L, 3L));
        assertEquals(2L, FieldUtils.safeDivide(-7L, -3L));

        // Edge cases for long boundaries
        assertEquals(Long.MAX_VALUE, FieldUtils.safeDivide(Long.MAX_VALUE, 1L));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeDivide(Long.MIN_VALUE, 1L));
        assertEquals(-Long.MAX_VALUE, FieldUtils.safeDivide(Long.MAX_VALUE, -1L));

        // Overflow and division by zero tests
        assertOverflow(() -> FieldUtils.safeDivide(Long.MIN_VALUE, -1L));
        assertOverflow(() -> FieldUtils.safeDivide(1L, 0L));
    }

    /**
     * Tests the safe division of two long integers with rounding mode.
     */
    public void testSafeDivideRoundingModeLong() {
        // Division with different rounding modes
        assertEquals(3L, FieldUtils.safeDivide(15L, 5L, RoundingMode.UNNECESSARY));
        assertEquals(59L, FieldUtils.safeDivide(179L, 3L, RoundingMode.FLOOR));
        assertEquals(60L, FieldUtils.safeDivide(179L, 3L, RoundingMode.CEILING));
        assertEquals(60L, FieldUtils.safeDivide(179L, 3L, RoundingMode.HALF_UP));
        assertEquals(-60L, FieldUtils.safeDivide(-179L, 3L, RoundingMode.HALF_UP));
        assertEquals(60L, FieldUtils.safeDivide(179L, 3L, RoundingMode.HALF_DOWN));
        assertEquals(-60L, FieldUtils.safeDivide(-179L, 3L, RoundingMode.HALF_DOWN));

        // Edge cases for long boundaries
        assertEquals(Long.MAX_VALUE, FieldUtils.safeDivide(Long.MAX_VALUE, 1L, RoundingMode.UNNECESSARY));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeDivide(Long.MIN_VALUE, 1L, RoundingMode.UNNECESSARY));
        assertEquals(-Long.MAX_VALUE, FieldUtils.safeDivide(Long.MAX_VALUE, -1L, RoundingMode.UNNECESSARY));

        // Overflow and division by zero tests
        assertOverflow(() -> FieldUtils.safeDivide(Long.MIN_VALUE, -1L, RoundingMode.UNNECESSARY));
        assertOverflow(() -> FieldUtils.safeDivide(1L, 0L, RoundingMode.UNNECESSARY));
    }

    /**
     * Helper method to assert that an operation throws an ArithmeticException.
     */
    private void assertOverflow(Runnable operation) {
        try {
            operation.run();
            fail("Expected ArithmeticException not thrown");
        } catch (ArithmeticException e) {
            // Expected exception
        }
    }
}