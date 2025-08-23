package org.threeten.extra;

import org.junit.Test;
import java.time.DateTimeException;
import java.time.chrono.HijrahDate;
import java.time.temporal.Temporal;
import org.evosuite.runtime.mock.java.time.chrono.MockHijrahDate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test suite focuses on the behavior of the DayOfYear.adjustInto() method.
 */
public class DayOfYearTest {

    /**
     * Tests that adjustInto() throws a DateTimeException when attempting to adjust
     * a temporal object that does not use the ISO calendar system. The method's contract
     * specifies that it only supports ISO-based date-time objects.
     */
    @Test
    public void adjustInto_whenTemporalIsNotIso_throwsDateTimeException() {
        // Arrange: Create a DayOfYear instance and a date from a non-ISO calendar (Hijrah).
        DayOfYear dayOfYear = DayOfYear.of(42);
        Temporal nonIsoDate = MockHijrahDate.now();

        // Act & Assert: Attempt the adjustment and verify that the correct exception is thrown.
        try {
            dayOfYear.adjustInto(nonIsoDate);
            fail("Expected a DateTimeException because the temporal object is not ISO-based.");
        } catch (DateTimeException e) {
            // Verify that the exception message clearly states the reason for the failure.
            assertEquals("Adjustment only supported on ISO date-time", e.getMessage());
        }
    }
}