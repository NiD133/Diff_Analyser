package org.joda.time.field;

import java.math.RoundingMode;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class FieldUtilsTestTest7 extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestFieldUtils.class);
    }

    //-----------------------------------------------------------------------
    public void testSafeDivideRoundingModeLong() {
        assertEquals(3L, FieldUtils.safeDivide(15L, 5L, RoundingMode.UNNECESSARY));
        assertEquals(59L, FieldUtils.safeDivide(179L, 3L, RoundingMode.FLOOR));
        assertEquals(60L, FieldUtils.safeDivide(179L, 3L, RoundingMode.CEILING));
        assertEquals(60L, FieldUtils.safeDivide(179L, 3L, RoundingMode.HALF_UP));
        assertEquals(-60L, FieldUtils.safeDivide(-179L, 3L, RoundingMode.HALF_UP));
        assertEquals(60L, FieldUtils.safeDivide(179L, 3L, RoundingMode.HALF_DOWN));
        assertEquals(-60L, FieldUtils.safeDivide(-179L, 3L, RoundingMode.HALF_DOWN));
        assertEquals(Long.MAX_VALUE, FieldUtils.safeDivide(Long.MAX_VALUE, 1L, RoundingMode.UNNECESSARY));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeDivide(Long.MIN_VALUE, 1L, RoundingMode.UNNECESSARY));
        assertEquals(-Long.MAX_VALUE, FieldUtils.safeDivide(Long.MAX_VALUE, -1L, RoundingMode.UNNECESSARY));
        try {
            FieldUtils.safeDivide(Long.MIN_VALUE, -1L, RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {
        }
        try {
            FieldUtils.safeDivide(1L, 0L, RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {
        }
    }
}
