package org.joda.time.chrono;

import org.joda.time.DateTimeConstants;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link IslamicChronology}.
 */
public class IslamicChronologyTest {

    @Test
    public void getAverageMillisPerYearDividedByTwo_returnsCorrectlyCalculatedValue() {
        // Arrange
        IslamicChronology islamicChronology = IslamicChronology.getInstanceUTC();

        // The Islamic calendar is based on a 30-year cycle containing 19 common years (354 days)
        // and 11 leap years (355 days). We calculate the expected average from these constants.
        final long commonYearsInCycle = 19;
        final long leapYearsInCycle = 11;
        final long cycleLengthInYears = 30;

        final long daysInCycle = (commonYearsInCycle * 354L) + (leapYearsInCycle * 355L);
        final long avgMillisPerYear = (daysInCycle * DateTimeConstants.MILLIS_PER_DAY) / cycleLengthInYears;
        final long expectedValue = avgMillisPerYear / 2;

        // This calculation results in (10631 * 86,400,000) / 30 / 2 = 15,310,040,000
        final long expectedCorrectValue = 15_310_040_000L;
        assertEquals(expectedCorrectValue, expectedValue); // Sanity check the calculation

        // Act
        long actualValue = islamicChronology.getAverageMillisPerYearDividedByTwo();

        // Assert
        assertEquals(expectedValue, actualValue);
    }
}