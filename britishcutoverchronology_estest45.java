package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.chrono.Era;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link BritishCutoverChronology} class, focusing on date creation.
 */
public class BritishCutoverChronologyTest {

    /**
     * Tests that the chronology can create a date from an era, year, month, and day.
     * This test uses a date that falls within the Julian calendar period (before 1752).
     */
    @Test
    public void dateWithEra_forEarlyJulianDate_createsCorrectDate() {
        // Arrange
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        Era adEra = JulianEra.AD;
        int year = 5;
        int month = 5;
        int day = 5;
        
        // The expected date is May 5, 5 AD, which falls under the Julian calendar rules.
        BritishCutoverDate expectedDate = BritishCutoverDate.of(year, month, day);

        // Act
        // Create a date using the era, year-of-era, month, and day.
        BritishCutoverDate actualDate = chronology.date(adEra, year, month, day);

        // Assert
        // Verify that the created date has the correct value.
        assertEquals("The created date should be equal to the expected date.", expectedDate, actualDate);
        assertEquals("Era should be AD.", adEra, actualDate.getEra());
        assertEquals("Year should match.", year, actualDate.getYear());
    }
}