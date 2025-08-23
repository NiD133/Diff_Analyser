package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * This test class contains an improved version of the original generated test case.
 * The original class name is kept for context, but scaffolding and EvoRunner dependencies are removed.
 */
public class Symmetry010Chronology_ESTestTest25 {

    /**
     * Verifies that dateNow() throws a DateTimeException when the provided Clock
     * represents a date beyond the maximum supported by the Symmetry010Chronology.
     */
    @Test
    public void dateNow_whenClockIsBeyondMaximumDate_throwsDateTimeException() {
        // Arrange: Set up the test scenario.
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;

        // Determine the maximum supported epoch day from the chronology's defined range.
        // This avoids using magic numbers and makes the test robust against future changes.
        long maxSupportedEpochDay = chronology.range(ChronoField.EPOCH_DAY).getMaximum();

        // Create a clock fixed to an instant that is exactly one day beyond the maximum supported date.
        // There are 86400 seconds in a standard day.
        Instant outOfBoundsInstant = Instant.ofEpochSecond((maxSupportedEpochDay + 1) * 86400L);
        Clock clockBeyondMaxDate = Clock.fixed(outOfBoundsInstant, ZoneOffset.UTC);

        // Act & Assert: Perform the action and verify the outcome.
        try {
            chronology.dateNow(clockBeyondMaxDate);
            fail("Expected a DateTimeException to be thrown for a date outside the supported range.");
        } catch (DateTimeException e) {
            // Verify that the exception is thrown for the correct reason.
            // The message should indicate that the EpochDay value is invalid.
            assertTrue(
                    "Exception message should indicate an invalid EpochDay value.",
                    e.getMessage().contains("Invalid value for EpochDay")
            );
        }
    }
}