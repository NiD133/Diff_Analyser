package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.*;
import java.math.RoundingMode;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.IslamicChronology;
import org.joda.time.chrono.ZonedChronology;

/**
 * Test suite for FieldUtils utility methods.
 * Tests cover arithmetic operations, value bounds verification, and value wrapping.
 */
public class FieldUtils_ESTest {

    // ========== Value Wrapping Tests ==========
    
    @Test
    public void testGetWrappedValue_ValueBelowRange_WrapsToMaxValue() {
        // When value is below minimum, it should wrap to the maximum end
        int result = FieldUtils.getWrappedValue(-2147483647, 0, -1, 0);
        assertEquals(-1, result);
    }

    @Test
    public void testGetWrappedValue_ValueEqualsMinimum_ReturnsValue() {
        int result = FieldUtils.getWrappedValue(-436, 0, -436, 0);
        assertEquals(-436, result);
    }

    @Test
    public void testGetWrappedValue_MinGreaterThanMax_ThrowsException() {
        try {
            FieldUtils.getWrappedValue(823, 317351877, 1363, -3977); // min > max
            fail("Expected IllegalArgumentException for invalid range");
        } catch (IllegalArgumentException e) {
            assertEquals("MIN > MAX", e.getMessage());
        }
    }

    @Test
    public void testGetWrappedValue_LargeNegativeValue_WrapsCorrectly() {
        int result = FieldUtils.getWrappedValue(Integer.MIN_VALUE, 0, 0, 1756);
        assertEquals(2697, result);
    }

    @Test
    public void testGetWrappedValue_ValueAboveRange_WrapsToLowerRange() {
        int result = FieldUtils.getWrappedValue(1055, 1671, 352831696);
        assertEquals(352831081, result);
    }

    @Test
    public void testGetWrappedValue_ValueInRange_ReturnsValue() {
        int result = FieldUtils.getWrappedValue(0, -3236, 0);
        assertEquals(0, result);
    }

    @Test
    public void testGetWrappedValue_EqualMinMax_ThrowsException() {
        try {
            FieldUtils.getWrappedValue(697, 697, 697); // min == max
            fail("Expected IllegalArgumentException when min equals max");
        } catch (IllegalArgumentException e) {
            assertEquals("MIN > MAX", e.getMessage());
        }
    }

    // ========== Value Bounds Verification Tests ==========

    @Test
    public void testVerifyValueBounds_ValueInRange_NoException() {
        // Should not throw when value is within bounds
        FieldUtils.verifyValueBounds("testField", 1, 1, 2000);
    }

    @Test
    public void testVerifyValueBounds_ValueOutOfRange_ThrowsException() {
        try {
            FieldUtils.verifyValueBounds("", -81, -820, -820);
            fail("Expected IllegalArgumentException for out of range value");
        } catch (IllegalArgumentException e) {
            assertEquals("Value -81 for  must be in the range [-820,-820]", e.getMessage());
        }
    }

    @Test
    public void testVerifyValueBounds_WithDateTimeFieldType_ValueInRange() {
        DateTimeFieldType minuteField = DateTimeFieldType.minuteOfHour();
        FieldUtils.verifyValueBounds(minuteField, -5550, -5550, -5550);
        assertEquals("minuteOfHour", minuteField.toString());
    }

    @Test
    public void testVerifyValueBounds_WithDateTimeFieldType_ValueOutOfRange() {
        DateTimeFieldType minuteField = DateTimeFieldType.minuteOfHour();
        try {
            FieldUtils.verifyValueBounds(minuteField, 1428, -1584, -473);
            fail("Expected IllegalArgumentException for out of range value");
        } catch (IllegalArgumentException e) {
            assertEquals("Value 1428 for minuteOfHour must be in the range [-1584,-473]", e.getMessage());
        }
    }

