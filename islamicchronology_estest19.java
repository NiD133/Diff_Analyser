package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import org.joda.time.DateTimeZone;

/**
 * Test suite for the {@link IslamicChronology} class.
 */
public class IslamicChronologyTest {

    /**
     * Tests that getTotalMillisByYearMonth() correctly calculates the instant
     * when provided with a large negative month value.
     *
     * <p>The method should handle out-of-range month values by rolling the year
     * backward appropriately. This test verifies that calculating the milliseconds
     * for year 1 and month -1291 yields the same result as calculating it for the
     * equivalent, canonical date, which is the 5th month of year -107 AH.
     * </p>
     */
    @Test(timeout = 4000)
    public void getTotalMillisByYearMonth_withLargeNegativeMonth_calculatesCorrectInstant() {
        // Arrange
        IslamicChronology chronology = IslamicChronology.getInstanceUTC();

        // A month value of -1291 in year 1 is equivalent to subtracting 1292 months
        // from the start of year 1, month 1.
        // 1292 months = 107 years and 8 months.
        // Rolling back from 1/1 AH:
        // Year: 1 - 107 = -106
        // Month: 1 - 8 = -7, which requires rolling back one more year.
        // Final Year: -107 AH
        // Final Month: 12 + 1 - 8 = 5
        final int equivalentYear = -107;
        final int equivalentMonth = 5;

        // Calculate the expected milliseconds for the equivalent date directly.
        long expectedMillis = chronology.getTotalMillisByYearMonth(equivalentYear, equivalentMonth);

        // Act
        // Calculate the milliseconds using the original out-of-range month value.
        long actualMillis = chronology.getTotalMillisByYearMonth(1, -1291);

        // Assert
        assertEquals("Milliseconds for year 1, month -1291 should match year -107, month 5",
                     expectedMillis, actualMillis);
    }
}