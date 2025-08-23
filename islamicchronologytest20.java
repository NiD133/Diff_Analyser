package org.joda.time.chrono;

import junit.framework.TestCase;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Tests the isLeapYear method for the 16-based leap year pattern
 * in IslamicChronology.
 *
 * @see IslamicChronology.LeapYearPatternType
 */
public class IslamicChronology16BasedLeapYearTest extends TestCase {

    /**
     * Verifies that the LEAP_YEAR_16_BASED pattern correctly identifies leap years
     * over a full 30-year cycle.
     *
     * <p>According to the 16-based system documentation, the leap years in a
     * 30-year cycle are: 2, 5, 7, 10, 13, 16, 18, 21, 24, 26, and 29.
     * This test confirms that the implementation matches this specification.
     */
    public void testIsLeapYear_for16BasedPattern_over30YearCycle() {
        // The set of years that are defined as leap years in the 16-based pattern.
        final Set<Integer> expectedLeapYears = new HashSet<>(Arrays.asList(
            2, 5, 7, 10, 13, 16, 18, 21, 24, 26, 29
        ));

        final int cycleLength = 30;

        for (int year = 1; year <= cycleLength; year++) {
            boolean isYearExpectedToBeLeap = expectedLeapYears.contains(year);
            boolean isYearActuallyLeap = IslamicChronology.LEAP_YEAR_16_BASED.isLeapYear(year);

            if (isYearExpectedToBeLeap) {
                assertTrue(
                    "Year " + year + " should be a leap year in the 16-based pattern",
                    isYearActuallyLeap
                );
            } else {
                assertFalse(
                    "Year " + year + " should NOT be a leap year in the 16-based pattern",
                    isYearActuallyLeap
                );
            }
        }
    }
}