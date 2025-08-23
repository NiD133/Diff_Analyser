package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.DateTimeZone;

/**
 * Contains tests for the {@link IslamicChronology} class, focusing on the
 * understandability and correctness of its date calculations.
 */
public class IslamicChronologyTest {

    /**
     * This test is a refactored version of the original machine-generated test.
     * It verifies that the calculation remains consistent even when provided with
     * an extremely large, invalid month value. Such tests are useful for ensuring
     * system robustness against unexpected inputs.
     */
    @Test
    public void getTotalMillisByYearMonth_withExtremelyLargeMonthValue_isConsistent() {
        // Arrange
        // The test uses an invalid value for the month parameter that happens to match
        // the bitmask pattern for the Indian leap year system (LEAP_YEAR_INDIAN).
        // This is characteristic of an automated test probing boundary conditions.
        IslamicChronology islamicChronology = IslamicChronology.getInstanceUTC();
        int year = -182;
        int extremelyLargeAndInvalidMonth = 690562340;
        long expectedMillis = 1760105289686400000L;

        // Act
        long actualMillis = islamicChronology.getTotalMillisByYearMonth(year, extremelyLargeAndInvalidMonth);

        // Assert
        // The assertion confirms that this unusual calculation produces a predictable result
        // without crashing or throwing an unhandled exception.
        assertEquals(expectedMillis, actualMillis);
    }

    /**
     * This test verifies a fundamental and easily understandable use case: calculating
     * the total milliseconds for the very first month of the first year in the
     * Islamic calendar.
     */
    @Test
    public void getTotalMillisByYearMonth_forFirstMonthOfFirstYear_returnsCorrectEpochOffset() {
        // Arrange
        // The Islamic calendar's epoch (Year 1, Month 1) occurs before the
        // standard Java epoch (1970-01-01T00:00:00Z), so its millisecond value is negative.
        IslamicChronology islamicChronology = IslamicChronology.getInstanceUTC();
        final int firstYear = 1;
        final int firstMonth = 1;

        // This is the expected number of milliseconds between the Java epoch and the
        // start of the Islamic calendar (0001-01-01 AH).
        final long expectedMillisForYear1Month1 = -42521587200000L;

        // Act
        long actualMillis = islamicChronology.getTotalMillisByYearMonth(firstYear, firstMonth);

        // Assert
        // For the first month of the first year, the result should match the known
        // millisecond value for the start of the chronology.
        assertEquals(expectedMillisForYear1Month1, actualMillis);
    }
}