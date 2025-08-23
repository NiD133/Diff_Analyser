package org.joda.time.chrono;

import org.joda.time.DateTimeConstants;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link GregorianChronology} class.
 */
public class GregorianChronologyTest {

    /**
     * Verifies that getAverageMillisPerYear() returns the correct, pre-calculated
     * constant value for the average length of a year in the Gregorian calendar.
     */
    @Test
    public void getAverageMillisPerYear_returnsCorrectConstantValue() {
        // Arrange
        // The average number of days in a Gregorian year is 365.2425.
        // This is based on the leap year rule: a leap year occurs every 4 years,
        // except for years divisible by 100 but not by 400.
        // Over a 400-year cycle, this results in 97 leap years.
        // Average days = (365 * 400 + 97) / 400 = 365.2425
        final double AVERAGE_DAYS_PER_GREGORIAN_YEAR = 365.2425;
        final long EXPECTED_AVERAGE_MILLIS_PER_YEAR =
                (long) (AVERAGE_DAYS_PER_GREGORIAN_YEAR * DateTimeConstants.MILLIS_PER_DAY);

        GregorianChronology chronology = GregorianChronology.getInstance();

        // Act
        long actualAverageMillisPerYear = chronology.getAverageMillisPerYear();

        // Assert
        assertEquals(
                "The average milliseconds per year should match the Gregorian calendar definition.",
                EXPECTED_AVERAGE_MILLIS_PER_YEAR,
                actualAverageMillisPerYear
        );
    }
}