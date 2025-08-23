package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

/**
 * Tests the conversion of a Gregorian DateTime with a specific time zone
 * to the IslamicChronology in UTC.
 */
public class IslamicChronologyWithZoneTest {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final Chronology ISLAMIC_UTC = IslamicChronology.getInstanceUTC();

    @Test
    public void gregorianToIslamic_whenConvertingDateTimeWithZone_thenZoneIsHandledCorrectly() {
        // Arrange: A Gregorian date in the Paris time zone.
        // On 2005-11-26, Paris is in CET (UTC+1).
        // So, 12:00 in Paris corresponds to 11:00 UTC.
        DateTime gregorianInParis = new DateTime(2005, 11, 26, 12, 0, 0, 0, PARIS);

        // Act: Convert the DateTime object to the Islamic calendar in UTC.
        DateTime islamicInUTC = gregorianInParis.withChronology(ISLAMIC_UTC);

        // Assert: The resulting date and time should correspond to the
        //         Islamic calendar representation of the original UTC instant.
        // Expected Islamic Date: 1426-10-24
        assertEquals("Era should be Anno Hegirae (AH)", IslamicChronology.AH, islamicInUTC.getEra());
        assertEquals("Year should be 1426", 1426, islamicInUTC.getYear());
        assertEquals("Month should be 10", 10, islamicInUTC.getMonthOfYear());
        assertEquals("Day should be 24", 24, islamicInUTC.getDayOfMonth());

        // Assert time components are correct after UTC conversion
        assertEquals("Hour should be 11 (12:00 Paris -> 11:00 UTC)", 11, islamicInUTC.getHourOfDay());
        assertEquals("Minute should be 0", 0, islamicInUTC.getMinuteOfHour());
        assertEquals("Second should be 0", 0, islamicInUTC.getSecondOfMinute());
        assertEquals("Millis should be 0", 0, islamicInUTC.getMillisOfSecond());
    }
}