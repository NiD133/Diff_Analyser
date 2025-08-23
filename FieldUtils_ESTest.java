package org.joda.time.field;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.junit.Test;

import java.math.RoundingMode;

import static org.junit.Assert.*;

/**
 * Tests for FieldUtils focused on clarity and maintenance.
 * 
 * The tests are grouped by the method under test and aim to cover
 * typical success paths, edge cases, and representative failure cases.
 */
public class FieldUtilsTest {

    // ---------------------------------------------------------------------
    // safeNegate
    // ---------------------------------------------------------------------

    @Test
    public void safeNegate_returnsNegatedValue() {
        assertEquals(0, FieldUtils.safeNegate(0));
        assertEquals(-10, FieldUtils.safeNegate(10));
        assertEquals(70, FieldUtils.safeNegate(-70));
    }

    @Test(expected = ArithmeticException.class)
    public void safeNegate_minInt_throws() {
        FieldUtils.safeNegate(Integer.MIN_VALUE);
    }

    // ---------------------------------------------------------------------
    // safeAdd (int)
    // ---------------------------------------------------------------------

    @Test
    public void safeAdd_int_addsWithinRange() {
        assertEquals(0, FieldUtils.safeAdd(0, 0));
        assertEquals(9008, FieldUtils.safeAdd(4504, 4504));
        assertEquals(-2147483634, FieldUtils.safeAdd(14, Integer.MIN_VALUE));
    }

    @Test(expected = ArithmeticException.class)
    public void safeAdd_int_overflow_throws() {
        FieldUtils.safeAdd(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    @Test(expected = ArithmeticException.class)
    public void safeAdd_int_underflow_throws() {
        FieldUtils.safeAdd(-1640, Integer.MIN_VALUE);
    }

    // ---------------------------------------------------------------------
    // safeAdd (long)
    // ---------------------------------------------------------------------

    @Test
    public void safeAdd_long_addsWithinRange() {
        assertEquals(0L, FieldUtils.safeAdd(0L, 0L));
        assertEquals(9223372036854775525L, FieldUtils.safeAdd(-275L, 9223372036854775800L));
    }

    @Test(expected = ArithmeticException.class)
    public void safeAdd_long_overflow_throws() {
        FieldUtils.safeAdd(Long.MIN_VALUE, Long.MIN_VALUE);
    }

    // ---------------------------------------------------------------------
    // safeSubtract (long)
    // ---------------------------------------------------------------------

    @Test
    public void safeSubtract_long_subtractsWithinRange() {
        assertEquals(3600L, FieldUtils.safeSubtract(3600L, 0L));
        assertEquals(-2147483647L, FieldUtils.safeSubtract(0L, 2147483647L));
        assertEquals(0L, FieldUtils.safeSubtract(Long.MIN_VALUE, Long.MIN_VALUE));
        assertEquals(2147483648L, FieldUtils.safeSubtract(2147483647L, -1L));
    }

    @Test(expected = ArithmeticException.class)
    public void safeSubtract_long_underflow_throws() {
        FieldUtils.safeSubtract(Long.MIN_VALUE, 11L);
    }

    // ---------------------------------------------------------------------
    // safeMultiply (int)
    // ---------------------------------------------------------------------

    @Test
    public void safeMultiply_int_multipliesWithinRange() {
        assertEquals(0, FieldUtils.safeMultiply(0, 3306));
        assertEquals(Integer.MAX_VALUE, FieldUtils.safeMultiply(-1, -2147483647));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeMultiply(-2, 1073741824)); // exact min
    }

    @Test(expected = ArithmeticException.class)
    public void safeMultiply_int_overflow_throws() {
        FieldUtils.safeMultiply(Integer.MIN_VALUE, Integer.MIN_VALUE);
    }

    @Test(expected = ArithmeticException.class)
    public void safeMultiply_int_overflowWithLargeFactors_throws() {
        FieldUtils.safeMultiply(2146641827, -2531);
    }

    // ---------------------------------------------------------------------
    // safeMultiply (long, int) and (long, long)
    // ---------------------------------------------------------------------

    @Test
    public void safeMultiply_long_variants_multiplyWithinRange() {
        assertEquals(576L, FieldUtils.safeMultiply(24L, 24L));
        assertEquals(0L, FieldUtils.safeMultiply(-9223372036854775804L, 0L));
        assertEquals(0L, FieldUtils.safeMultiply(0L, 0L));
        assertEquals(-889L, FieldUtils.safeMultiply(-889L, 1));
        assertEquals(12544L, FieldUtils.safeMultiply(112L, 112));
        assertEquals(1L, FieldUtils.safeMultiply(-1L, -1));
        assertEquals(-9223372036854775808L, FieldUtils.safeMultiply(1L, Long.MIN_VALUE));
        assertEquals(1L, FieldUtils.safeMultiply(1L, 1L));
    }

    @Test(expected = ArithmeticException.class)
    public void safeMultiply_long_long_overflow_throws() {
        FieldUtils.safeMultiply(Long.MIN_VALUE, Long.MIN_VALUE);
    }

    @Test(expected = ArithmeticException.class)
    public void safeMultiply_long_long_overflowNearMin_throws() {
        FieldUtils.safeMultiply(Long.MIN_VALUE, -1L);
    }

    @Test(expected = ArithmeticException.class)
    public void safeMultiply_long_int_overflow_throws() {
        FieldUtils.safeMultiply(9223372036854775775L, 4_978_168);
    }

    // ---------------------------------------------------------------------
    // safeDivide (long, long)
    // ---------------------------------------------------------------------

    @Test
    public void safeDivide_long_dividesWithinRange() {
        assertEquals(0L, FieldUtils.safeDivide(0L, 3961L));
        assertEquals(-279496122328932600L, FieldUtils.safeDivide(Long.MIN_VALUE, 33L));
        assertEquals(4344499310812423L, FieldUtils.safeDivide(-9223372036854775765L, -2123L));
    }

    @Test(expected = ArithmeticException.class)
    public void safeDivide_long_byZero_throws() {
        FieldUtils.safeDivide(0L, 0L);
    }

    @Test(expected = ArithmeticException.class)
    public void safeDivide_long_overflowAtMinDividedByMinusOne_throws() {
        FieldUtils.safeDivide(Long.MIN_VALUE, -1L);
    }

    // ---------------------------------------------------------------------
    // safeDivide (long, long, RoundingMode)
    // ---------------------------------------------------------------------

    @Test
    public void safeDivide_withRoundingMode_dividesAsExpected() {
        assertEquals(0L, FieldUtils.safeDivide(0L, 218L, RoundingMode.CEILING));
        assertEquals(1L, FieldUtils.safeDivide((long) Integer.MIN_VALUE, (long) Integer.MIN_VALUE, RoundingMode.FLOOR));
        assertEquals(1L, FieldUtils.safeDivide(1L, 2L, RoundingMode.CEILING));
        assertEquals(-1L, FieldUtils.safeDivide(-1L, 2L, RoundingMode.FLOOR));
        assertEquals(-28913391965061994L, FieldUtils.safeDivide(Long.MIN_VALUE, 319L, RoundingMode.UP));
    }

    @Test(expected = ArithmeticException.class)
    public void safeDivide_withRoundingMode_byZero_throws() {
        FieldUtils.safeDivide(0L, 0L, RoundingMode.HALF_EVEN);
    }

    @Test(expected = NullPointerException.class)
    public void safeDivide_withRoundingMode_nullMode_throws() {
        FieldUtils.safeDivide(Long.MIN_VALUE, Long.MIN_VALUE, null);
    }

    // ---------------------------------------------------------------------
    // safeToInt
    // ---------------------------------------------------------------------

    @Test
    public void safeToInt_withinBounds_returnsValue() {
        assertEquals(0, FieldUtils.safeToInt(0L));
        assertEquals(Integer.MAX_VALUE, FieldUtils.safeToInt((long) Integer.MAX_VALUE));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeToInt((long) Integer.MIN_VALUE));
    }

