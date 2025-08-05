/*
 *  Copyright 2001-2013 Stephen Colebourne
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

import org.joda.time.chrono.GJChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.JulianChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.SkipDateTimeField;

/**
 * Tests IllegalFieldValueException by triggering it from other methods.
 *
 * @author Brian S O'Neill
 */
public class TestIllegalFieldValueException {

    // ========================================================================
    // Tests for FieldUtils.verifyValueBounds()
    // ========================================================================

    @Test
    public void testVerifyValueBounds_DateTimeField_OutOfRange() {
        try {
            FieldUtils.verifyValueBounds(ISOChronology.getInstance().monthOfYear(), -5, 1, 31);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            assertEquals("Field type", DateTimeFieldType.monthOfYear(), e.getDateTimeFieldType());
            assertNull("Duration field type should be null", e.getDurationFieldType());
            assertEquals("Field name", "monthOfYear", e.getFieldName());
            assertEquals("Illegal number value", Integer.valueOf(-5), e.getIllegalNumberValue());
            assertNull("Illegal string value should be null", e.getIllegalStringValue());
            assertEquals("Illegal value as string", "-5", e.getIllegalValueAsString());
            assertEquals("Lower bound", Integer.valueOf(1), e.getLowerBound());
            assertEquals("Upper bound", Integer.valueOf(31), e.getUpperBound());
        }
    }

    @Test
    public void testVerifyValueBounds_DateTimeFieldType_OutOfRange() {
        try {
            FieldUtils.verifyValueBounds(DateTimeFieldType.hourOfDay(), 27, 0, 23);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            assertEquals("Field type", DateTimeFieldType.hourOfDay(), e.getDateTimeFieldType());
            assertNull("Duration field type should be null", e.getDurationFieldType());
            assertEquals("Field name", "hourOfDay", e.getFieldName());
            assertEquals("Illegal number value", Integer.valueOf(27), e.getIllegalNumberValue());
            assertNull("Illegal string value should be null", e.getIllegalStringValue());
            assertEquals("Illegal value as string", "27", e.getIllegalValueAsString());
            assertEquals("Lower bound", Integer.valueOf(0), e.getLowerBound());
            assertEquals("Upper bound", Integer.valueOf(23), e.getUpperBound());
        }
    }

    @Test
    public void testVerifyValueBounds_FieldName_OutOfRange() {
        try {
            FieldUtils.verifyValueBounds("foo", 1, 2, 3);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            assertNull("DateTime field type should be null", e.getDateTimeFieldType());
            assertNull("Duration field type should be null", e.getDurationFieldType());
            assertEquals("Field name", "foo", e.getFieldName());
            assertEquals("Illegal number value", Integer.valueOf(1), e.getIllegalNumberValue());
            assertNull("Illegal string value should be null", e.getIllegalStringValue());
            assertEquals("Illegal value as string", "1", e.getIllegalValueAsString());
            assertEquals("Lower bound", Integer.valueOf(2), e.getLowerBound());
            assertEquals("Upper bound", Integer.valueOf(3), e.getUpperBound());
        }
    }

    // ========================================================================
    // Tests for SkipDateTimeField
    // ========================================================================

    @Test
    public void testSkipDateTimeField_InvalidYear() {
        DateTimeField field = new SkipDateTimeField(
            ISOChronology.getInstanceUTC(), 
            ISOChronology.getInstanceUTC().year(), 
            1970
        );
        try {
            field.set(0, 1970);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            assertEquals("Field type", DateTimeFieldType.year(), e.getDateTimeFieldType());
            assertNull("Duration field type should be null", e.getDurationFieldType());
            assertEquals("Field name", "year", e.getFieldName());
            assertEquals("Illegal number value", Integer.valueOf(1970), e.getIllegalNumberValue());
            assertNull("Illegal string value should be null", e.getIllegalStringValue());
            assertEquals("Illegal value as string", "1970", e.getIllegalValueAsString());
            assertNull("Lower bound should be null", e.getLowerBound());
            assertNull("Upper bound should be null", e.getUpperBound());
        }
    }

    // ========================================================================
    // Tests for DateTimeField.setText()
    // ========================================================================

