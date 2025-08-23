package org.threeten.extra;

import org.junit.Test;

import java.time.DateTimeException;
import java.time.chrono.HijrahDate;
import java.time.temporal.Temporal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

// The original test class extended a scaffolding class.
// This is preserved to maintain compatibility with the original test suite structure.
public class DayOfYear_ESTestTest29 extends DayOfYear_ESTest_scaffolding {

    /**
     * Tests that adjustInto() throws a DateTimeException when attempting to adjust a
     * temporal object that does not use the ISO calendar system.
     *
     * The specification for DayOfYear.adjustInto() requires the temporal object to be
     * based on the ISO chronology. This test verifies that using a non-ISO date,
     * such as HijrahDate, correctly triggers this exception.
     */
    @Test
    public void adjustInto_withNonIsoChronology_throwsException() {
        // Arrange: Create a DayOfYear instance and a date from a non-ISO calendar system.
        DayOfYear dayOfYear = DayOfYear.of(150);
        Temporal nonIsoDate = HijrahDate.of(1440, 1, 1); // HijrahDate uses a non-ISO chronology.

        // Act & Assert: Verify that calling adjustInto with the non-ISO date throws the correct exception.
        try {
            dayOfYear.adjustInto(nonIsoDate);
            fail("Expected a DateTimeException to be thrown, but no exception was thrown.");
        } catch (DateTimeException e) {
            // Assert that the exception message matches the expected message for this error condition.
            assertEquals("Adjustment only supported on ISO date-time", e.getMessage());
        }
    }
}