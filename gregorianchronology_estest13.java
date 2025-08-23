package org.joda.time.chrono;

import org.joda.time.DateTimeConstants;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the GregorianChronology class.
 */
public class GregorianChronologyTest {

    /**
     * Verifies that getAverageMillisPerMonth() returns the correct, pre-calculated average.
     * The average is based on a Gregorian year having 365.2425 days.
     */
    @Test
    public void getAverageMillisPerMonth_returnsCorrectConstantValue() {
        // Arrange
        GregorianChronology chronology = GregorianChronology.getInstanceUTC();

        // The expected value is derived from the average number of days in a Gregorian year.
        // Calculation: (365.2425 days/year * MILLIS_PER_DAY) / 12 months/year
        long expectedAverageMillis = (long) (365.2425 * DateTimeConstants.MILLIS_PER_DAY / 12);

        // Act
        long actualAverageMillis = chronology.getAverageMillisPerMonth();

        // Assert
        assertEquals(expectedAverageMillis, actualAverageMillis);
    }
}