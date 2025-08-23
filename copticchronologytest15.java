package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

/**
 * Tests for CopticChronology focusing on conversions involving different time zones.
 */
public class CopticChronologyTimeZoneTest {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final Chronology COPTIC_UTC = CopticChronology.getInstanceUTC();

    @Test
    public void testIsoDateTimeInParisZoneConvertsCorrectlyToCopticUTC() {
        // Arrange: Define an ISO date and time in a specific time zone (Paris).
        // On June 9, 2004, Paris was in Daylight Saving Time, making it UTC+2.
        DateTime isoDateTimeInParis = new DateTime(2004, 6, 9, 12, 0, 0, 0, PARIS);

        // Act: Convert this instant to the Coptic calendar, expressed in the UTC time zone.
        // The underlying instant in time remains the same, but its fields (year, month, hour, etc.)
        // are recalculated based on the new chronology and time zone.
        DateTime copticDateTimeInUTC = isoDateTimeInParis.withChronology(COPTIC_UTC);

        // Assert: Verify that the date and time fields match the expected Coptic values in UTC.
        
        // The Gregorian date 2004-06-09 converts to the Coptic date 1720-10-02.
        assertEquals("Era should be AM", CopticChronology.AM, copticDateTimeInUTC.getEra());
        assertEquals("Year of Era should be 1720", 1720, copticDateTimeInUTC.getYearOfEra());
        assertEquals("Year should be 1720", 1720, copticDateTimeInUTC.getYear());
        assertEquals("Month should be 10", 10, copticDateTimeInUTC.getMonthOfYear());
        assertEquals("Day should be 2", 2, copticDateTimeInUTC.getDayOfMonth());

        // The time must be adjusted from Paris time (UTC+2) to UTC.
        // Therefore, 12:00 in Paris becomes 10:00 in UTC.
        assertEquals("Hour should be 10", 10, copticDateTimeInUTC.getHourOfDay());
        assertEquals("Minute should be 0", 0, copticDateTimeInUTC.getMinuteOfHour());
        assertEquals("Second should be 0", 0, copticDateTimeInUTC.getSecondOfMinute());
        assertEquals("Millis should be 0", 0, copticDateTimeInUTC.getMillisOfSecond());
    }
}