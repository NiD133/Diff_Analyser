package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.ValueRange;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test class for {@link Symmetry010Chronology}.
 * This class contains the improved test case.
 */
// The original class name "Symmetry010Chronology_ESTestTest30" was kept
// as requested, but in a real-world scenario, it should be renamed to
// something like "Symmetry010ChronologyTest".
public class Symmetry010Chronology_ESTestTest30 {

    /**
     * Tests that creating a date from a TemporalAccessor with an epoch-day
     * value outside the chronology's valid range throws a DateTimeException.
     */
    @Test
    public void date_fromTemporalWithEpochDayOutOfRange_throwsException() {
        // Arrange: Define the test context and inputs.
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;

        // Programmatically determine the valid range to avoid magic numbers.
        ValueRange validEpochDayRange = chronology.range(ChronoField.EPOCH_DAY);
        long outOfRangeEpochDay = validEpochDayRange.getMaximum() + 1;

        // Use a standard Java Time class (LocalDate) as the TemporalAccessor.
        // This makes the test self-contained and easier to understand than using a custom type like PaxDate.
        TemporalAccessor temporalWithInvalidEpochDay = LocalDate.ofEpochDay(outOfRangeEpochDay);

        // Act & Assert: Perform the action and verify the outcome.
        try {
            chronology.date(temporalWithInvalidEpochDay);
            fail("Expected a DateTimeException to be thrown for an out-of-range epoch day.");
        } catch (DateTimeException e) {
            // Verify that the exception message is informative and correct.
            String actualMessage = e.getMessage();
            assertTrue(
                "Exception message should indicate an invalid epoch day. Got: " + actualMessage,
                actualMessage.contains("Invalid value for EpochDay")
            );
            assertTrue(
                "Exception message should contain the invalid value. Got: " + actualMessage,
                actualMessage.contains(String.valueOf(outOfRangeEpochDay))
            );
        }
    }
}