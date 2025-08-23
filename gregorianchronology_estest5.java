package org.joda.time.chrono;

import org.joda.time.DateTimeConstants;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link GregorianChronology}.
 */
public class GregorianChronologyTest {

    @Test
    public void testGetAverageMillisPerYearDividedByTwo_returnsCorrectConstantValue() {
        // Arrange
        GregorianChronology gregorianChronology = GregorianChronology.getInstance();

        // The Gregorian calendar has an average of 365.2425 days per year.
        // This test verifies the pre-calculated constant for half the average milliseconds in a year.
        final long averageMillisPerYear = (long) (365.2425 * DateTimeConstants.MILLIS_PER_DAY);
        final long expectedHalfAverageMillisPerYear = averageMillisPerYear / 2;

        // Act
        long actualHalfAverageMillisPerYear = gregorianChronology.getAverageMillisPerYearDividedByTwo();

        // Assert
        assertEquals(expectedHalfAverageMillisPerYear, actualHalfAverageMillisPerYear);
    }
}