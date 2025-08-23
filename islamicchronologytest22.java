package org.joda.time.chrono;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.joda.time.chrono.IslamicChronology.LeapYearPatternType;
import org.junit.Test;

/**
 * A focused test for the leap year calculation of the Habash al-Hasib pattern
 * in IslamicChronology.
 *
 * <p>This test verifies that the isLeapYear() method correctly identifies all leap years
 * within a standard 30-year cycle for this specific pattern.
 */
public class IslamicChronology_HabashAlHasibLeapYearTest {

    private static final LeapYearPatternType HABASH_AL_HASIB_PATTERN =
            IslamicChronology.LEAP_YEAR_HABASH_AL_HASIB;

    /**
     * Tests that the Habash al-Hasib pattern correctly identifies leap years
     * throughout its 30-year cycle.
     *
     * <p>The source documentation for {@link IslamicChronology} specifies the leap years for this
     * pattern are: 2, 5, 8, 11, 13, 16, 19, 21, 24, 27, and 30. This test validates that
     * the implementation matches this specification.
     */
    @Test
    public void habashAlHasibPattern_identifiesLeapYearsIn30YearCycle() {
        // Define the set of years that are expected to be leap years in the 30-year cycle.
        final Set<Integer> expectedLeapYears = new HashSet<>(Arrays.asList(
            2, 5, 8, 11, 13, 16, 19, 21, 24, 27, 30
        ));

        // Iterate through the entire 30-year cycle to verify each year.
        for (int year = 1; year <= 30; year++) {
            boolean isActualLeapYear = HABASH_AL_HASIB_PATTERN.isLeapYear(year);

            if (expectedLeapYears.contains(year)) {
                assertTrue(
                    "Year " + year + " should be a leap year for Habash al-Hasib pattern.",
                    isActualLeapYear);
            } else {
                assertFalse(
                    "Year " + year + " should NOT be a leap year for Habash al-Hasib pattern.",
                    isActualLeapYear);
            }
        }
    }
}