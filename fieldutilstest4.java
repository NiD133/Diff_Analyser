package org.joda.time.field;

import java.math.RoundingMode;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class FieldUtilsTestTest4 extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestFieldUtils.class);
    }

    //-----------------------------------------------------------------------
    public void testSafeMultiplyLongLong() {
        assertEquals(0L, FieldUtils.safeMultiply(0L, 0L));
        assertEquals(1L, FieldUtils.safeMultiply(1L, 1L));
        assertEquals(3L, FieldUtils.safeMultiply(1L, 3L));
        assertEquals(3L, FieldUtils.safeMultiply(3L, 1L));
        assertEquals(6L, FieldUtils.safeMultiply(2L, 3L));
        assertEquals(-6L, FieldUtils.safeMultiply(2L, -3L));
        assertEquals(-6L, FieldUtils.safeMultiply(-2L, 3L));
        assertEquals(6L, FieldUtils.safeMultiply(-2L, -3L));
        assertEquals(Long.MAX_VALUE, FieldUtils.safeMultiply(Long.MAX_VALUE, 1L));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeMultiply(Long.MIN_VALUE, 1L));
        assertEquals(-Long.MAX_VALUE, FieldUtils.safeMultiply(Long.MAX_VALUE, -1L));
        try {
            FieldUtils.safeMultiply(Long.MIN_VALUE, -1L);
            fail();
        } catch (ArithmeticException e) {
        }
        try {
            FieldUtils.safeMultiply(-1L, Long.MIN_VALUE);
            fail();
        } catch (ArithmeticException e) {
        }
        try {
            FieldUtils.safeMultiply(Long.MIN_VALUE, 100L);
            fail();
        } catch (ArithmeticException e) {
        }
        try {
            FieldUtils.safeMultiply(Long.MIN_VALUE, Long.MAX_VALUE);
            fail();
        } catch (ArithmeticException e) {
        }
        try {
            FieldUtils.safeMultiply(Long.MAX_VALUE, Long.MIN_VALUE);
            fail();
        } catch (ArithmeticException e) {
        }
    }
}
