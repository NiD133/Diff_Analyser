package org.joda.time.field;

import java.math.RoundingMode;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class FieldUtilsTestTest1 extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestFieldUtils.class);
    }

    public void testSafeAddInt() {
        assertEquals(0, FieldUtils.safeAdd(0, 0));
        assertEquals(5, FieldUtils.safeAdd(2, 3));
        assertEquals(-1, FieldUtils.safeAdd(2, -3));
        assertEquals(1, FieldUtils.safeAdd(-2, 3));
        assertEquals(-5, FieldUtils.safeAdd(-2, -3));
        assertEquals(Integer.MAX_VALUE - 1, FieldUtils.safeAdd(Integer.MAX_VALUE, -1));
        assertEquals(Integer.MIN_VALUE + 1, FieldUtils.safeAdd(Integer.MIN_VALUE, 1));
        assertEquals(-1, FieldUtils.safeAdd(Integer.MIN_VALUE, Integer.MAX_VALUE));
        assertEquals(-1, FieldUtils.safeAdd(Integer.MAX_VALUE, Integer.MIN_VALUE));
        try {
            FieldUtils.safeAdd(Integer.MAX_VALUE, 1);
            fail();
        } catch (ArithmeticException e) {
        }
        try {
            FieldUtils.safeAdd(Integer.MAX_VALUE, 100);
            fail();
        } catch (ArithmeticException e) {
        }
        try {
            FieldUtils.safeAdd(Integer.MAX_VALUE, Integer.MAX_VALUE);
            fail();
        } catch (ArithmeticException e) {
        }
        try {
            FieldUtils.safeAdd(Integer.MIN_VALUE, -1);
            fail();
        } catch (ArithmeticException e) {
        }
        try {
            FieldUtils.safeAdd(Integer.MIN_VALUE, -100);
            fail();
        } catch (ArithmeticException e) {
        }
        try {
            FieldUtils.safeAdd(Integer.MIN_VALUE, Integer.MIN_VALUE);
            fail();
        } catch (ArithmeticException e) {
        }
    }
}
