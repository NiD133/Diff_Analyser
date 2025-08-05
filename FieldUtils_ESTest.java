/*
 * Copyright 2001-2005 Stephen Colebourne
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.joda.time.field;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.chrono.IslamicChronology;
import org.junit.Test;

import java.math.RoundingMode;

import static org.junit.Assert.*;

/**
 * Understandable and maintainable unit tests for {@link FieldUtils}.
 */
public class FieldUtilsTest {

    // --- safeAdd ---

    @Test
    public void safeAdd_int_shouldAddValuesCorrectly() {
        assertEquals(5, FieldUtils.safeAdd(2, 3));
        assertEquals(-5, FieldUtils.safeAdd(-2, -3));
        assertEquals(0, FieldUtils.safeAdd(5, -5));
        assertEquals(Integer.MAX_VALUE, FieldUtils.safeAdd(Integer.MAX_VALUE, 0));
    }

    @Test
    public void safeAdd_int_shouldThrowExceptionOnPositiveOverflow() {
        try {
            FieldUtils.safeAdd(Integer.MAX_VALUE, 1);
            fail("Expected ArithmeticException for positive overflow");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void safeAdd_int_shouldThrowExceptionOnNegativeOverflow() {
        try {
            FieldUtils.safeAdd(Integer.MIN_VALUE, -1);
            fail("Expected ArithmeticException for negative overflow");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void safeAdd_long_shouldAddValuesCorrectly() {
        assertEquals(5L, FieldUtils.safeAdd(2L, 3L));
        assertEquals(-5L, FieldUtils.safeAdd(-2L, -3L));
        assertEquals(0L, FieldUtils.safeAdd(5L, -5L));
        assertEquals(Long.MAX_VALUE, FieldUtils.safeAdd(Long.MAX_VALUE, 0L));
    }

    @Test
    public void safeAdd_long_shouldThrowExceptionOnPositiveOverflow() {
        try {
            FieldUtils.safeAdd(Long.MAX_VALUE, 1L);
            fail("Expected ArithmeticException for positive overflow");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void safeAdd_long_shouldThrowExceptionOnNegativeOverflow() {
        try {
            FieldUtils.safeAdd(Long.MIN_VALUE, -1L);
            fail("Expected ArithmeticException for negative overflow");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    // --- safeSubtract ---

    @Test
    public void safeSubtract_long_shouldSubtractValuesCorrectly() {
        assertEquals(-1L, FieldUtils.safeSubtract(2L, 3L));
        assertEquals(3600L, FieldUtils.safeSubtract(3600L, 0L));
        assertEquals(0L, FieldUtils.safeSubtract(Long.MIN_VALUE, Long.MIN_VALUE));
    }

    @Test
    public void safeSubtract_long_shouldThrowExceptionOnPositiveOverflow() {
        try {
            // Equivalent to Long.MAX_VALUE + 1
            FieldUtils.safeSubtract(Long.MAX_VALUE, -1L);
            fail("Expected ArithmeticException for positive overflow");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void safeSubtract_long_shouldThrowExceptionOnNegativeOverflow() {
        try {
            FieldUtils.safeSubtract(Long.MIN_VALUE, 1L);
            fail("Expected ArithmeticException for negative overflow");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    // --- safeMultiply ---

    @Test
    public void safeMultiply_int_shouldMultiplyWithoutOverflow() {
        assertEquals(6, FieldUtils.safeMultiply(2, 3));
        assertEquals(0, FieldUtils.safeMultiply(Integer.MAX_VALUE, 0));
        assertEquals(Integer.MAX_VALUE, FieldUtils.safeMultiply(Integer.MAX_VALUE, 1));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeMultiply(Integer.MIN_VALUE, 1));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeMultiply(1073741824, -2));
    }

    @Test
    public void safeMultiply_int_shouldThrowExceptionOnOverflow() {
        try {
            FieldUtils.safeMultiply(Integer.MAX_VALUE, 2);
            fail("Expected ArithmeticException for positive overflow");
        } catch (ArithmeticException e) {
            // Expected
        }

        try {
            FieldUtils.safeMultiply(Integer.MIN_VALUE, 2);
            fail("Expected ArithmeticException for negative overflow");
        } catch (ArithmeticException e) {
            // Expected
        }

        try {
            FieldUtils.safeMultiply(Integer.MIN_VALUE, -1);
            fail("Expected ArithmeticException for MIN_VALUE * -1");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void safeMultiply_long_long_shouldMultiplyWithoutOverflow() {
        assertEquals(6L, FieldUtils.safeMultiply(2L, 3L));
        assertEquals(0L, FieldUtils.safeMultiply(Long.MAX_VALUE, 0L));
        assertEquals(Long.MAX_VALUE, FieldUtils.safeMultiply(Long.MAX_VALUE, 1L));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeMultiply(Long.MIN_VALUE, 1L));
    }

    @Test
    public void safeMultiply_long_long_shouldThrowExceptionOnOverflow() {
        try {
            FieldUtils.safeMultiply(Long.MAX_VALUE, 2L);
            fail("Expected ArithmeticException for positive overflow");
        } catch (ArithmeticException e) {
            // Expected
        }

        try {
            FieldUtils.safeMultiply(Long.MIN_VALUE, -1L);
            fail("Expected ArithmeticException for MIN_VALUE * -1");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void safeMultiply_long_int_shouldThrowExceptionOnOverflow() {
        try {
            FieldUtils.safeMultiply(Long.MAX_VALUE, 2);
            fail("Expected ArithmeticException for positive overflow");
        } catch (ArithmeticException e) {
            // Expected
        }

        try {
            FieldUtils.safeMultiply(Long.MIN_VALUE, -1);
            fail("Expected ArithmeticException for MIN_VALUE * -1");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    // --- safeDivide ---

    @Test
    public void safeDivide_long_shouldDivideCorrectly() {
        assertEquals(3L, FieldUtils.safeDivide(10L, 3L));
        assertEquals(-3L, FieldUtils.safeDivide(-10L, 3L));
        assertEquals(0L, FieldUtils.safeDivide(0L, 5L));
    }

    @Test
    public void safeDivide_long_shouldThrowExceptionOnDivisionByZero() {
        try {
            FieldUtils.safeDivide(10L, 0L);
            fail("Expected ArithmeticException for division by zero");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void safeDivide_long_shouldThrowExceptionOnOverflow() {
        try {
            FieldUtils.safeDivide(Long.MIN_VALUE, -1L);
            fail("Expected ArithmeticException for overflow (MIN_VALUE / -1)");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void safeDivide_long_withRounding_shouldDivideCorrectly() {
        assertEquals(1L, FieldUtils.safeDivide(Long.MIN_VALUE, Long.MIN_VALUE, RoundingMode.FLOOR));
        assertEquals(0L, FieldUtils.safeDivide(0L, 218L, RoundingMode.CEILING));
        assertEquals(-28913391965061994L, FieldUtils.safeDivide(Long.MIN_VALUE, 319L, RoundingMode.UP));
    }

    @Test
    public void safeDivide_long_withRounding_shouldThrowExceptionOnDivisionByZero() {
        try {
            FieldUtils.safeDivide(10L, 0L, RoundingMode.HALF_UP);
            fail("Expected ArithmeticException for division by zero");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    // --- safeNegate ---

    @Test
    public void safeNegate_shouldNegateValue() {
        assertEquals(-1, FieldUtils.safeNegate(1));
        assertEquals(1, FieldUtils.safeNegate(-1));
        assertEquals(0, FieldUtils.safeNegate(0));
    }

    @Test
    public void safeNegate_shouldThrowExceptionOnMinValue() {
        try {
            FieldUtils.safeNegate(Integer.MIN_VALUE);
            fail("Expected ArithmeticException for negating MIN_VALUE");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    // --- safeToInt ---

    @Test
    public void safeToInt_shouldConvertValueWithinIntRange() {
        assertEquals(123, FieldUtils.safeToInt(123L));
        assertEquals(Integer.MAX_VALUE, FieldUtils.safeToInt(Integer.MAX_VALUE));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeToInt(Integer.MIN_VALUE));
    }

    @Test
    public void safeToInt_shouldThrowExceptionOnOverflow() {
        try {
            FieldUtils.safeToInt((long) Integer.MAX_VALUE + 1);
            fail("Expected ArithmeticException for overflow");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void safeToInt_shouldThrowExceptionOnUnderflow() {
        try {
            FieldUtils.safeToInt((long) Integer.MIN_VALUE - 1);
            fail("Expected ArithmeticException for underflow");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    // --- safeMultiplyToInt ---

    @Test
    public void safeMultiplyToInt_shouldMultiplyAndConvert() {
        assertEquals(100, FieldUtils.safeMultiplyToInt(10L, 10L));
        assertEquals(0, FieldUtils.safeMultiplyToInt(Long.MAX_VALUE, 0L));
    }

    @Test
    public void safeMultiplyToInt_shouldThrowExceptionOnResultOverflow() {
        try {
            FieldUtils.safeMultiplyToInt((long) Integer.MAX_VALUE, 2L);
            fail("Expected ArithmeticException for result overflow");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    // --- verifyValueBounds ---

    @Test
    public void verifyValueBounds_string_shouldPassForValidValue() {
        FieldUtils.verifyValueBounds("testField", 5, 0, 10);
        FieldUtils.verifyValueBounds("testField", 0, 0, 10); // Lower bound
        FieldUtils.verifyValueBounds("testField", 10, 0, 10); // Upper bound
    }

    @Test
    public void verifyValueBounds_string_shouldThrowExceptionForInvalidValue() {
        try {
            FieldUtils.verifyValueBounds("testField", -1, 0, 10);
            fail("Expected IllegalFieldValueException for value below minimum");
        } catch (IllegalFieldValueException e) {
            assertTrue(e.getMessage().contains("Value -1 for testField must be in the range [0,10]"));
        }

        try {
            FieldUtils.verifyValueBounds("testField", 11, 0, 10);
            fail("Expected IllegalFieldValueException for value above maximum");
        } catch (IllegalFieldValueException e) {
            assertTrue(e.getMessage().contains("Value 11 for testField must be in the range [0,10]"));
        }
    }

    @Test
    public void verifyValueBounds_fieldType_shouldPassForValidValue() {
        DateTimeFieldType type = DateTimeFieldType.dayOfMonth();
        FieldUtils.verifyValueBounds(type, 15, 1, 31);
    }

    @Test
    public void verifyValueBounds_fieldType_shouldThrowExceptionForInvalidValue() {
        DateTimeFieldType type = DateTimeFieldType.dayOfMonth();
        try {
            FieldUtils.verifyValueBounds(type, 32, 1, 31);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            assertEquals(type, e.getDateTimeFieldType());
            assertTrue(e.getMessage().contains("Value 32 for dayOfMonth must be in the range [1,31]"));
        }
    }

    @Test
    public void verifyValueBounds_field_shouldPassForValidValue() {
        DateTimeField field = IslamicChronology.getInstanceUTC().dayOfMonth();
        FieldUtils.verifyValueBounds(field, 15, 1, 30);
    }

    @Test
    public void verifyValueBounds_field_shouldThrowExceptionForInvalidValue() {
        DateTimeField field = IslamicChronology.getInstanceUTC().dayOfMonth();
        try {
            FieldUtils.verifyValueBounds(field, 31, 1, 30);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            assertEquals(field.getType(), e.getDateTimeFieldType());
            assertTrue(e.getMessage().contains("Value 31 for dayOfMonth must be in the range [1,30]"));
        }
    }

    // --- getWrappedValue ---

    @Test
    public void getWrappedValue_shouldWrapAroundRange() {
        // e.g., For a 1-12 month field, 13 wraps to 1, and 0 wraps to 12
        assertEquals(1, FieldUtils.getWrappedValue(13, 1, 12));
        assertEquals(12, FieldUtils.getWrappedValue(0, 1, 12));
        assertEquals(2, FieldUtils.getWrappedValue(14, 1, 12));
        assertEquals(11, FieldUtils.getWrappedValue(-1, 1, 12));
    }

    @Test
    public void getWrappedValue_shouldReturnSameValueIfWithinBounds() {
        assertEquals(5, FieldUtils.getWrappedValue(5, 1, 12));
    }

    @Test
    public void getWrappedValue_shouldHandleEdgeCases() {
        assertEquals(0, FieldUtils.getWrappedValue(Integer.MIN_VALUE, 0, 1));
    }

    @Test
    public void getWrappedValue_shouldThrowExceptionIfMinNotLessThanMax() {
        try {
            FieldUtils.getWrappedValue(10, 5, 5);
            fail("Expected IllegalArgumentException for min >= max");
        } catch (IllegalArgumentException e) {
            assertEquals("MIN > MAX", e.getMessage());
        }
    }

    @Test
    public void getWrappedValue_shouldThrowExceptionOnRangeOverflow() {
        // The range (max - min + 1) overflows an int, leading to division by zero
        try {
            FieldUtils.getWrappedValue(0, Integer.MIN_VALUE, Integer.MAX_VALUE);
            fail("Expected ArithmeticException due to range overflow");
        } catch (ArithmeticException e) {
            assertEquals("/ by zero", e.getMessage());
        }
    }

    @Test
    public void getWrappedValue_withAmount_shouldWrapCorrectly() {
        // e.g., For a 0-59 minute field, at 58, add 3 minutes -> 1
        assertEquals(1, FieldUtils.getWrappedValue(58, 3, 0, 59));
        // e.g., For a 0-59 minute field, at 1, subtract 3 minutes -> 58
        assertEquals(58, FieldUtils.getWrappedValue(1, -3, 0, 59));
    }

    // --- equals ---

    @Test
    public void equals_shouldHandleNullsAndObjectsCorrectly() {
        Object obj = new Object();
        assertTrue("Same object should be equal", FieldUtils.equals(obj, obj));
        assertTrue("Two nulls should be equal", FieldUtils.equals(null, null));
        assertTrue("Equal strings should be equal", FieldUtils.equals("hello", "hello"));

        assertFalse("Object and null should not be equal", FieldUtils.equals(obj, null));
        assertFalse("Null and object should not be equal", FieldUtils.equals(null, obj));
        assertFalse("Different strings should not be equal", FieldUtils.equals("hello", "world"));
        assertFalse("Different types should not be equal", FieldUtils.equals("hello", new Object()));
    }
}