    @Test
    public void testSetText_YearField_NullText() {
        try {
            ISOChronology.getInstanceUTC().year().set(0, null, java.util.Locale.US);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            assertEquals("Field type", DateTimeFieldType.year(), e.getDateTimeFieldType());
            assertNull("Duration field type should be null", e.getDurationFieldType());
            assertEquals("Field name", "year", e.getFieldName());
            assertNull("Illegal number value should be null", e.getIllegalNumberValue());
            assertNull("Illegal string value should be null", e.getIllegalStringValue());
            assertEquals("Illegal value as string", "null", e.getIllegalValueAsString());
            assertNull("Lower bound should be null", e.getLowerBound());
            assertNull("Upper bound should be null", e.getUpperBound());
        }
    }

    @Test
    public void testSetText_YearField_InvalidText() {
        try {
            ISOChronology.getInstanceUTC().year().set(0, "nineteen seventy", java.util.Locale.US);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            assertEquals("Field type", DateTimeFieldType.year(), e.getDateTimeFieldType());
            assertNull("Duration field type should be null", e.getDurationFieldType());
            assertEquals("Field name", "year", e.getFieldName());
            assertNull("Illegal number value should be null", e.getIllegalNumberValue());
            assertEquals("Illegal string value", "nineteen seventy", e.getIllegalStringValue());
            assertEquals("Illegal value as string", "nineteen seventy", e.getIllegalValueAsString());
            assertNull("Lower bound should be null", e.getLowerBound());
            assertNull("Upper bound should be null", e.getUpperBound());
        }
    }

    @Test
    public void testSetText_EraField_InvalidText() {
        try {
            ISOChronology.getInstanceUTC().era().set(0, "long ago", java.util.Locale.US);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            assertEquals("Field type", DateTimeFieldType.era(), e.getDateTimeFieldType());
            assertNull("Duration field type should be null", e.getDurationFieldType());
            assertEquals("Field name", "era", e.getFieldName());
            assertNull("Illegal number value should be null", e.getIllegalNumberValue());
            assertEquals("Illegal string value", "long ago", e.getIllegalStringValue());
            assertEquals("Illegal value as string", "long ago", e.getIllegalValueAsString());
            assertNull("Lower bound should be null", e.getLowerBound());
            assertNull("Upper bound should be null", e.getUpperBound());
        }
    }

    @Test
    public void testSetText_MonthOfYearField_InvalidText() {
        try {
            ISOChronology.getInstanceUTC().monthOfYear().set(0, "spring", java.util.Locale.US);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            assertEquals("Field type", DateTimeFieldType.monthOfYear(), e.getDateTimeFieldType());
            assertNull("Duration field type should be null", e.getDurationFieldType());
            assertEquals("Field name", "monthOfYear", e.getFieldName());
            assertNull("Illegal number value should be null", e.getIllegalNumberValue());
            assertEquals("Illegal string value", "spring", e.getIllegalStringValue());
            assertEquals("Illegal value as string", "spring", e.getIllegalValueAsString());
            assertNull("Lower bound should be null", e.getLowerBound());
            assertNull("Upper bound should be null", e.getUpperBound());
        }
    }

    @Test
    public void testSetText_DayOfWeekField_InvalidText() {
        try {
            ISOChronology.getInstanceUTC().dayOfWeek().set(0, "yesterday", java.util.Locale.US);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            assertEquals("Field type", DateTimeFieldType.dayOfWeek(), e.getDateTimeFieldType());
            assertNull("Duration field type should be null", e.getDurationFieldType());
            assertEquals("Field name", "dayOfWeek", e.getFieldName());
            assertNull("Illegal number value should be null", e.getIllegalNumberValue());
            assertEquals("Illegal string value", "yesterday", e.getIllegalStringValue());
            assertEquals("Illegal value as string", "yesterday", e.getIllegalValueAsString());
            assertNull("Lower bound should be null", e.getLowerBound());
            assertNull("Upper bound should be null", e.getUpperBound());
        }
    }

    @Test
    public void testSetText_HalfdayOfDayField_InvalidText() {
        try {
            ISOChronology.getInstanceUTC().halfdayOfDay().set(0, "morning", java.util.Locale.US);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            assertEquals("Field type", DateTimeFieldType.halfdayOfDay(), e.getDateTimeFieldType());
            assertNull("Duration field type should be null", e.getDurationFieldType());
            assertEquals("Field name", "halfdayOfDay", e.getFieldName());
            assertNull("Illegal number value should be null", e.getIllegalNumberValue());
            assertEquals("Illegal string value", "morning", e.getIllegalStringValue());
            assertEquals("Illegal value as string", "morning", e.getIllegalValueAsString());
            assertNull("Lower bound should be null", e.getLowerBound());
            assertNull("Upper bound should be null", e.getUpperBound());
        }
    }

