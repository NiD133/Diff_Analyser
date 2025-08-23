package org.joda.time.chrono;

import org.joda.time.DateTimeConstants;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link GregorianChronology}.
 */
public class GregorianChronologyTest {

    @Test
    public void getApproxMillisAtEpochDividedByTwo_returnsCorrectApproximation() {
        // This test verifies the constant value returned by getApproxMillisAtEpochDividedByTwo().
        // This value is an approximation of half the number of milliseconds between
        // year 0 and the epoch (1970-01-01). It's calculated based on the average
        // length of a Gregorian year (365.2425 days).

        // Arrange
        GregorianChronology gregorianChronology = GregorianChronology.getInstanceUTC();

        // The calculation is: (1970 years * average_millis_per_year) / 2
        final long AVERAGE_MILLIS_PER_GREGORIAN_YEAR =
                (long) (365.2425 * DateTimeConstants.MILLIS_PER_DAY);
        final long YEARS_FROM_0_TO_EPOCH = 1970L;

        long expectedValue = (YEARS_FROM_0_TO_EPOCH * AVERAGE_MILLIS_PER_GREGORIAN_YEAR) / 2;
        // The original test asserted against the magic number 31083597720000L.
        // Our calculation derives this number, making the test self-documenting.

        // Act
        long actualValue = gregorianChronology.getApproxMillisAtEpochDividedByTwo();

        // Assert
        assertEquals(expectedValue, actualValue);
    }
}