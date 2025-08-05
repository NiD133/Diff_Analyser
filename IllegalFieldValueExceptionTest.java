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
 * This test suite verifies that an {@link IllegalFieldValueException} is thrown
 * with the correct state under various invalid conditions.
 *
 * <p>The tests are structured to be self-documenting. Each test method:
 * <ul>
 *   <li>Is named to describe the specific scenario it tests.
 *   <li>Follows an "Arrange, Act, Assert" pattern for clarity.
 *   <li>Tests a single condition to ensure test isolation.
 *   <li>Uses a helper method, {@code assertExceptionDetails}, to reduce boilerplate
 *       and centralize the validation of exception properties.
 * </ul>
 */
public class TestIllegalFieldValueException extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestIllegalFieldValueException.class);
    }

    public TestIllegalFieldValueException(String name) {
        super(name);
    }

    //-----------------------------------------------------------------------
    // Test cases for FieldUtils.verifyValueBounds
    //-----------------------------------------------------------------------

    public void testVerifyValueBounds_whenValueIsBelowLowerBound_throwsException() {
        // Arrange
        final DateTimeField monthOfYearField = ISOChronology.getInstance().monthOfYear();
        final int illegalValue = -5;
        final int lowerBound = 1;
        final int upperBound = 31;

        // Act & Assert
        try {
            FieldUtils.verifyValueBounds(monthOfYearField, illegalValue, lowerBound, upperBound);
            fail("Expected IllegalFieldValueException was not thrown.");
        } catch (IllegalFieldValueException ex) {
            assertExceptionDetails(ex, DateTimeFieldType.monthOfYear(), null, "monthOfYear",
                    illegalValue, null, lowerBound, upperBound);
        }
    }

    public void testVerifyValueBounds_whenValueIsAboveUpperBound_throwsException() {
        // Arrange
        final DateTimeFieldType hourOfDayFieldType = DateTimeFieldType.hourOfDay();
        final int illegalValue = 27;
        final int lowerBound = 0;
        final int upperBound = 23;

        // Act & Assert
        try {
            FieldUtils.verifyValueBounds(hourOfDayFieldType, illegalValue, lowerBound, upperBound);
            fail("Expected IllegalFieldValueException was not thrown.");
        } catch (IllegalFieldValueException ex) {
            assertExceptionDetails(ex, hourOfDayFieldType, null, "hourOfDay",
                    illegalValue, null, lowerBound, upperBound);
        }
    }

    public void testVerifyValueBounds_whenUsingStringFieldName_throwsException() {
        // Arrange
        final String fieldName = "foo";
        final int illegalValue = 1;
        final int lowerBound = 2;
        final int upperBound = 3;

        // Act & Assert
        try {
            FieldUtils.verifyValueBounds(fieldName, illegalValue, lowerBound, upperBound);
            fail("Expected IllegalFieldValueException was not thrown.");
        } catch (IllegalFieldValueException ex) {
            assertExceptionDetails(ex, null, null, fieldName,
                    illegalValue, null, lowerBound, upperBound);
        }
    }

    //-----------------------------------------------------------------------
    // Test cases for specific field behaviors
    //-----------------------------------------------------------------------

    public void testSetOnSkipDateTimeField_whenValueIsSkipped_throwsException() {
        // Arrange
        final int skippedYear = 1970;
        DateTimeField field = new SkipDateTimeField(
                ISOChronology.getInstanceUTC(),
                ISOChronology.getInstanceUTC().year(),
                skippedYear);

        // Act & Assert
        try {
            field.set(0, skippedYear);
            fail("Expected IllegalFieldValueException for skipped value.");
        } catch (IllegalFieldValueException ex) {
            assertExceptionDetails(ex, DateTimeFieldType.year(), null, "year",
                    skippedYear, null, null, null);
        }
    }

    public void testSetText_withInvalidTextForMonth_throwsException() {
        // Arrange
        final DateTimeField monthField = ISOChronology.getInstanceUTC().monthOfYear();
        final String invalidText = "spring";

        // Act & Assert
        try {
            monthField.set(0, invalidText, java.util.Locale.US);
            fail("Expected IllegalFieldValueException for invalid text.");
        } catch (IllegalFieldValueException ex) {
            assertExceptionDetails(ex, DateTimeFieldType.monthOfYear(), null, "monthOfYear",
                    null, invalidText, null, null);
        }
    }

    public void testSetText_withNullText_throwsException() {
        // Arrange
        final DateTimeField yearField = ISOChronology.getInstanceUTC().year();

        // Act & Assert
        try {
            yearField.set(0, null, java.util.Locale.US);
            fail("Expected IllegalFieldValueException for null text.");
        } catch (IllegalFieldValueException ex) {
            assertExceptionDetails(ex, DateTimeFieldType.year(), null, "year",
                    null, null, null, null);
        }
    }

    //-----------------------------------------------------------------------
    // Test cases for chronological edge cases
    //-----------------------------------------------------------------------

    public void testSetHourOfDay_duringZoneTransitionGap_throwsException() {
        // Arrange: In Los Angeles, on 2005-04-03, the clocks jumped from 01:59:59 to 03:00:00.
        // The 2:00 hour does not exist.
        DateTimeZone losAngelesZone = DateTimeZone.forID("America/Los_Angeles");
        DateTime dt = new DateTime(2005, 4, 3, 1, 0, 0, 0, losAngelesZone);
        final int nonExistentHour = 2;

        // Act & Assert
        try {
            dt.hourOfDay().setCopy(nonExistentHour);
            fail("Expected IllegalFieldValueException due to DST gap.");
        } catch (IllegalFieldValueException ex) {
            assertExceptionDetails(ex, DateTimeFieldType.hourOfDay(), null, "hourOfDay",
                    nonExistentHour, null, null, null);
        }
    }

    public void testSetYear_toZeroInJulianChronology_throwsException() {
        // Arrange: Year zero does not exist in the Julian calendar system.
        DateTime dt = new DateTime(JulianChronology.getInstanceUTC());
        final int illegalYear = 0;

        // Act & Assert
        try {
            dt.year().setCopy(illegalYear);
            fail("Expected IllegalFieldValueException for year zero in Julian calendar.");
        } catch (IllegalFieldValueException ex) {
            assertExceptionDetails(ex, DateTimeFieldType.year(), null, "year",
                    illegalYear, null, null, null);
        }
    }

    public void testSetDayOfMonth_toNonExistentDayDuringCutover_throwsException() {
        // Arrange: In the Gregorian calendar cutover from Julian, Oct 5-14, 1582, were skipped.
        DateTime dt = new DateTime("1582-10-04", GJChronology.getInstanceUTC());
        final int nonExistentDay = 5;

        // Act & Assert
        try {
            dt.dayOfMonth().setCopy(nonExistentDay);
            fail("Expected IllegalFieldValueException for non-existent day in GJ cutover.");
        } catch (IllegalFieldValueException ex) {
            assertExceptionDetails(ex, DateTimeFieldType.dayOfMonth(), null, "dayOfMonth",
                    nonExistentDay, null, null, null);
        }
    }

    //-----------------------------------------------------------------------
    // Test cases for ReadablePartial validation
    //-----------------------------------------------------------------------

    @SuppressWarnings("deprecation") // Testing deprecated constructor for backward compatibility
    public void testReadablePartial_whenMonthIsAboveUpperBound_throwsException() {
        // Arrange
        final int invalidMonth = 13;

        // Act & Assert
        try {
            new YearMonthDay(1970, invalidMonth, 1);
            fail("Expected IllegalFieldValueException for month value too high.");
        } catch (IllegalFieldValueException ex) {
            assertExceptionDetails(ex, DateTimeFieldType.monthOfYear(), null, "monthOfYear",
                    invalidMonth, null, null, 12);
        }
    }

    @SuppressWarnings("deprecation") // Testing deprecated constructor for backward compatibility
    public void testReadablePartial_whenDayIsInvalidForMonth_throwsException() {
        // Arrange: February 1970 had 28 days.
        final int invalidDay = 30;

        // Act & Assert
        try {
            new YearMonthDay(1970, 2, invalidDay);
            fail("Expected IllegalFieldValueException for invalid day of month.");
        } catch (IllegalFieldValueException ex) {
            assertExceptionDetails(ex, DateTimeFieldType.dayOfMonth(), null, "dayOfMonth",
                    invalidDay, null, null, 28);
        }
    }

    //-----------------------------------------------------------------------
    // Test cases for direct exception construction
    //-----------------------------------------------------------------------

    public void testExceptionConstructors_notTriggeredByApi() {
        // This test validates constructors not directly called by the methods under test.

        // Arrange & Act: Constructor with DurationFieldType and Number
        IllegalFieldValueException ex1 = new IllegalFieldValueException(
                DurationFieldType.days(), 1, 2, 3);
        // Assert
        assertExceptionDetails(ex1, null, DurationFieldType.days(), "days", 1, null, 2, 3);

        // Arrange & Act: Constructor with DurationFieldType and String
        IllegalFieldValueException ex2 = new IllegalFieldValueException(
                DurationFieldType.months(), "five");
        // Assert
        assertExceptionDetails(ex2, null, DurationFieldType.months(), "months", null, "five", null, null);

        // Arrange & Act: Constructor with String field name and String value
        IllegalFieldValueException ex3 = new IllegalFieldValueException("months", "five");
        // Assert
        assertExceptionDetails(ex3, null, null, "months", null, "five", null, null);
    }

    //-----------------------------------------------------------------------
    // Helper Methods
    //-----------------------------------------------------------------------

    /**
     * Asserts that the properties of a caught {@link IllegalFieldValueException}
     * match the expected values.
     */
    private void assertExceptionDetails(
            IllegalFieldValueException ex,
            DateTimeFieldType expectedDateTimeFieldType,
            DurationFieldType expectedDurationFieldType,
            String expectedFieldName,
            Number expectedIllegalNumber,
            String expectedIllegalString,
            Number expectedLowerBound,
            Number expectedUpperBound) {

        assertEquals("DateTimeFieldType mismatch", expectedDateTimeFieldType, ex.getDateTimeFieldType());
        assertEquals("DurationFieldType mismatch", expectedDurationFieldType, ex.getDurationFieldType());
        assertEquals("FieldName mismatch", expectedFieldName, ex.getFieldName());
        assertEquals("IllegalNumberValue mismatch", expectedIllegalNumber, ex.getIllegalNumberValue());
        assertEquals("IllegalStringValue mismatch", expectedIllegalString, ex.getIllegalStringValue());
        assertEquals("LowerBound mismatch", expectedLowerBound, ex.getLowerBound());
        assertEquals("UpperBound mismatch", expectedUpperBound, ex.getUpperBound());

        // Also check the string representation of the illegal value
        String expectedValueString = (expectedIllegalNumber != null)
                ? expectedIllegalNumber.toString()
                : String.valueOf(expectedIllegalString);
        assertEquals("IllegalValueAsString mismatch", expectedValueString, ex.getIllegalValueAsString());
    }
}