package org.threeten.extra;

import org.junit.Test;

import java.time.DateTimeException;
import java.time.temporal.Temporal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.evosuite.runtime.mock.java.time.chrono.MockHijrahDate;

/**
 * This test class is a placeholder for the original's structure.
 * The improved test is self-contained below.
 */
class DayOfMonth_ESTest_scaffolding {}

public class DayOfMonth_ESTestTest29 extends DayOfMonth_ESTest_scaffolding {

    /**
     * Tests that adjustInto() throws a DateTimeException when the temporal object
     * does not use the ISO calendar system.
     */
    @Test
    public void adjustInto_whenTemporalIsNonIso_throwsDateTimeException() {
        // Arrange: Create a DayOfMonth and a temporal object with a non-ISO calendar system.
        // The adjustInto method is documented to only support ISO-based temporals.
        DayOfMonth dayOfMonth = DayOfMonth.of(15);
        Temporal nonIsoTemporal = MockHijrahDate.now(); // HijrahDate is a non-ISO calendar

        // Act & Assert
        try {
            dayOfMonth.adjustInto(nonIsoTemporal);
            fail("Expected a DateTimeException to be thrown, but it was not.");
        } catch (DateTimeException e) {
            // Verify that the exception message clearly states the reason for failure.
            assertEquals("Adjustment only supported on ISO date-time", e.getMessage());
        }
    }
}