package org.joda.time.field;

import java.math.RoundingMode;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class FieldUtilsTestTest6 extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestFieldUtils.class);
    }

    //-----------------------------------------------------------------------
    public void testSafeDivideLongLong() {
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
        assertEquals(Long.MAX_VALUE, FieldUtils.safeDivide(Long.MAX_VALUE, 1L));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeDivide(Long.MIN_VALUE, 1L));
        assertEquals(-Long.MAX_VALUE, FieldUtils.safeDivide(Long.MAX_VALUE, -1L));
        try {
            FieldUtils.safeDivide(Long.MIN_VALUE, -1L);
            fail();
        } catch (ArithmeticException e) {
        }
        try {
            FieldUtils.safeDivide(1L, 0L);
            fail();
        } catch (ArithmeticException e) {
        }
    }
}
