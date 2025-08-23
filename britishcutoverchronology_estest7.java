package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.chrono.Era;
import java.time.temporal.ChronoField;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for {@link BritishCutoverChronology}.
 */
public class BritishCutoverChronologyTest {

    /**
     * Tests the creation of a date from an era, year-of-era, and day-of-year.
     * This test case uses a date from the B.C. era, which falls before the
     * Gregorian cutover and should be handled by the Julian calendar rules.
     */
    @Test
    public void shouldCreateCorrectDateInBCEraFromDayOfYear() {
        // Arrange
        // The 5th year of the B.C. era corresponds to the proleptic year -4.
        // The 5th day of that year is January 5th.
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        Era eraBC = JulianEra.BC;
        int yearOfEra = 5;
        int dayOfYear = 5;

        int expectedProlepticYear = -4; // In Julian calendar, proleptic year for B.C. is 1 - yearOfEra
        int expectedMonth = 1;          // January
        int expectedDayOfMonth = 5;

        // Act
        BritishCutoverDate date = chronology.dateYearDay(eraBC, yearOfEra, dayOfYear);

        // Assert
        assertNotNull("The created date should not be null.", date);

        assertEquals("Era should be B.C.", eraBC, date.getEra());
        assertEquals("Year of era should match the input.", yearOfEra, date.get(ChronoField.YEAR_OF_ERA));
        assertEquals("Proleptic year should be correctly calculated for a B.C. date.", expectedProlepticYear, date.get(ChronoField.PROLEPTIC_YEAR));
        assertEquals("Month should be January for the 5th day of the year.", expectedMonth, date.get(ChronoField.MONTH_OF_YEAR));
        assertEquals("Day of month should be the 5th.", expectedDayOfMonth, date.get(ChronoField.DAY_OF_MONTH));
        assertEquals("Day of year should match the input.", dayOfYear, date.get(ChronoField.DAY_OF_YEAR));
    }
}