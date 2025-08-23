package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.DateTimeException;

/**
 * Tests for {@link BritishCutoverChronology}.
 */
public class BritishCutoverChronologyTest {

    /**
     * Tests that creating a date with an invalid day-of-year throws an exception.
     * The day-of-year must be a positive number within the valid range for the year.
     */
    @Test(expected = DateTimeException.class)
    public void dateYearDay_withNegativeDayOfYear_throwsException() {
        // Arrange
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        JulianEra era = JulianEra.BC;
        int year = 1778; // A valid year after the cutover
        int invalidDayOfYear = -5738;

        // Act
        // This call is expected to throw DateTimeException due to the invalid day-of-year.
        chronology.dateYearDay(era, year, invalidDayOfYear);

        // Assert: The exception is verified by the @Test(expected=...) annotation.
    }
}