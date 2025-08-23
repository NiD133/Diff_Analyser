package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.DateTimeZone;

/**
 * Tests for the {@link CopticChronology} class, focusing on boundary conditions.
 */
public class CopticChronology_ESTestTest1 extends CopticChronology_ESTest_scaffolding {

    /**
     * Tests that {@link CopticChronology#calculateFirstDayOfYearMillis(int)}
     * correctly calculates the first day for the maximum supported year.
     *
     * <p>The CopticChronology in Joda-Time has a defined maximum year limit. [1]
     * This test verifies the calculation at this upper boundary to prevent
     * potential overflow errors and ensure correctness at the edge of the
     * supported date range.</p>
     */
    @Test(timeout = 4000)
    public void testCalculateFirstDayMillisForMaxYear() {
        // Arrange: Get the CopticChronology instance and its defined maximum year.
        CopticChronology copticChronology = CopticChronology.getInstanceUTC();
        int maxYear = copticChronology.getMaxYear();
        long expectedFirstDayMillis = 9223371994233600000L;

        // Act: Calculate the first day of the maximum year in milliseconds.
        long actualFirstDayMillis = copticChronology.calculateFirstDayOfYearMillis(maxYear);

        // Assert: Verify that the calculated milliseconds match the expected value.
        assertEquals("The first day of the maximum year should be calculated correctly.",
                     expectedFirstDayMillis,
                     actualFirstDayMillis);
    }
}