    @Test(expected = ArithmeticException.class)
    public void safeToInt_overflowHigh_throws() {
        FieldUtils.safeToInt(Long.MAX_VALUE - 22L);
    }

    @Test(expected = ArithmeticException.class)
    public void safeToInt_overflowLow_throws() {
        FieldUtils.safeToInt((long) Integer.MIN_VALUE - 23L);
    }

    // ---------------------------------------------------------------------
    // safeMultiplyToInt
    // ---------------------------------------------------------------------

    @Test
    public void safeMultiplyToInt_withinBounds_returnsValue() {
        assertEquals(0, FieldUtils.safeMultiplyToInt(0L, 0L));
        assertEquals(2765, FieldUtils.safeMultiplyToInt(1L, 2765L));
        assertEquals(-2147483646, FieldUtils.safeMultiplyToInt(2147483646, -1L));
    }

    @Test(expected = ArithmeticException.class)
    public void safeMultiplyToInt_overflow_throws() {
        FieldUtils.safeMultiplyToInt(3_196L, Integer.MIN_VALUE);
    }

    @Test(expected = ArithmeticException.class)
    public void safeMultiplyToInt_longMultiplicationOverflow_throws() {
        FieldUtils.safeMultiplyToInt(26_607_895_200_000L, 26_607_895_200_000L);
    }

    // ---------------------------------------------------------------------
    // verifyValueBounds (String)
    // ---------------------------------------------------------------------

    @Test
    public void verifyValueBounds_withString_inRange_passes() {
        FieldUtils.verifyValueBounds("minuteOfHour", -5550, -5550, -5550);
        FieldUtils.verifyValueBounds("", 1, 1, 2000);
        FieldUtils.verifyValueBounds("any", -3236, -3236, -3236);
    }

