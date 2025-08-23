package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

/**
 * Tests the construction of a DateTime object using the IslamicChronology.
 */
public class IslamicChronologyTest {

    /**
     * This test verifies that creating a DateTime with specific year, month, and day fields
     * in the Islamic calendar results in the correct moment in time, by comparing it to the
     * equivalent, known date in the standard ISO calendar.
     */
    @Test
    public void dateTimeConstructor_createsCorrectInstantFromIslamicDate() {
        // Arrange: Define an Islamic date and its known equivalent in the ISO (Gregorian) calendar.
        // The date is 6 Dhu al-Hijjah 1364 AH, which corresponds to 12 November 1945 CE.
        Chronology islamicChronology = IslamicChronology.getInstanceUTC();
        int islamicYear = 1364;
        int islamicMonth = 12; // Dhu al-Hijjah
        int islamicDay = 6;

        DateTime expectedIsoDate = new DateTime(1945, 11, 12, 0, 0, 0, 0, ISOChronology.getInstanceUTC());

        // Act: Create a DateTime object using the Islamic calendar fields.
        DateTime islamicDate = new DateTime(islamicYear, islamicMonth, islamicDay, 0, 0, 0, 0, islamicChronology);

        // Assert: The instant in time for the Islamic date should be identical to the ISO date.
        assertEquals(expectedIsoDate, islamicDate);
    }
}