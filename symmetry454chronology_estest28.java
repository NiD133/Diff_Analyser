package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for {@link Symmetry454Chronology}.
 * This test focuses on handling invalid temporal values.
 */
public class Symmetry454ChronologyTest {

    /**
     * Tests that creating a date from a TemporalAccessor fails when its epoch-day
     * value is below the minimum supported by the Symmetry454 calendar system.
     */
    @Test
    public void dateFromTemporalAccessor_whenEpochDayIsBelowMinimum_throwsException() {
        // Arrange: Set up the test case.
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;

        // The valid range for EpochDay in Symmetry454Chronology is known to be
        // [-365961480, 364523156] from the implementation and previous test runs.
        // We will test the lower boundary condition.
        long minValidEpochDay = -365961480L;
        long outOfRangeEpochDay = minValidEpochDay - 1;

        // Create a standard TemporalAccessor (a LocalDate) with an epoch day value
        // that is just outside the supported range.
        TemporalAccessor dateWithEpochDayOutOfRange = LocalDate.ofEpochDay(outOfRangeEpochDay);

        // Act & Assert: Execute the method and verify the outcome.
        try {
            chronology.date(dateWithEpochDayOutOfRange);
            fail("Expected DateTimeException was not thrown for an epoch day below the minimum.");
        } catch (DateTimeException e) {
            // The exception was thrown as expected.
            // For completeness, we can check if the message is informative.
            String expectedMessageContent = "Invalid value for EpochDay";
            assertTrue(
                "The exception message should explain the error.",
                e.getMessage().contains(expectedMessageContent)
            );
        }
    }
}