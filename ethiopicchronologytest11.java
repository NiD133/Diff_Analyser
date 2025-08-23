package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

/**
 * Tests for the epoch definition in EthiopicChronology.
 */
public class EthiopicChronologyTest {

    private static final Chronology ETHIOPIC_UTC = EthiopicChronology.getInstanceUTC();
    private static final Chronology JULIAN_UTC = JulianChronology.getInstanceUTC();

    /**
     * The Ethiopic calendar defines its epoch (Year 1, Month 1, Day 1) as
     * equivalent to August 29, 8 CE in the Julian calendar. This test
     * verifies that conversion is handled correctly.
     */
    @Test
    public void testEpoch_correspondsToCorrectJulianDate() {
        // Arrange: Define the start of the Ethiopic calendar.
        DateTime ethiopicEpoch = new DateTime(1, 1, 1, 0, 0, 0, 0, ETHIOPIC_UTC);

        // Arrange: Define the expected equivalent date in the Julian calendar.
        DateTime expectedJulianDate = new DateTime(8, 8, 29, 0, 0, 0, 0, JULIAN_UTC);

        // Act: Convert the Ethiopic epoch to the Julian chronology.
        DateTime actualInJulian = ethiopicEpoch.withChronology(JULIAN_UTC);

        // Assert: The converted date should match the expected Julian date.
        assertEquals(expectedJulianDate, actualInJulian);
    }
}