package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for {@link JulianChronology}.
 * This class focuses on edge cases related to date creation.
 */
public class JulianChronology_ESTestTest21 extends JulianChronology_ESTest_scaffolding {

    /**
     * Tests that dateNow() throws a DateTimeException when the clock is set to a date
     * that is beyond the maximum supported year of the Julian calendar.
     */
    @Test
    public void dateNow_whenClockRepresentsDateBeyondMaxSupportedYear_throwsDateTimeException() {
        // Arrange
        // Use the singleton instance of JulianChronology, which is the recommended practice.
        final JulianChronology julianChronology = JulianChronology.INSTANCE;
        final ZoneId utc = ZoneOffset.UTC;

        // The JulianChronology's valid year range is defined from -999,998 to 999,999.
        // We need to create a clock that is fixed to a date outside this range.
        // Let's use the year 1,000,000, which is just beyond the maximum.
        // We construct this date using the standard ISO calendar (LocalDateTime) because
        // attempting to create a JulianDate directly with this year would also fail.
        Instant instantBeyondMaxYear = LocalDateTime.of(1_000_000, Month.JANUARY, 1, 0, 0)
                                                   .toInstant(utc);
        Clock clockFixedAtFutureDate = Clock.fixed(instantBeyondMaxYear, utc);

        // Act & Assert
        try {
            julianChronology.dateNow(clockFixedAtFutureDate);
            fail("Expected a DateTimeException to be thrown, but it wasn't.");
        } catch (DateTimeException e) {
            // The test passes if a DateTimeException is caught.
            // For completeness, we verify the exception message confirms the cause.
            String expectedMessageContent = "Invalid value for Year";
            assertTrue(
                "Exception message should indicate an invalid year. Actual message: " + e.getMessage(),
                e.getMessage().contains(expectedMessageContent)
            );
        }
    }
}