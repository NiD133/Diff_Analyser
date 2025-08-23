package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTime.Property;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationFieldType;
import org.junit.Test;

/**
 * Tests the properties and fields of the IslamicChronology.
 * This test focuses on verifying the field values for a specific date
 * after converting it from the ISO calendar to the Islamic calendar.
 */
public class IslamicChronologyFieldsTest {

    private static final Chronology ISLAMIC_UTC = IslamicChronology.getInstanceUTC();
    private static final Chronology ISO_UTC = ISOChronology.getInstanceUTC();

    @Test
    public void testFields_forGregorianDate_2005_11_26() {
        // ARRANGE: Define the test date in the ISO calendar and the expected Islamic date values.
        DateTime gregorianDate = new DateTime(2005, 11, 26, 0, 0, 0, 0, ISO_UTC);

        // Expected Islamic date: 1426-10-24 AH (Shawwal 24, 1426 AH)
        final int expectedEra = IslamicChronology.AH;
        final int expectedCenturyOfEra = 15; // (1426 - 1) / 100 + 1
        final int expectedYearOfCentury = 26;  // (1426 - 1) % 100 + 1
        final int expectedYear = 1426;
        final int expectedMonth = 10; // Shawwal
        final int expectedDay = 24;
        final int expectedDayOfWeek = DateTimeConstants.SATURDAY;
        // Day of year is the sum of days in preceding months plus the day in the current month.
        // The first 9 months of a leap year (like 1426) have 5 long (30-day) and 4 short (29-day) months.
        // (5 * 30) + (4 * 29) + 24 = 150 + 116 + 24 = 290.
        final int expectedDayOfYear = 290;

        // ACT: Convert the date to the Islamic chronology.
        DateTime islamicDate = gregorianDate.withChronology(ISLAMIC_UTC);

        // ASSERT: Verify the main date components.
        assertEquals("Era", expectedEra, islamicDate.getEra());
        assertEquals("Century of Era", expectedCenturyOfEra, islamicDate.getCenturyOfEra());
        assertEquals("Year of Century", expectedYearOfCentury, islamicDate.getYearOfCentury());
        assertEquals("Year of Era", expectedYear, islamicDate.getYearOfEra());
        assertEquals("Year", expectedYear, islamicDate.getYear());
        assertEquals("Month of Year", expectedMonth, islamicDate.getMonthOfYear());
        assertEquals("Day of Month", expectedDay, islamicDate.getDayOfMonth());
        assertEquals("Day of Week", expectedDayOfWeek, islamicDate.getDayOfWeek());
        assertEquals("Day of Year", expectedDayOfYear, islamicDate.getDayOfYear());

        // ASSERT: Verify properties of the 'year' field for 1426 AH.
        Property yearField = islamicDate.year();
        assertTrue("Year 1426 should be a leap year", yearField.isLeap());
        assertEquals("Leap amount for year", 1, yearField.getLeapAmount());
        assertEquals("Leap duration field for year", DurationFieldType.days(), yearField.getLeapDurationField().getType());

        // ASSERT: Verify properties of the 'monthOfYear' field (for Shawwal).
        Property monthField = islamicDate.monthOfYear();
        assertFalse("Month should not be a leap month", monthField.isLeap());
        assertEquals("Leap amount for month", 0, monthField.getLeapAmount());
        assertEquals("Leap duration field for month", DurationFieldType.days(), monthField.getLeapDurationField().getType());
        assertEquals("Min value for month", 1, monthField.getMinimumValue());
        assertEquals("Max value for month", 12, monthField.getMaximumValue());

        // ASSERT: Verify properties of the 'dayOfMonth' field.
        Property dayOfMonthField = islamicDate.dayOfMonth();
        assertFalse("Day should not be a leap day", dayOfMonthField.isLeap());
        assertEquals("Leap amount for day", 0, dayOfMonthField.getLeapAmount());
        assertNull("Leap duration field for day", dayOfMonthField.getLeapDurationField());
        assertEquals("Min value for day of month", 1, dayOfMonthField.getMinimumValue());
        assertEquals("Max value for this day of month (Shawwal has 29 days)", 29, dayOfMonthField.getMaximumValue());
        assertEquals("Max value overall for day of month", 30, dayOfMonthField.getMaximumValueOverall());

        // ASSERT: Verify properties of the 'dayOfWeek' field.
        Property dayOfWeekField = islamicDate.dayOfWeek();
        assertEquals("Min value for day of week", 1, dayOfWeekField.getMinimumValue());
        assertEquals("Max value for day of week", 7, dayOfWeekField.getMaximumValue());

        // ASSERT: Verify properties of the 'dayOfYear' field for a leap year.
        Property dayOfYearField = islamicDate.dayOfYear();
        assertEquals("Min value for day of year", 1, dayOfYearField.getMinimumValue());
        assertEquals("Max value for day of year (leap year)", 355, dayOfYearField.getMaximumValue());
        assertEquals("Max value overall for day of year", 355, dayOfYearField.getMaximumValueOverall());

        // ASSERT: Verify time components are unchanged.
        assertEquals("Hour of Day", 0, islamicDate.getHourOfDay());
        assertEquals("Minute of Hour", 0, islamicDate.getMinuteOfHour());
        assertEquals("Second of Minute", 0, islamicDate.getSecondOfMinute());
        assertEquals("Millis of Second", 0, islamicDate.getMillisOfSecond());
    }
}