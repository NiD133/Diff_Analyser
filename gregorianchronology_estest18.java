package org.joda.time.chrono;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link GregorianChronology}.
 */
public class GregorianChronologyTest {

    /**
     * Tests that calculateFirstDayOfYearMillis() returns the correct number of milliseconds
     * from the epoch for a given future year.
     */
    @Test
    public void shouldCalculateCorrectMillisForFirstDayOfFutureYear() {
        // Arrange: Set up the chronology and the year to be tested.
        // We use the UTC instance to ensure the test is consistent across different environments,
        // as epoch milliseconds are defined relative to UTC.
        GregorianChronology chronology = GregorianChronology.getInstanceUTC();
        int year = 3000;

        // Act: Call the method under test.
        long actualMillis = chronology.calculateFirstDayOfYearMillis(year);

        // Assert: Verify the result by comparing it to a known-good calculation.
        // Creating a DateTime for the start of the year 3000 provides a clear,
        // verifiable expected value, avoiding the "magic number" 32503680000000L.
        DateTime expectedDateTime = new DateTime(year, 1, 1, 0, 0, 0, 0, chronology);
        long expectedMillis = expectedDateTime.getMillis();

        assertEquals(expectedMillis, actualMillis);
    }
}