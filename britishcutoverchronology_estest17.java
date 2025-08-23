package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for {@link BritishCutoverChronology}.
 * This class focuses on edge cases and exception handling.
 */
// The original test was part of an auto-generated suite (e.g., BritishCutoverChronology_ESTestTest17).
// It has been renamed for clarity and to follow standard Java conventions.
public class BritishCutoverChronologyTest {

    /**
     * Tests that calling `localDateTime()` with a TemporalAccessor that only contains date
     * information (like a LocalDate) throws a DateTimeException.
     *
     * The `localDateTime()` method requires time-of-day information to construct a
     * `ChronoLocalDateTime`, which a `LocalDate` does not provide.
     */
    @Test
    public void localDateTime_withDateOnlyTemporal_throwsDateTimeException() {
        // Arrange
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        // Use the CUTOVER date as a representative LocalDate instance. A LocalDate is a
        // TemporalAccessor that lacks the necessary time-of-day fields.
        TemporalAccessor dateOnlyTemporal = BritishCutoverChronology.CUTOVER;

        // Act & Assert
        try {
            chronology.localDateTime(dateOnlyTemporal);
            fail("Expected a DateTimeException to be thrown, but no exception occurred.");
        } catch (DateTimeException e) {
            // This is the expected behavior.
            // For robustness, we can verify the exception message confirms the cause.
            String expectedMessagePart = "Unable to obtain ChronoLocalDateTime from TemporalAccessor";
            assertTrue(
                "Exception message should indicate failure to create LocalDateTime. Actual: " + e.getMessage(),
                e.getMessage().contains(expectedMessagePart)
            );
        }
    }
}