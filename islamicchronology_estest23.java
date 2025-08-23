package org.joda.time.chrono;

import org.joda.time.DateTimeConstants;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This test class contains the refactored test case.
 * The original test class name and inheritance structure are preserved.
 */
public class IslamicChronology_ESTestTest23 extends IslamicChronology_ESTest_scaffolding {

    /**
     * Tests that getDayOfMonth() returns 0 for dates before the first year of the Islamic calendar.
     * <p>
     * The IslamicChronology in Joda-Time is not proleptic, meaning it does not support dates
     * before the first year (1 AH). This test verifies that the system handles such dates gracefully
     * by returning 0, which indicates an invalid or out-of-range field value.
     */
    @Test
    public void getDayOfMonth_forDateBeforeFirstYear_returnsZero() {
        // Arrange
        // Use the UTC instance to ensure the test is deterministic and not affected by the system's default time zone.
        IslamicChronology islamicChronology = IslamicChronology.getInstanceUTC();

        // Define an instant that is explicitly outside the supported range of the calendar.
        // We calculate the first millisecond of year 1 and then subtract one day to get an instant
        // that is guaranteed to be before the calendar's start.
        long startOfYear1 = islamicChronology.calculateFirstDayOfYearMillis(1);
        long instantBeforeCalendarStart = startOfYear1 - DateTimeConstants.MILLIS_PER_DAY;

        // Act
        int dayOfMonth = islamicChronology.getDayOfMonth(instantBeforeCalendarStart);

        // Assert
        // For dates before the supported era, getDayOfMonth is expected to return 0.
        assertEquals(0, dayOfMonth);
    }
}