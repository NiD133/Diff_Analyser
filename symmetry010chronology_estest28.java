package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.DateTimeException;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link Symmetry010Chronology} class.
 */
public class Symmetry010ChronologyTest {

    /**
     * Tests that dateEpochDay() throws a DateTimeException
     * when the provided epoch day is greater than the maximum supported value.
     */
    @Test
    public void dateEpochDay_throwsExceptionForEpochDayBeyondMaximum() {
        // Arrange: Get the valid range and determine the first invalid value.
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        ValueRange validRange = chronology.range(ChronoField.EPOCH_DAY);
        long epochDayTooLarge = validRange.getMaximum() + 1;

        // Act & Assert
        try {
            chronology.dateEpochDay(epochDayTooLarge);
            fail("Expected a DateTimeException because the epoch day is out of the valid range.");
        } catch (DateTimeException e) {
            // Verify that the exception message is informative and correct.
            assertTrue(
                "Exception message should indicate an invalid epoch day",
                e.getMessage().contains("Invalid value for EpochDay")
            );
        }
    }
}