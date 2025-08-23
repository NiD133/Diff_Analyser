package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.DateTimeException;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link Symmetry454Chronology}.
 */
public class Symmetry454ChronologyTest {

    /**
     * Tests that dateYearDay() throws a DateTimeException for a day-of-year value
     * that is less than 1.
     */
    @Test
    public void dateYearDay_whenDayOfYearIsNegative_throwsDateTimeException() {
        // Arrange
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;
        int year = 2023;
        int invalidDayOfYear = -1;

        // Act & Assert
        DateTimeException thrown = assertThrows(
            "Calling dateYearDay() with a negative day of year should throw DateTimeException.",
            DateTimeException.class,
            () -> chronology.dateYearDay(year, invalidDayOfYear)
        );

        // Verify the exception message for more precise feedback
        assertTrue(
            "The exception message should explain that the day-of-year is invalid.",
            thrown.getMessage().contains("Invalid value for DayOfYear")
        );
    }
}