    @Test(expected = IllegalArgumentException.class)
    public void verifyValueBounds_withString_outOfRange_throws() {
        FieldUtils.verifyValueBounds("", -81, -820, -820);
    }

    @Test(expected = IllegalArgumentException.class)
    public void verifyValueBounds_withString_invertedBounds_throws() {
        FieldUtils.verifyValueBounds("", -1702, 1472, 0);
    }

    // ---------------------------------------------------------------------
    // verifyValueBounds (DateTimeFieldType)
    // ---------------------------------------------------------------------

    @Test
    public void verifyValueBounds_withFieldType_inRange_passes() {
        DateTimeFieldType type = DateTimeFieldType.weekOfWeekyear();
        FieldUtils.verifyValueBounds(type, 1537, -2144353229, 1537);
    }

    @Test(expected = IllegalArgumentException.class)
    public void verifyValueBounds_withFieldType_outOfRange_throws() {
        DateTimeFieldType type = DateTimeFieldType.minuteOfHour();
        FieldUtils.verifyValueBounds(type, 1428, -1584, -473);
    }

    @Test(expected = NullPointerException.class)
    public void verifyValueBounds_withFieldType_nullTypeOutOfRange_throwsNpe() {
        FieldUtils.verifyValueBounds((DateTimeFieldType) null, 112, 12544, 12544);
    }

    // ---------------------------------------------------------------------
    // verifyValueBounds (DateTimeField)
    // Note: When the value is out of range and the field is null, the method
    // attempts to access field metadata for error messaging, which yields NPE.
    // ---------------------------------------------------------------------

    @Test
    public void verifyValueBounds_withField_nullFieldAndInRange_passes() {
        FieldUtils.verifyValueBounds((DateTimeField) null, 400, 400, 400);
    }

    @Test(expected = NullPointerException.class)
    public void verifyValueBounds_withField_nullFieldAndOutOfRange_throwsNpe() {
        FieldUtils.verifyValueBounds((DateTimeField) null, 24, -2147483646, -2147483646);
    }

    // ---------------------------------------------------------------------
    // getWrappedValue (4-arg)
    // ---------------------------------------------------------------------

    @Test
    public void getWrappedValue_addWithinBounds_returnsValue() {
        // 23 + 10 in [0,59] -> 33
        assertEquals(33, FieldUtils.getWrappedValue(23, 10, 0, 59));
    }

    @Test
    public void getWrappedValue_wrapsBelowMin_intoRange() {
        // (5 + -10) in [0,9] -> wraps to 5
        assertEquals(5, FieldUtils.getWrappedValue(5, -10, 0, 9));
    }

    @Test
    public void getWrappedValue_preservesBoundsOnEdges() {
        assertEquals(-436, FieldUtils.getWrappedValue(-436, 0, -436, 0));
        assertEquals(-1, FieldUtils.getWrappedValue(-2147483647, 0, -1, 0));
        assertEquals(0, FieldUtils.getWrappedValue(2149, 1610612735, -1, 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getWrappedValue_4arg_minGreaterOrEqualMax_throws() {
        FieldUtils.getWrappedValue(-1610612735, -1610612735, -1610612735, -1610612735);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getWrappedValue_4arg_minGreaterThanMax_throws() {
        FieldUtils.getWrappedValue(823, 317351877, 1363, -3977);
    }

    // ---------------------------------------------------------------------
    // getWrappedValue (3-arg)
    // ---------------------------------------------------------------------

    @Test
    public void getWrappedValue_3arg_wrapsIntoInclusiveRange() {
        // 61 in [0,59] -> 1
        assertEquals(1, FieldUtils.getWrappedValue(61, 0, 59));
        assertEquals(0, FieldUtils.getWrappedValue(Integer.MIN_VALUE, 0, 1));
        assertEquals(0, FieldUtils.getWrappedValue(0, -3236, 0));
        assertEquals(-477, FieldUtils.getWrappedValue(-4697, -4536, -317));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getWrappedValue_3arg_minEqualMax_throws() {
        FieldUtils.getWrappedValue(697, 697, 697);
    }

    // ---------------------------------------------------------------------
    // equals(Object, Object)
    // ---------------------------------------------------------------------

    @Test
    public void equals_handlesNullsAndIdentity() {
        Object o = new Object();
        assertTrue(FieldUtils.equals(o, o));
        assertFalse(FieldUtils.equals(o, null));
        assertFalse(FieldUtils.equals(null, o));
    }

    @Test
    public void equals_differentTypesNotEqual() {
        assertFalse(FieldUtils.equals(new Object(), Integer.valueOf(0)));
        assertFalse(FieldUtils.equals(Integer.valueOf(-1), null));
    }
}