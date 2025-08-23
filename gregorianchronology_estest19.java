package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

/**
 * Contains tests for the {@link GregorianChronology} class.
 */
public class GregorianChronologyTest {

    /**
     * Tests that calculateFirstDayOfYearMillis() returns the correct millisecond
     * instant for the beginning of year 0.
     */
    @Test
    public void calculateFirstDayOfYearMillis_forYearZero_returnsCorrectInstant() {
        // Arrange
        GregorianChronology chronology = GregorianChronology.getInstanceUTC();
        final int year = 0;

        // The expected result is the millisecond instant for midnight on January 1st of year 0, UTC.
        // We create a DateTime object for this moment to derive the expected value.
        // This approach is self-documenting and avoids using an unexplained "magic number".
        DateTime startOfYearZero = new DateTime(year, 1, 1, 0, 0, 0, 0, chronology);
        long expectedMillis = startOfYearZero.getMillis();

        // Act
        long actualMillis = chronology.calculateFirstDayOfYearMillis(year);

        // Assert
        assertEquals("Millis for the start of year 0 should be calculated correctly.",
                expectedMillis, actualMillis);
    }
}