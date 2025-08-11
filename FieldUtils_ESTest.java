package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.math.RoundingMode;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.IslamicChronology;
import org.joda.time.chrono.ZonedChronology;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class FieldUtils_ESTest extends FieldUtils_ESTest_scaffolding {

    // Test for getWrappedValue method with valid range
    @Test(timeout = 4000)
    public void testGetWrappedValueWithinRange() throws Throwable {
        int result = FieldUtils.getWrappedValue(-2147483647, 0, -1, 0);
        assertEquals(-1, result);
    }

    // Test for getWrappedValue method with edge case
    @Test(timeout = 4000)
    public void testGetWrappedValueEdgeCase() throws Throwable {
        int result = FieldUtils.getWrappedValue(-436, 0, -436, 0);
        assertEquals(-436, result);
    }

    // Test for getWrappedValue method with invalid range
    @Test(timeout = 4000)
    public void testGetWrappedValueInvalidRange() throws Throwable {
        try {
            FieldUtils.getWrappedValue(823, 317351877, 1363, -3977);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.joda.time.field.FieldUtils", e);
        }
    }

    // Test for verifyValueBounds method with valid bounds
    @Test(timeout = 4000)
    public void testVerifyValueBoundsValid() throws Throwable {
        FieldUtils.verifyValueBounds("", 1, 1, 2000);
    }

    // Test for verifyValueBounds method with invalid bounds
    @Test(timeout = 4000)
    public void testVerifyValueBoundsInvalid() throws Throwable {
        try {
            FieldUtils.verifyValueBounds("", -81, -820, -820);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.joda.time.field.FieldUtils", e);
        }
    }

    // Test for safeToInt method with maximum integer value
    @Test(timeout = 4000)
    public void testSafeToIntMaxValue() throws Throwable {
        int result = FieldUtils.safeToInt(2147483647L);
        assertEquals(Integer.MAX_VALUE, result);
    }

    // Test for safeMultiply method with overflow
    @Test(timeout = 4000)
    public void testSafeMultiplyOverflow() throws Throwable {
        try {
            FieldUtils.safeMultiply(-9223372036854775808L, -7);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            verifyException("org.joda.time.field.FieldUtils", e);
        }
    }

    // Test for safeSubtract method with no overflow
    @Test(timeout = 4000)
    public void testSafeSubtractNoOverflow() throws Throwable {
        long result = FieldUtils.safeSubtract(3600L, 0L);
        assertEquals(3600L, result);
    }

    // Test for safeAdd method with overflow
    @Test(timeout = 4000)
    public void testSafeAddOverflow() throws Throwable {
        try {
            FieldUtils.safeAdd(-610, -9223372036854775808L);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            verifyException("org.joda.time.field.FieldUtils", e);
        }
    }

    // Test for safeNegate method with zero
    @Test(timeout = 4000)
    public void testSafeNegateZero() throws Throwable {
        int result = FieldUtils.safeNegate(0);
        assertEquals(0, result);
    }

    // Test for safeNegate method with negative value
    @Test(timeout = 4000)
    public void testSafeNegateNegativeValue() throws Throwable {
        int result = FieldUtils.safeNegate(-70);
        assertEquals(70, result);
    }

    // Test for safeMultiplyToInt method with zero
    @Test(timeout = 4000)
    public void testSafeMultiplyToIntZero() throws Throwable {
        int result = FieldUtils.safeMultiplyToInt(0L, 0L);
        assertEquals(0, result);
    }

    // Test for safeMultiplyToInt method with positive values
    @Test(timeout = 4000)
    public void testSafeMultiplyToIntPositiveValues() throws Throwable {
        int result = FieldUtils.safeMultiplyToInt(1L, 2765L);
        assertEquals(2765, result);
    }

    // Test for safeDivide method with zero dividend
    @Test(timeout = 4000)
    public void testSafeDivideZeroDividend() throws Throwable {
        RoundingMode roundingMode = RoundingMode.CEILING;
        long result = FieldUtils.safeDivide(0L, 218L, roundingMode);
        assertEquals(0L, result);
    }

    // Test for safeDivide method with valid division
    @Test(timeout = 4000)
    public void testSafeDivideValidDivision() throws Throwable {
        long result = FieldUtils.safeDivide(-9223372036854775808L, 33L);
        assertEquals(-279496122328932600L, result);
    }

    // Test for equals method with null comparison
    @Test(timeout = 4000)
    public void testEqualsWithNull() throws Throwable {
        Integer integer = new Integer(-1);
        boolean result = FieldUtils.equals(integer, null);
        assertFalse(result);
    }

    // Test for equals method with different objects
    @Test(timeout = 4000)
    public void testEqualsDifferentObjects() throws Throwable {
        Object object = new Object();
        Integer integer = new Integer(0);
        boolean result = FieldUtils.equals(object, integer);
        assertFalse(result);
    }

    // Test for equals method with same objects
    @Test(timeout = 4000)
    public void testEqualsSameObjects() throws Throwable {
        Object object = new Object();
        boolean result = FieldUtils.equals(object, object);
        assertTrue(result);
    }

    // Test for equals method with null as first argument
    @Test(timeout = 4000)
    public void testEqualsNullFirstArgument() throws Throwable {
        Object object = new Object();
        boolean result = FieldUtils.equals(null, object);
        assertFalse(result);
    }

    // Additional tests for various methods can be added here following the same pattern

}