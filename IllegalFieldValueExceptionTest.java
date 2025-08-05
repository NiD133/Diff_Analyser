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

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.chrono.GJChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.JulianChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.SkipDateTimeField;

/**
 * Tests IllegalFieldValueException by triggering it from various Joda-Time operations
 * and verifying the exception contains the correct field information.
 *
 * @author Brian S O'Neill
 */
public class TestIllegalFieldValueException extends TestCase {
    
    // Test constants for better readability
    private static final int INVALID_MONTH = -5;
    private static final int VALID_MONTH_MIN = 1;
    private static final int VALID_MONTH_MAX = 31;
    
    private static final int INVALID_HOUR = 27;
    private static final int VALID_HOUR_MIN = 0;
    private static final int VALID_HOUR_MAX = 23;
    
    private static final int SKIPPED_YEAR = 1970;
    private static final String INVALID_YEAR_TEXT = "nineteen seventy";
    private static final String INVALID_ERA_TEXT = "long ago";
    private static final String INVALID_MONTH_TEXT = "spring";
    private static final String INVALID_DAY_TEXT = "yesterday";
    private static final String INVALID_HALFDAY_TEXT = "morning";
    
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestIllegalFieldValueException.class);
    }

    public TestIllegalFieldValueException(String name) {
        super(name);
    }

    /**
     * Tests that FieldUtils.verifyValueBounds throws IllegalFieldValueException
     * with correct field information when values are outside valid bounds.
     */
    public void testVerifyValueBounds() {
        testVerifyValueBounds_WithDateTimeField();
        testVerifyValueBounds_WithDateTimeFieldType();
        testVerifyValueBounds_WithStringFieldName();
    }
    
    private void testVerifyValueBounds_WithDateTimeField() {
        try {
            FieldUtils.verifyValueBounds(ISOChronology.getInstance().monthOfYear(), 
                                       INVALID_MONTH, VALID_MONTH_MIN, VALID_MONTH_MAX);
            fail("Expected IllegalFieldValueException for invalid month value");
        } catch (IllegalFieldValueException e) {
            assertExceptionDetails(e, DateTimeFieldType.monthOfYear(), null, "monthOfYear",
                                 INVALID_MONTH, null, String.valueOf(INVALID_MONTH),
                                 VALID_MONTH_MIN, VALID_MONTH_MAX);
        }
    }
    
    private void testVerifyValueBounds_WithDateTimeFieldType() {
        try {
            FieldUtils.verifyValueBounds(DateTimeFieldType.hourOfDay(), 
                                       INVALID_HOUR, VALID_HOUR_MIN, VALID_HOUR_MAX);
            fail("Expected IllegalFieldValueException for invalid hour value");
        } catch (IllegalFieldValueException e) {
            assertExceptionDetails(e, DateTimeFieldType.hourOfDay(), null, "hourOfDay",
                                 INVALID_HOUR, null, String.valueOf(INVALID_HOUR),
                                 VALID_HOUR_MIN, VALID_HOUR_MAX);
        }
    }
    
    private void testVerifyValueBounds_WithStringFieldName() {
        String fieldName = "foo";
        int invalidValue = 1;
        int lowerBound = 2;
        int upperBound = 3;
        
        try {
            FieldUtils.verifyValueBounds(fieldName, invalidValue, lowerBound, upperBound);
            fail("Expected IllegalFieldValueException for value outside bounds");
        } catch (IllegalFieldValueException e) {
            assertExceptionDetails(e, null, null, fieldName,
                                 invalidValue, null, String.valueOf(invalidValue),
                                 lowerBound, upperBound);
        }
    }

    /**
     * Tests that SkipDateTimeField throws IllegalFieldValueException
     * when attempting to set a skipped value (like year 1970 in this test).
     */
    public void testSkipDateTimeField() {
        DateTimeField skipField = new SkipDateTimeField(
            ISOChronology.getInstanceUTC(), 
            ISOChronology.getInstanceUTC().year(), 
            SKIPPED_YEAR
        );
        
        try {
            skipField.set(0, SKIPPED_YEAR);
            fail("Expected IllegalFieldValueException for skipped year value");
        } catch (IllegalFieldValueException e) {
            assertExceptionDetails(e, DateTimeFieldType.year(), null, "year",
                                 SKIPPED_YEAR, null, String.valueOf(SKIPPED_YEAR),
                                 null, null);
        }
    }

    /**
     * Tests that setting invalid text values on various date/time fields
     * throws IllegalFieldValueException with correct string value information.
     */
    public void testSetText() {
        testSetText_NullValue();
        testSetText_InvalidYearText();
        testSetText_InvalidEraText();
        testSetText_InvalidMonthText();
        testSetText_InvalidDayOfWeekText();
        testSetText_InvalidHalfdayText();
    }
    
    private void testSetText_NullValue() {
        try {
            ISOChronology.getInstanceUTC().year().set(0, null, java.util.Locale.US);
            fail("Expected IllegalFieldValueException for null year text");
        } catch (IllegalFieldValueException e) {
            assertExceptionDetails(e, DateTimeFieldType.year(), null, "year",
                                 null, null, "null", null, null);
        }
    }
    
    private void testSetText_InvalidYearText() {
        try {
            ISOChronology.getInstanceUTC().year().set(0, INVALID_YEAR_TEXT, java.util.Locale.US);
            fail("Expected IllegalFieldValueException for invalid year text");
        } catch (IllegalFieldValueException e) {
            assertExceptionDetails(e, DateTimeFieldType.year(), null, "year",
                                 null, INVALID_YEAR_TEXT, INVALID_YEAR_TEXT, null, null);
        }
    }
    
    private void testSetText_InvalidEraText() {
        try {
            ISOChronology.getInstanceUTC().era().set(0, INVALID_ERA_TEXT, java.util.Locale.US);
            fail("Expected IllegalFieldValueException for invalid era text");
        } catch (IllegalFieldValueException e) {
            assertExceptionDetails(e, DateTimeFieldType.era(), null, "era",
                                 null, INVALID_ERA_TEXT, INVALID_ERA_TEXT, null, null);
        }
    }
    
    private void testSetText_InvalidMonthText() {
        try {
            ISOChronology.getInstanceUTC().monthOfYear().set(0, INVALID_MONTH_TEXT, java.util.Locale.US);
            fail("Expected IllegalFieldValueException for invalid month text");
        } catch (IllegalFieldValueException e) {
            assertExceptionDetails(e, DateTimeFieldType.monthOfYear(), null, "monthOfYear",
                                 null, INVALID_MONTH_TEXT, INVALID_MONTH_TEXT, null, null);
        }
    }
    
    private void testSetText_InvalidDayOfWeekText() {
        try {
            ISOChronology.getInstanceUTC().dayOfWeek().set(0, INVALID_DAY_TEXT, java.util.Locale.US);
            fail("Expected IllegalFieldValueException for invalid day of week text");
        } catch (IllegalFieldValueException e) {
            assertExceptionDetails(e, DateTimeFieldType.dayOfWeek(), null, "dayOfWeek",
                                 null, INVALID_DAY_TEXT, INVALID_DAY_TEXT, null, null);
        }
    }
    
    private void testSetText_InvalidHalfdayText() {
        try {
            ISOChronology.getInstanceUTC().halfdayOfDay().set(0, INVALID_HALFDAY_TEXT, java.util.Locale.US);
            fail("Expected IllegalFieldValueException for invalid halfday text");
        } catch (IllegalFieldValueException e) {
            assertExceptionDetails(e, DateTimeFieldType.halfdayOfDay(), null, "halfdayOfDay",
                                 null, INVALID_HALFDAY_TEXT, INVALID_HALFDAY_TEXT, null, null);
        }
    }

    /**
     * Tests that setting an hour during a timezone transition (daylight saving time)
     * throws IllegalFieldValueException when the hour doesn't exist due to the transition.
     */
    public void testZoneTransition() {
        // Create a DateTime during DST transition in Los Angeles (spring forward)
        DateTime dateTimeDuringTransition = new DateTime(
            2005, 4, 3, 1, 0, 0, 0, 
            DateTimeZone.forID("America/Los_Angeles")
        );
        
        int nonExistentHour = 2; // Hour 2 AM doesn't exist during spring forward
        
        try {
            dateTimeDuringTransition.hourOfDay().setCopy(nonExistentHour);
            fail("Expected IllegalFieldValueException for non-existent hour during DST transition");
        } catch (IllegalFieldValueException e) {
            assertExceptionDetails(e, DateTimeFieldType.hourOfDay(), null, "hourOfDay",
                                 nonExistentHour, null, String.valueOf(nonExistentHour),
                                 null, null);
        }
    }

    /**
     * Tests that setting year 0 in Julian chronology throws IllegalFieldValueException
     * because Julian calendar doesn't have a year 0.
     */
    public void testJulianYearZero() {
        DateTime julianDateTime = new DateTime(JulianChronology.getInstanceUTC());
        int invalidJulianYear = 0;
        
        try {
            julianDateTime.year().setCopy(invalidJulianYear);
            fail("Expected IllegalFieldValueException for year 0 in Julian chronology");
        } catch (IllegalFieldValueException e) {
            assertExceptionDetails(e, DateTimeFieldType.year(), null, "year",
                                 invalidJulianYear, null, String.valueOf(invalidJulianYear),
                                 null, null);
        }
    }

    /**
     * Tests that setting dates during the Gregorian/Julian cutover period
     * throws IllegalFieldValueException for non-existent dates.
     */
    public void testGJCutover() {
        testGJCutover_October4th1582();
        testGJCutover_October15th1582();
    }
    
    private void testGJCutover_October4th1582() {
        DateTime cutoverDate = new DateTime("1582-10-04", GJChronology.getInstanceUTC());
        int invalidDay = 5; // October 5th doesn't exist due to calendar cutover
        
        try {
            cutoverDate.dayOfMonth().setCopy(invalidDay);
            fail("Expected IllegalFieldValueException for non-existent day during calendar cutover");
        } catch (IllegalFieldValueException e) {
            assertExceptionDetails(e, DateTimeFieldType.dayOfMonth(), null, "dayOfMonth",
                                 invalidDay, null, String.valueOf(invalidDay), null, null);
        }
    }
    
    private void testGJCutover_October15th1582() {
        DateTime cutoverDate = new DateTime("1582-10-15", GJChronology.getInstanceUTC());
        int invalidDay = 14; // October 14th doesn't exist due to calendar cutover
        
        try {
            cutoverDate.dayOfMonth().setCopy(invalidDay);
            fail("Expected IllegalFieldValueException for non-existent day during calendar cutover");
        } catch (IllegalFieldValueException e) {
            assertExceptionDetails(e, DateTimeFieldType.dayOfMonth(), null, "dayOfMonth",
                                 invalidDay, null, String.valueOf(invalidDay), null, null);
        }
    }

    /**
     * Tests that creating YearMonthDay objects with invalid values
     * throws IllegalFieldValueException with appropriate bounds information.
     */
    @SuppressWarnings("deprecation")
    public void testReadablePartialValidate() {
        testReadablePartialValidate_InvalidMonth_TooLow();
        testReadablePartialValidate_InvalidMonth_TooHigh();
        testReadablePartialValidate_InvalidDayForMonth();
    }
    
    private void testReadablePartialValidate_InvalidMonth_TooLow() {
        int year = 1970;
        int invalidMonth = -5;
        int validDay = 1;
        
        try {
            new YearMonthDay(year, invalidMonth, validDay);
            fail("Expected IllegalFieldValueException for month value too low");
        } catch (IllegalFieldValueException e) {
            assertExceptionDetails(e, DateTimeFieldType.monthOfYear(), null, "monthOfYear",
                                 invalidMonth, null, String.valueOf(invalidMonth),
                                 1, null);
        }
    }
    
    private void testReadablePartialValidate_InvalidMonth_TooHigh() {
        int year = 1970;
        int invalidMonth = 500;
        int validDay = 1;
        
        try {
            new YearMonthDay(year, invalidMonth, validDay);
            fail("Expected IllegalFieldValueException for month value too high");
        } catch (IllegalFieldValueException e) {
            assertExceptionDetails(e, DateTimeFieldType.monthOfYear(), null, "monthOfYear",
                                 invalidMonth, null, String.valueOf(invalidMonth),
                                 null, 12);
        }
    }
    
    private void testReadablePartialValidate_InvalidDayForMonth() {
        int year = 1970;
        int february = 2;
        int invalidDayForFebruary = 30; // February doesn't have 30 days
        
        try {
            new YearMonthDay(year, february, invalidDayForFebruary);
            fail("Expected IllegalFieldValueException for invalid day in February");
        } catch (IllegalFieldValueException e) {
            assertExceptionDetails(e, DateTimeFieldType.dayOfMonth(), null, "dayOfMonth",
                                 invalidDayForFebruary, null, String.valueOf(invalidDayForFebruary),
                                 null, 28);
        }
    }

    /**
     * Tests alternative constructors of IllegalFieldValueException that are not
     * commonly used by the main Joda-Time API but are available for completeness.
     */
    public void testAlternativeConstructors() {
        testConstructor_WithDurationFieldType();
        testConstructor_WithDurationFieldTypeAndStringValue();
        testConstructor_WithStringFieldNameAndStringValue();
    }
    
    private void testConstructor_WithDurationFieldType() {
        Integer illegalValue = 1;
        Integer lowerBound = 2;
        Integer upperBound = 3;
        
        IllegalFieldValueException exception = new IllegalFieldValueException(
            DurationFieldType.days(), illegalValue, lowerBound, upperBound
        );
        
        assertExceptionDetails(exception, null, DurationFieldType.days(), "days",
                             illegalValue, null, String.valueOf(illegalValue),
                             lowerBound, upperBound);
    }
    
    private void testConstructor_WithDurationFieldTypeAndStringValue() {
        String illegalStringValue = "five";
        
        IllegalFieldValueException exception = new IllegalFieldValueException(
            DurationFieldType.months(), illegalStringValue
        );
        
        assertExceptionDetails(exception, null, DurationFieldType.months(), "months",
                             null, illegalStringValue, illegalStringValue, null, null);
    }
    
    private void testConstructor_WithStringFieldNameAndStringValue() {
        String fieldName = "months";
        String illegalStringValue = "five";
        
        IllegalFieldValueException exception = new IllegalFieldValueException(
            fieldName, illegalStringValue
        );
        
        assertExceptionDetails(exception, null, null, fieldName,
                             null, illegalStringValue, illegalStringValue, null, null);
    }
    
    /**
     * Helper method to assert all exception details in a consistent way.
     * This reduces code duplication and makes the tests more maintainable.
     */
    private void assertExceptionDetails(IllegalFieldValueException exception,
                                      DateTimeFieldType expectedDateTimeFieldType,
                                      DurationFieldType expectedDurationFieldType,
                                      String expectedFieldName,
                                      Integer expectedNumberValue,
                                      String expectedStringValue,
                                      String expectedValueAsString,
                                      Integer expectedLowerBound,
                                      Integer expectedUpperBound) {
        assertEquals("DateTimeFieldType mismatch", expectedDateTimeFieldType, exception.getDateTimeFieldType());
        assertEquals("DurationFieldType mismatch", expectedDurationFieldType, exception.getDurationFieldType());
        assertEquals("Field name mismatch", expectedFieldName, exception.getFieldName());
        assertEquals("Number value mismatch", expectedNumberValue, exception.getIllegalNumberValue());
        assertEquals("String value mismatch", expectedStringValue, exception.getIllegalStringValue());
        assertEquals("Value as string mismatch", expectedValueAsString, exception.getIllegalValueAsString());
        assertEquals("Lower bound mismatch", expectedLowerBound, exception.getLowerBound());
        assertEquals("Upper bound mismatch", expectedUpperBound, exception.getUpperBound());
    }
}