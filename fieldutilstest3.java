package org.joda.time.field;

import java.math.RoundingMode;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class FieldUtilsTestTest3 extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestFieldUtils.class);
    }

    public void testSafeSubtractLong() {
        assertEquals(0L, FieldUtils.safeSubtract(0L, 0L));
        assertEquals(-1L, FieldUtils.safeSubtract(2L, 3L));
        assertEquals(5L, FieldUtils.safeSubtract(2L, -3L));
        assertEquals(-5L, FieldUtils.safeSubtract(-2L, 3L));
        assertEquals(1L, FieldUtils.safeSubtract(-2L, -3L));
        assertEquals(Long.MAX_VALUE - 1, FieldUtils.safeSubtract(Long.MAX_VALUE, 1L));
        assertEquals(Long.MIN_VALUE + 1, FieldUtils.safeSubtract(Long.MIN_VALUE, -1L));
        assertEquals(0, FieldUtils.safeSubtract(Long.MIN_VALUE, Long.MIN_VALUE));
        assertEquals(0, FieldUtils.safeSubtract(Long.MAX_VALUE, Long.MAX_VALUE));
        try {
            FieldUtils.safeSubtract(Long.MIN_VALUE, 1L);
            fail();
        } catch (ArithmeticException e) {
        }
        try {
            FieldUtils.safeSubtract(Long.MIN_VALUE, 100L);
            fail();
        } catch (ArithmeticException e) {
        }
        try {
            FieldUtils.safeSubtract(Long.MIN_VALUE, Long.MAX_VALUE);
            fail();
        } catch (ArithmeticException e) {
        }
        try {
            FieldUtils.safeSubtract(Long.MAX_VALUE, -1L);
            fail();
        } catch (ArithmeticException e) {
        }
        try {
            FieldUtils.safeSubtract(Long.MAX_VALUE, -100L);
            fail();
        } catch (ArithmeticException e) {
        }
        try {
            FieldUtils.safeSubtract(Long.MAX_VALUE, Long.MIN_VALUE);
            fail();
        } catch (ArithmeticException e) {
        }
    }
}
