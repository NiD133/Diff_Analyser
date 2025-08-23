package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

/**
 * Tests the epoch of the IslamicChronology.
 * The Islamic calendar epoch (Year 1, Month 1, Day 1) corresponds to
 * July 16, 622 CE in the proleptic Julian calendar.
 */
public class IslamicChronologyEpochTest {

    private static final Chronology ISLAMIC_UTC = IslamicChronology.getInstanceUTC();
    private static final Chronology JULIAN_UTC = JulianChronology.getInstanceUTC();

    @Test
    public void testEpoch_correspondsToJulianCalendarEpoch() {
        // The Islamic calendar epoch is defined as the start of Year 1, Month 1, Day 1.
        final DateTime islamicEpoch = new DateTime(1, 1, 1, 0, 0, 0, 0, ISLAMIC_UTC);

        // This date corresponds to July 16, 622 CE in the Julian calendar, which marks the Hijra.
        final DateTime expectedJulianDate = new DateTime(622, 7, 16, 0, 0, 0, 0, JULIAN_UTC);

        // Verify that the millisecond representation of both dates is identical.
        assertEquals("The Islamic epoch millis should match the corresponding Julian date millis",
                expectedJulianDate.getMillis(), islamicEpoch.getMillis());
    }
}