    @Test
    public void testVerifyValueBounds_NullDateTimeField_ThrowsNullPointer() {
        try {
            FieldUtils.verifyValueBounds((DateTimeField) null, 24, -2147483646, -2147483646);
            fail("Expected NullPointerException for null field");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    // ========== Safe Arithmetic Operations Tests ==========

    @Test
    public void testSafeToInt_MaxIntValue_ReturnsMaxInt() {
        int result = FieldUtils.safeToInt(2147483647L);
        assertEquals(Integer.MAX_VALUE, result);
    }

    @Test
    public void testSafeToInt_MinIntValue_ReturnsMinInt() {
        int result = FieldUtils.safeToInt(-2147483648L);
        assertEquals(Integer.MIN_VALUE, result);
    }

    @Test
    public void testSafeToInt_ValueTooLarge_ThrowsArithmeticException() {
        try {
            FieldUtils.safeToInt(9223372036854775785L);
            fail("Expected ArithmeticException for value too large for int");
        } catch (ArithmeticException e) {
            assertEquals("Value cannot fit in an int: 9223372036854775785", e.getMessage());
        }
    }

    @Test
    public void testSafeToInt_ValueTooSmall_ThrowsArithmeticException() {
        try {
            FieldUtils.safeToInt(-2147483671L);
            fail("Expected ArithmeticException for value too small for int");
        } catch (ArithmeticException e) {
            assertEquals("Value cannot fit in an int: -2147483671", e.getMessage());
        }
    }

    @Test
    public void testSafeAdd_IntegerOverflow_ThrowsArithmeticException() {
        try {
            FieldUtils.safeAdd(Integer.MAX_VALUE, Integer.MAX_VALUE);
            fail("Expected ArithmeticException for integer overflow");
        } catch (ArithmeticException e) {
            assertEquals("The calculation caused an overflow: 2147483647 + 2147483647", e.getMessage());
        }
    }

    @Test
    public void testSafeAdd_IntegerUnderflow_ThrowsArithmeticException() {
        try {
            FieldUtils.safeAdd(-1640, Integer.MIN_VALUE);
            fail("Expected ArithmeticException for integer underflow");
        } catch (ArithmeticException e) {
            assertEquals("The calculation caused an overflow: -1640 + -2147483648", e.getMessage());
        }
    }

    @Test
    public void testSafeAdd_ValidIntegers_ReturnsSum() {
        int result = FieldUtils.safeAdd(4504, 4504);
        assertEquals(9008, result);
    }

    @Test
    public void testSafeAdd_LongOverflow_ThrowsArithmeticException() {
        try {
            FieldUtils.safeAdd(-610L, -9223372036854775808L);
            fail("Expected ArithmeticException for long overflow");
        } catch (ArithmeticException e) {
            assertEquals("The calculation caused an overflow: -610 + -9223372036854775808", e.getMessage());
        }
    }

    @Test
    public void testSafeAdd_ValidLongs_ReturnsSum() {
        long result = FieldUtils.safeAdd(-1185L, -1185L);
        assertEquals(-2370L, result);
    }

    @Test
    public void testSafeSubtract_ValidOperation_ReturnsResult() {
        long result = FieldUtils.safeSubtract(3600L, 0L);
        assertEquals(3600L, result);
    }

    @Test
    public void testSafeSubtract_LongUnderflow_ThrowsArithmeticException() {
        try {
            FieldUtils.safeSubtract(-9223372036854775808L, 11L);
            fail("Expected ArithmeticException for long underflow");
        } catch (ArithmeticException e) {
            assertEquals("The calculation caused an overflow: -9223372036854775808 - 11", e.getMessage());
        }
    }

    @Test
    public void testSafeMultiply_IntegerOverflow_ThrowsArithmeticException() {
        try {
            FieldUtils.safeMultiply(Integer.MIN_VALUE, Integer.MIN_VALUE);
            fail("Expected ArithmeticException for integer multiplication overflow");
        } catch (ArithmeticException e) {
            assertEquals("Multiplication overflows an int: -2147483648 * -2147483648", e.getMessage());
        }
    }

    @Test
    public void testSafeMultiply_LongOverflow_ThrowsArithmeticException() {
        try {
            FieldUtils.safeMultiply(-9223372036854775808L, -7);
            fail("Expected ArithmeticException for long multiplication overflow");
        } catch (ArithmeticException e) {
            assertEquals("Multiplication overflows a long: -9223372036854775808 * -7", e.getMessage());
        }
    }

    @Test
    public void testSafeMultiply_ValidIntegers_ReturnsProduct() {
        int result = FieldUtils.safeMultiply(-1, -2147483647);
        assertEquals(Integer.MAX_VALUE, result);
    }

    @Test
    public void testSafeMultiply_ValidLongs_ReturnsProduct() {
        long result = FieldUtils.safeMultiply(24L, 24L);
        assertEquals(576L, result);
    }

    @Test
    public void testSafeNegate_MinValue_ThrowsArithmeticException() {
        try {
            FieldUtils.safeNegate(Integer.MIN_VALUE);
            fail("Expected ArithmeticException when negating Integer.MIN_VALUE");
        } catch (ArithmeticException e) {
            assertEquals("Integer.MIN_VALUE cannot be negated", e.getMessage());
        }
    }

    @Test
    public void testSafeNegate_ValidValue_ReturnsNegation() {
        int result = FieldUtils.safeNegate(-70);
        assertEquals(70, result);
    }

    @Test
    public void testSafeNegate_Zero_ReturnsZero() {
        int result = FieldUtils.safeNegate(0);
        assertEquals(0, result);
    }

    // ========== Safe Division Tests ==========

    @Test
    public void testSafeDivide_DivisionByZero_ThrowsArithmeticException() {
        try {
            FieldUtils.safeDivide(0L, 0L);
            fail("Expected ArithmeticException for division by zero");
        } catch (ArithmeticException e) {
            assertEquals("/ by zero", e.getMessage());
        }
    }

    @Test
    public void testSafeDivide_ValidDivision_ReturnsQuotient() {
        long result = FieldUtils.safeDivide(-9223372036854775808L, 33L);
        assertEquals(-279496122328932600L, result);
    }

    @Test
    public void testSafeDivide_WithRoundingMode_ReturnsRoundedResult() {
        RoundingMode ceilingMode = RoundingMode.CEILING;
        long result = FieldUtils.safeDivide(0L, 218L, ceilingMode);
        assertEquals(0L, result);
    }

    @Test
    public void testSafeDivide_OverflowCondition_ThrowsArithmeticException() {
        try {
            FieldUtils.safeDivide(-9223372036854775808L, -1L);
            fail("Expected ArithmeticException for division overflow");
        } catch (ArithmeticException e) {
            assertEquals("Multiplication overflows a long: -9223372036854775808 / -1", e.getMessage());
        }
    }

    // ========== Utility Method Tests ==========

    @Test
    public void testEquals_BothNull_ReturnsTrue() {
        boolean result = FieldUtils.equals(null, null);
        assertTrue(result);
    }

    @Test
    public void testEquals_OneNull_ReturnsFalse() {
        Integer value = new Integer(-1);
        boolean result = FieldUtils.equals(value, null);
        assertFalse(result);
    }

    @Test
    public void testEquals_SameObject_ReturnsTrue() {
        Object obj = new Object();
        boolean result = FieldUtils.equals(obj, obj);
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentObjects_ReturnsFalse() {
        Object obj1 = new Object();
        Integer obj2 = new Integer(0);
        boolean result = FieldUtils.equals(obj1, obj2);
        assertFalse(result);
    }

    // ========== Edge Case Tests ==========

    @Test
    public void testSafeMultiplyToInt_Overflow_ThrowsArithmeticException() {
        try {
            FieldUtils.safeMultiplyToInt(3196L, Integer.MIN_VALUE);
            fail("Expected ArithmeticException for multiplication result too large for int");
        } catch (ArithmeticException e) {
            assertEquals("Value cannot fit in an int: -6863357739008", e.getMessage());
        }
    }

    @Test
    public void testSafeMultiplyToInt_ValidOperation_ReturnsInt() {
        int result = FieldUtils.safeMultiplyToInt(1L, 2765L);
        assertEquals(2765, result);
    }

    @Test
    public void testSafeMultiplyToInt_ZeroMultiplication_ReturnsZero() {
        int result = FieldUtils.safeMultiplyToInt(0L, 0L);
        assertEquals(0, result);
    }

    // ========== Complex DateTime Field Tests ==========

    @Test
    public void testVerifyValueBounds_WithComplexDateTimeField_ValidValue() {
        DateTimeZone utc = DateTimeZone.UTC;
        IslamicChronology islamic = IslamicChronology.getInstance(utc);
        ZonedChronology zoned = ZonedChronology.getInstance(islamic, utc);
        DateTimeField minuteField = zoned.minuteOfHour();
        
        // Should not throw for valid bounds
        FieldUtils.verifyValueBounds(minuteField, 400, 400, 400);
    }

    @Test
    public void testVerifyValueBounds_WithComplexDateTimeField_InvalidRange() {
        DateTimeZone utc = DateTimeZone.UTC;
        IslamicChronology islamic = IslamicChronology.getInstance(utc);
        ZonedChronology zoned = ZonedChronology.getInstance(islamic, utc);
        DateTimeField minuteField = zoned.minuteOfHour();
        
        try {
            FieldUtils.verifyValueBounds(minuteField, 1, 1, -1); // invalid range
            fail("Expected IllegalArgumentException for invalid range");
        } catch (IllegalArgumentException e) {
            assertEquals("Value 1 for minuteOfHour must be in the range [1,-1]", e.getMessage());
        }
    }
}