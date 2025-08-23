package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.DateTimeException;
import java.time.chrono.Era;
import java.time.chrono.IsoEra;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link Symmetry454Chronology} class, focusing on date creation.
 */
public class Symmetry454ChronologyTest {

    /**
     * Verifies that dateYearDay() throws a DateTimeException when provided with an invalid
     * day-of-year value. The day-of-year must be a positive integer within the valid
     * range for the calendar system (1 to 364 or 371).
     */
    @Test
    public void dateYearDay_withNegativeDayOfYear_throwsDateTimeException() {
        // Arrange: Set up the chronology and input parameters.
        // A negative day-of-year is always invalid.
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;
        Era era = IsoEra.BCE;
        int yearOfEra = 2023; // A valid year-of-era.
        int invalidDayOfYear = -313;

        // Act & Assert: Attempt to create the date and verify that the correct exception is thrown.
        try {
            chronology.dateYearDay(era, yearOfEra, invalidDayOfYear);
            fail("Expected a DateTimeException to be thrown for an invalid day-of-year.");
        } catch (DateTimeException e) {
            // The exception is expected. We check the message to ensure it's informative.
            String expectedMessageContent = "Invalid value for DayOfYear";
            assertTrue(
                "Exception message should explain that the day-of-year is invalid.",
                e.getMessage().contains(expectedMessageContent)
            );
        }
    }
}