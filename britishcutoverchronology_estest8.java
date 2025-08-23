package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.ChronoField;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test suite for {@link BritishCutoverChronology}.
 */
public class BritishCutoverChronologyTest {

    /**
     * Tests the creation of a date from a year and day-of-year for a date
     * that falls before the Julian-to-Gregorian cutover.
     */
    @Test
    public void dateYearDay_forJulianDate_createsCorrectDate() {
        // Arrange
        // The year 103 is well before the 1752 cutover, so it follows the Julian calendar rules.
        // In the Julian calendar, 103 is not a leap year.
        // The 103rd day of a non-leap year is April 13th.
        // (Jan 31 + Feb 28 + Mar 31 = 90; 103 - 90 = 13)
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        int prolepticYear = 103;
        int dayOfYear = 103;

        // Act
        BritishCutoverDate actualDate = chronology.dateYearDay(prolepticYear, dayOfYear);

        // Assert
        assertNotNull("The created date should not be null.", actualDate);

        // Verify the components of the created date for correctness
        assertEquals("Year should match the input proleptic year",
                prolepticYear, actualDate.get(ChronoField.YEAR));
        assertEquals("Month should be April (4)",
                4, actualDate.get(ChronoField.MONTH_OF_YEAR));
        assertEquals("Day of month should be 13",
                13, actualDate.get(ChronoField.DAY_OF_MONTH));
        assertEquals("Day of year should match the input day-of-year",
                dayOfYear, actualDate.get(ChronoField.DAY_OF_YEAR));
    }
}