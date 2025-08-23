package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * A more understandable test for the GregorianChronology class.
 */
public class GregorianChronologyTest {

    /**
     * Tests that the first millisecond of the Unix epoch year (1970) is correctly calculated as 0.
     */
    @Test
    public void calculateFirstDayOfYearMillis_forEpochYear1970_shouldReturnZero() {
        // Arrange: Set up the test context.
        // Get an instance of the GregorianChronology, which defaults to the UTC time zone.
        GregorianChronology chronology = GregorianChronology.getInstance();
        final int epochYear = 1970;

        // Act: Execute the method under test.
        long firstDayMillis = chronology.calculateFirstDayOfYearMillis(epochYear);

        // Assert: Verify the outcome.
        // The Unix epoch is defined as starting at 1970-01-01 00:00:00 UTC.
        // This moment corresponds to 0 milliseconds.
        assertEquals("The first day of the epoch year 1970 should be 0L.", 0L, firstDayMillis);
    }
}