package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.DateTimeConstants;

/**
 * This test class focuses on verifying the constants used within the IslamicChronology.
 * The original test was auto-generated and has been rewritten for clarity and maintainability.
 */
public class IslamicChronologyTest {

    /**
     * Tests that {@link IslamicChronology#getAverageMillisPerMonth()} returns the correct
     * pre-calculated value.
     * <p>
     * The Islamic calendar is a lunar calendar. The average milliseconds per month
     * is based on a fixed approximation of the synodic month (the time from one new moon 
     * to the next). The specific constant used by {@code IslamicChronology} is 29.53056 days.
     */
    @Test
    public void getAverageMillisPerMonth_returnsConstantBasedOnSynodicMonthApproximation() {
        // Arrange
        // The constant for the average number of days in a month used by IslamicChronology.
        final double AVERAGE_DAYS_PER_LUNAR_MONTH = 29.53056;
        final long expectedAverageMillis =
                (long) (AVERAGE_DAYS_PER_LUNAR_MONTH * DateTimeConstants.MILLIS_PER_DAY);

        // The IslamicChronology is a singleton for a given time zone and leap year pattern.
        // We can use the default instance to test this constant value.
        IslamicChronology chronology = IslamicChronology.getInstance();

        // Act
        long actualAverageMillis = chronology.getAverageMillisPerMonth();

        // Assert
        // The expected value is 2551440384L. By calculating it from its
        // constituent parts, we make the test's purpose clear and avoid a "magic number".
        assertEquals(expectedAverageMillis, actualAverageMillis);
    }
}