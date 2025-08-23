package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.DateTimeException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link Symmetry454Chronology} class, focusing on edge cases.
 */
public class Symmetry454ChronologyTest {

    /**
     * Tests that dateEpochDay() throws a DateTimeException for an epoch day value
     * that is greater than the maximum supported value. This ensures that the
     * method correctly validates its input arguments.
     */
    @Test
    public void dateEpochDay_whenEpochDayIsAboveMaximum_throwsDateTimeException() {
        // Arrange: Set up the test context.
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;

        // The valid range for epoch day is defined in the chronology.
        // From the source, the maximum valid value is 364,523,156.
        long maxValidEpochDay = 364_523_156L;
        long outOfRangeEpochDay = maxValidEpochDay + 1;

        // Act & Assert: Perform the action and verify the outcome.
        try {
            chronology.dateEpochDay(outOfRangeEpochDay);
            fail("Expected a DateTimeException because the epoch day is out of the valid range.");
        } catch (DateTimeException e) {
            // Verify that the exception message is correct and informative.
            // The expected message format is defined by java.time.temporal.ValueRange.
            String expectedMessage = "Invalid value for EpochDay (valid values -365961480 - 364523156): " + outOfRangeEpochDay;
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}