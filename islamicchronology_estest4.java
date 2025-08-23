package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.Chronology;

// The original test class name and inheritance are preserved as per the prompt's context.
// In a typical refactoring, these would be updated to standard conventions (e.g., class IslamicChronologyTest).
public class IslamicChronology_ESTestTest4 extends IslamicChronology_ESTest_scaffolding {

    /**
     * Tests the low-level calculation of milliseconds for a given date, specifically
     * using the maximum supported year and out-of-bounds month/day values.
     *
     * <p>The {@code getYearMonthDayMillis} method is an internal calculation helper
     * that does not perform validation on its inputs. This test verifies that the
     * calculation remains consistent and does not cause an overflow, even at the
     * highest supported year boundary with unusual inputs.
     */
    @Test
    public void getYearMonthDayMillis_shouldCalculateCorrectlyForMaxYearAndNegativeMonth() {
        // ARRANGE
        // Use the 15-based leap year pattern, consistent with the original test.
        IslamicChronology.LeapYearPatternType fifteenBasedLeapPattern = IslamicChronology.LEAP_YEAR_15_BASED;
        IslamicChronology islamicChronology = new IslamicChronology(null, null, fifteenBasedLeapPattern);

        // Define boundary and out-of-bounds inputs.
        // The year is the maximum value supported by this chronology.
        final int year = IslamicChronology.MAX_YEAR;
        // The month is negative, and the day is a large positive number.
        // This tests the raw calculation logic without input validation.
        final int month = -1;
        final int day = 273;

        // This is the pre-calculated expected result for the given inputs, ensuring
        // the calculation at this boundary remains stable across code changes.
        final long expectedMillis = 8948501182656000000L;

        // ACT
        long actualMillis = islamicChronology.getYearMonthDayMillis(year, month, day);

        // ASSERT
        assertEquals(expectedMillis, actualMillis);
    }
}