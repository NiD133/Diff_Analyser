package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

/**
 * Tests the conversion of a DateTime from the ISO chronology to the Ethiopic chronology,
 * particularly when time zones are involved.
 */
public class EthiopicChronologyTest {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final Chronology ETHIOPIC_UTC = EthiopicChronology.getInstanceUTC();

    @Test
    public void withChronology_shouldConvertFromISOToEthiopicAndAdjustForTimeZone() {
        // This test verifies that converting a DateTime from ISO to Ethiopic chronology
        // correctly handles both the calendar date conversion and time zone adjustment.

        // ARRANGE:
        // Create a DateTime in the ISO calendar and a non-UTC time zone (Paris).
        // In June 2004, Paris was in Daylight Saving Time (UTC+2).
        DateTime isoDateTimeInParis = new DateTime(2004, 6, 9, 12, 0, 0, 0, PARIS);

        // Define the expected outcome after converting to Ethiopic calendar in UTC.
        // The absolute instant in time (UTC millis) must be preserved.
        // - Date: ISO 2004-06-09 converts to Ethiopic 1996-10-02.
        // - Time: 12:00 in Paris (UTC+2) is 10:00 in UTC.
        final int expectedEthiopicYear = 1996;
        final int expectedEthiopicMonth = 10; // Corresponds to the Ethiopic month 'Sene'
        final int expectedEthiopicDay = 2;
        final int expectedHourInUTC = 10;

        // ACT:
        // Convert the DateTime to the Ethiopic chronology, with the time zone set to UTC.
        DateTime ethiopicDateTimeUTC = isoDateTimeInParis.withChronology(ETHIOPIC_UTC);

        // ASSERT:
        // Verify that all fields of the new DateTime match the expected Ethiopic values.
        assertEquals("Era", EthiopicChronology.EE, ethiopicDateTimeUTC.getEra());
        assertEquals("Year", expectedEthiopicYear, ethiopicDateTimeUTC.getYear());
        assertEquals("Year of Era", expectedEthiopicYear, ethiopicDateTimeUTC.getYearOfEra());
        assertEquals("Month of Year", expectedEthiopicMonth, ethiopicDateTimeUTC.getMonthOfYear());
        assertEquals("Day of Month", expectedEthiopicDay, ethiopicDateTimeUTC.getDayOfMonth());

        assertEquals("Hour of Day (adjusted to UTC)", expectedHourInUTC, ethiopicDateTimeUTC.getHourOfDay());
        assertEquals("Minute of Hour", 0, ethiopicDateTimeUTC.getMinuteOfHour());
        assertEquals("Second of Minute", 0, ethiopicDateTimeUTC.getSecondOfMinute());
        assertEquals("Millis of Second", 0, ethiopicDateTimeUTC.getMillisOfSecond());
    }
}