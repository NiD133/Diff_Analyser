package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.DateTimeException;
import java.time.chrono.Era;
import java.time.chrono.IsoEra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the Symmetry454Chronology class.
 * This class replaces the auto-generated Symmetry454Chronology_ESTestTest56.
 */
public class Symmetry454ChronologyTest {

    /**
     * Tests that creating a date with a day-of-year only valid in a leap year
     * throws an exception if the year is not a leap year.
     */
    @Test
    public void dateYearDay_whenDayIs371ForNonLeapYear_throwsDateTimeException() {
        // Arrange
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;

        // In the Symmetry454 calendar, a normal year has 364 days and a leap year has 371.
        // Day 371 is therefore invalid for a non-leap year.
        // The year 1145 CE is not a leap year in this chronology.
        int nonLeapYear = 1145;
        int dayInLeapYearOnly = 371;
        Era era = IsoEra.CE;

        // Act & Assert
        try {
            chronology.dateYearDay(era, nonLeapYear, dayInLeapYearOnly);
            fail("Expected a DateTimeException to be thrown, but it was not.");
        } catch (DateTimeException e) {
            // Verify that the exception message correctly identifies the reason for failure.
            String expectedMessage = "Invalid date 'DayOfYear 371' as '1145' is not a leap year";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}