    // ========================================================================
    // Tests for DST zone transition
    // ========================================================================

    @Test
    public void testZoneTransition_InvalidHour() {
        DateTime dt = new DateTime(
            2005, 4, 3, 1, 0, 0, 0, DateTimeZone.forID("America/Los_Angeles"));
        try {
            dt.hourOfDay().setCopy(2);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            assertEquals("Field type", DateTimeFieldType.hourOfDay(), e.getDateTimeFieldType());
            assertNull("Duration field type should be null", e.getDurationFieldType());
            assertEquals("Field name", "hourOfDay", e.getFieldName());
            assertEquals("Illegal number value", Integer.valueOf(2), e.getIllegalNumberValue());
            assertNull("Illegal string value should be null", e.getIllegalStringValue());
            assertEquals("Illegal value as string", "2", e.getIllegalValueAsString());
            assertNull("Lower bound should be null", e.getLowerBound());
            assertNull("Upper bound should be null", e.getUpperBound());
        }
    }

    // ========================================================================
    // Tests for Julian chronology
    // ========================================================================

    @Test
    public void testJulianYearZero_InvalidYear() {
        DateTime dt = new DateTime(JulianChronology.getInstanceUTC());
        try {
            dt.year().setCopy(0);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            assertEquals("Field type", DateTimeFieldType.year(), e.getDateTimeFieldType());
            assertNull("Duration field type should be null", e.getDurationFieldType());
            assertEquals("Field name", "year", e.getFieldName());
            assertEquals("Illegal number value", Integer.valueOf(0), e.getIllegalNumberValue());
            assertNull("Illegal string value should be null", e.getIllegalStringValue());
            assertEquals("Illegal value as string", "0", e.getIllegalValueAsString());
            assertNull("Lower bound should be null", e.getLowerBound());
            assertNull("Upper bound should be null", e.getUpperBound());
        }
    }

    // ========================================================================
    // Tests for Gregorian/Julian cutover
    // ========================================================================

    @Test
    public void testGJCutover_FirstCutoverDay_InvalidDay() {
        DateTime dt = new DateTime("1582-10-04", GJChronology.getInstanceUTC());
        try {
            dt.dayOfMonth().setCopy(5);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            assertEquals("Field type", DateTimeFieldType.dayOfMonth(), e.getDateTimeFieldType());
            assertNull("Duration field type should be null", e.getDurationFieldType());
            assertEquals("Field name", "dayOfMonth", e.getFieldName());
            assertEquals("Illegal number value", Integer.valueOf(5), e.getIllegalNumberValue());
            assertNull("Illegal string value should be null", e.getIllegalStringValue());
            assertEquals("Illegal value as string", "5", e.getIllegalValueAsString());
            assertNull("Lower bound should be null", e.getLowerBound());
            assertNull("Upper bound should be null", e.getUpperBound());
        }
    }

    @Test
    public void testGJCutover_SecondCutoverDay_InvalidDay() {
        DateTime dt = new DateTime("1582-10-15", GJChronology.getInstanceUTC());
        try {
            dt.dayOfMonth().setCopy(14);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            assertEquals("Field type", DateTimeFieldType.dayOfMonth(), e.getDateTimeFieldType());
            assertNull("Duration field type should be null", e.getDurationFieldType());
            assertEquals("Field name", "dayOfMonth", e.getFieldName());
            assertEquals("Illegal number value", Integer.valueOf(14), e.getIllegalNumberValue());
            assertNull("Illegal string value should be null", e.getIllegalStringValue());
            assertEquals("Illegal value as string", "14", e.getIllegalValueAsString());
            assertNull("Lower bound should be null", e.getLowerBound());
            assertNull("Upper bound should be null", e.getUpperBound());
        }
    }

    // ========================================================================
    // Tests for ReadablePartial validation
    // ========================================================================

