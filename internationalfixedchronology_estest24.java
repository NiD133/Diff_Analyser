package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link InternationalFixedChronology}.
 * This class focuses on edge cases and exception handling.
 */
public class InternationalFixedChronologyTest {

    /**
     * Tests that creating a ZonedDateTime from an Instant that is outside the
     * chronology's maximum supported year throws a DateTimeException.
     * The InternationalFixedChronology supports years from 1 to 1,000,000.
     */
    @Test
    public void zonedDateTime_whenInstantIsBeyondMaxSupportedYear_throwsDateTimeException() {
        // Arrange
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        ZoneId utc = ZoneOffset.UTC;

        // The maximum supported year is 1,000,000.
        // Create an Instant corresponding to a year just outside this range (1,000,001)
        // to test the upper boundary.
        Instant outOfRangeInstant = ZonedDateTime.of(1_000_001, 1, 1, 0, 0, 0, 0, utc).toInstant();

        // Act & Assert
        DateTimeException exception = assertThrows(
                DateTimeException.class,
                () -> chronology.zonedDateTime(outOfRangeInstant, utc)
        );

        // Verify that the exception message indicates an out-of-range value.
        // This confirms the exception was thrown for the correct reason.
        assertTrue(exception.getMessage().contains("EpochDay"));
    }
}