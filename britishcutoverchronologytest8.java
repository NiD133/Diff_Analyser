package org.threeten.extra.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.Month;
import java.time.temporal.ChronoField;
import org.junit.Test;

/**
 * A test suite for the BritishCutoverChronology, focusing on date creation.
 */
public class BritishCutoverChronologyTest {

    /**
     * Tests that creating a date from a year and day-of-year works correctly
     * for a date that falls within the Julian calendar period (before the 1752 cutover).
     *
     * <p>The 103rd day of the year 103 corresponds to April 13, 103. This test
     * verifies that the date is constructed with the correct year, month, and day.
     */
    @Test
    public void dateYearDay_shouldCreateCorrectDate_forJulianYear() {
        // Arrange
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        int prolepticYear = 103;
        int dayOfYear = 103;

        // Act
        BritishCutoverDate createdDate = chronology.dateYearDay(prolepticYear, dayOfYear);

        // Assert
        assertNotNull("The created date should not be null.", createdDate);
        assertEquals("The proleptic year should match the input.", prolepticYear, createdDate.get(ChronoField.YEAR));
        assertEquals("The month should be correctly calculated from the day-of-year.", Month.APRIL, createdDate.getMonth());
        assertEquals("The day of the month should be correctly calculated.", 13, createdDate.getDayOfMonth());
    }
}