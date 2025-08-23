package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

/**
 * Tests the epoch of the CopticChronology.
 * The Coptic epoch is the first day of the first year in its calendar.
 */
public class CopticChronologyEpochTest {

    private static final Chronology COPTIC_UTC = CopticChronology.getInstanceUTC();
    private static final Chronology JULIAN_UTC = JulianChronology.getInstanceUTC();

    @Test
    public void copticEpoch_shouldCorrespondToCorrectJulianDate() {
        // The Coptic calendar epoch is defined as 1/1/1 AM (Anno Martyrum).
        // This test verifies that this date corresponds to August 29, 284 in the
        // proleptic Julian calendar, as per historical definitions.

        // Arrange: Define the Coptic epoch and the expected equivalent Julian date.
        DateTime copticEpoch = new DateTime(1, 1, 1, 0, 0, 0, 0, COPTIC_UTC);
        DateTime expectedJulianDate = new DateTime(284, 8, 29, 0, 0, 0, 0, JULIAN_UTC);

        // Act: Convert the Coptic epoch to the Julian chronology.
        DateTime actualJulianDate = copticEpoch.withChronology(JULIAN_UTC);

        // Assert: The converted date should match the expected Julian date.
        assertEquals(expectedJulianDate, actualJulianDate);
    }
}