    @Test
    @SuppressWarnings("deprecation")
    public void testReadablePartialValidate_NegativeMonth() {
        try {
            new YearMonthDay(1970, -5, 1);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            assertEquals("Field type", DateTimeFieldType.monthOfYear(), e.getDateTimeFieldType());
            assertNull("Duration field type should be null", e.getDurationFieldType());
            assertEquals("Field name", "monthOfYear", e.getFieldName());
            assertEquals("Illegal number value", Integer.valueOf(-5), e.getIllegalNumberValue());
            assertNull("Illegal string value should be null", e.getIllegalStringValue());
            assertEquals("Illegal value as string", "-5", e.getIllegalValueAsString());
            assertEquals("Lower bound", Integer.valueOf(1), e.getLowerBound());
            assertNull("Upper bound should be null", e.getUpperBound());
        }
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testReadablePartialValidate_MonthTooHigh() {
        try {
            new YearMonthDay(1970, 500, 1);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            assertEquals("Field type", DateTimeFieldType.monthOfYear(), e.getDateTimeFieldType());
            assertNull("Duration field type should be null", e.getDurationFieldType());
            assertEquals("Field name", "monthOfYear", e.getFieldName());
            assertEquals("Illegal number value", Integer.valueOf(500), e.getIllegalNumberValue());
            assertNull("Illegal string value should be null", e.getIllegalStringValue());
            assertEquals("Illegal value as string", "500", e.getIllegalValueAsString());
            assertNull("Lower bound should be null", e.getLowerBound());
            assertEquals("Upper bound", Integer.valueOf(12), e.getUpperBound());
        }
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testReadablePartialValidate_DayTooHighForFebruary() {
        try {
            new YearMonthDay(1970, 2, 30);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            assertEquals("Field type", DateTimeFieldType.dayOfMonth(), e.getDateTimeFieldType());
            assertNull("Duration field type should be null", e.getDurationFieldType());
            assertEquals("Field name", "dayOfMonth", e.getFieldName());
            assertEquals("Illegal number value", Integer.valueOf(30), e.getIllegalNumberValue());
            assertNull("Illegal string value should be null", e.getIllegalStringValue());
            assertEquals("Illegal value as string", "30", e.getIllegalValueAsString());
            assertNull("Lower bound should be null", e.getLowerBound());
            assertEquals("Upper bound", Integer.valueOf(28), e.getUpperBound());
        }
    }

    // ========================================================================
    // Tests for additional constructors
    // ========================================================================

    @Test
    public void testConstructor_DurationFieldTypeWithBounds() {
        IllegalFieldValueException e = new IllegalFieldValueException(
            DurationFieldType.days(), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3));
        assertNull("DateTime field type should be null", e.getDateTimeFieldType());
        assertEquals("Duration field type", DurationFieldType.days(), e.getDurationFieldType());
        assertEquals("Field name", "days", e.getFieldName());
        assertEquals("Illegal number value", Integer.valueOf(1), e.getIllegalNumberValue());
        assertNull("Illegal string value should be null", e.getIllegalStringValue());
        assertEquals("Illegal value as string", "1", e.getIllegalValueAsString());
        assertEquals("Lower bound", Integer.valueOf(2), e.getLowerBound());
        assertEquals("Upper bound", Integer.valueOf(3), e.getUpperBound());
    }

    @Test
    public void testConstructor_DurationFieldTypeWithText() {
        IllegalFieldValueException e = new IllegalFieldValueException(DurationFieldType.months(), "five");
        assertNull("DateTime field type should be null", e.getDateTimeFieldType());
        assertEquals("Duration field type", DurationFieldType.months(), e.getDurationFieldType());
        assertEquals("Field name", "months", e.getFieldName());
        assertNull("Illegal number value should be null", e.getIllegalNumberValue());
        assertEquals("Illegal string value", "five", e.getIllegalStringValue());
        assertEquals("Illegal value as string", "five", e.getIllegalValueAsString());
        assertNull("Lower bound should be null", e.getLowerBound());
        assertNull("Upper bound should be null", e.getUpperBound());
    }

    @Test
    public void testConstructor_FieldNameWithText() {
        IllegalFieldValueException e = new IllegalFieldValueException("months", "five");
        assertNull("DateTime field type should be null", e.getDateTimeFieldType());
        assertNull("Duration field type should be null", e.getDurationFieldType());
        assertEquals("Field name", "months", e.getFieldName());
        assertNull("Illegal number value should be null", e.getIllegalNumberValue());
        assertEquals("Illegal string value", "five", e.getIllegalStringValue());
        assertEquals("Illegal value as string", "five", e.getIllegalValueAsString());
        assertNull("Lower bound should be null", e.getLowerBound());
        assertNull("Upper bound should be null", e.getUpperBound());
    }
}