package org.threeten.extra.chrono;

import org.junit.jupiter.api.Test;
import java.time.DateTimeException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for the {@link Symmetry010Chronology} class, focusing on date creation logic.
 */
class Symmetry010ChronologyTest {

    /**
     * Tests that creating a date with a day-of-year of 371 fails for a non-leap year.
     * In the Symmetry010 calendar, a normal year has 364 days, while a leap year has 371.
     * The year 371 is not a leap year, so requesting the 371st day should be invalid.
     */
    @Test
    void dateYearDay_throwsExceptionForDay371InNonLeapYear() {
        // Arrange
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        int nonLeapYear = 371;
        int invalidDayOfYearForNonLeapYear = 371;

        // Act & Assert
        DateTimeException thrownException = assertThrows(
            DateTimeException.class,
            () -> chronology.dateYearDay(nonLeapYear, invalidDayOfYearForNonLeapYear),
            "Should throw DateTimeException for a day-of-year that only exists in a leap year."
        );

        // Verify the exception message for correctness
        assertEquals("Invalid date 'DayOfYear 371' as '371' is not a leap year", thrownException.getMessage());
    }
}