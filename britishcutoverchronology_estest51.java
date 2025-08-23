package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.DateTimeException;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for {@link BritishCutoverChronology}.
 */
public class BritishCutoverChronologyTest {

    /**
     * Tests that creating a date with an epoch day that is too far in the past
     * throws a DateTimeException. The valid year range for this chronology is
     * -999,998 to 999,999.
     */
    @Test
    public void dateEpochDay_withEpochDayBelowSupportedRange_throwsDateTimeException() {
        // Arrange: Set up the chronology and an epoch day value that is known to be out of bounds.
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        // This epoch day value corresponds to a year far below the minimum supported year.
        long epochDayBelowRange = -2_135_812_540L;

        // Act & Assert: Attempt to create a date and verify that the correct exception is thrown.
        try {
            chronology.dateEpochDay(epochDayBelowRange);
            fail("Expected a DateTimeException to be thrown for an epoch day outside the supported range.");
        } catch (DateTimeException e) {
            // Verify that the exception message correctly identifies an invalid year as the cause.
            String expectedMessageContent = "Invalid value for Year";
            assertTrue(
                "Exception message should contain '" + expectedMessageContent + "'. Actual: " + e.getMessage(),
                e.getMessage().contains(expectedMessageContent)
            );
        }
    }
}