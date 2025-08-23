package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

/**
 * Tests the Indian leap year pattern for the IslamicChronology.
 * The Indian pattern defines which years in a 30-year cycle are leap years.
 */
public class IslamicChronologyIndianLeapYearTest {

    /**
     * Verifies that the isLeapYear method for the Indian pattern correctly identifies
     * leap years within its 30-year cycle.
     *
     * <p>According to the source documentation, the Indian pattern considers the following
     * years as leap years in a 30-year cycle: 2, 5, 8, 10, 13, 16, 19, 21, 24, 27, and 29.
     */
    @Test
    public void testIsLeapYear_forIndian30YearCycle() {
        // The set of years that are expected to be leap years in the 30-year Indian cycle.
        final Set<Integer> expectedLeapYears = new HashSet<>(
            Arrays.asList(2, 5, 8, 10, 13, 16, 19, 21, 24, 27, 29)
        );

        final int cycleLength = 30;
        IslamicChronology.LeapYearPatternType indianPattern = IslamicChronology.LEAP_YEAR_INDIAN;

        for (int year = 1; year <= cycleLength; year++) {
            boolean isLeap = indianPattern.isLeapYear(year);
            boolean expectedToBeLeap = expectedLeapYears.contains(year);

            assertEquals(
                "Year " + year + " in the 30-year cycle should " + (expectedToBeLeap ? "" : "not ") + "be a leap year.",
                expectedToBeLeap,
                isLeap
            );
        }
    }
}