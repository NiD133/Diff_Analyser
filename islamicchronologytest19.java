package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Tests the leap year patterns in the IslamicChronology.
 * This test focuses on the LEAP_YEAR_15_BASED pattern.
 */
public class IslamicChronologyLeapYearPatternTest {

    /**
     * Verifies that the 15-based leap year pattern correctly identifies leap years
     * within its 30-year cycle, as documented in the IslamicChronology class.
     */
    @Test
    public void test15BasedLeapYear_followsDocumentedPattern() {
        // The 15-based pattern defines the following years in a 30-year cycle as leap years.
        // This set is derived from the documentation of IslamicChronology.
        final Set<Integer> expectedLeapYears = new HashSet<>(Arrays.asList(
            2, 5, 7, 10, 13, 15, 18, 21, 24, 26, 29
        ));

        // The Islamic calendar leap year calculation is based on a 30-year cycle.
        // We test every year in the cycle to ensure the pattern is fully implemented.
        for (int yearInCycle = 1; yearInCycle <= 30; yearInCycle++) {
            boolean isLeap = IslamicChronology.LEAP_YEAR_15_BASED.isLeapYear(yearInCycle);
            boolean expected = expectedLeapYears.contains(yearInCycle);

            String assertionMessage = String.format(
                "Year %d of the 30-year cycle should %sbe a leap year for the 15-based pattern.",
                yearInCycle,
                expected ? "" : "NOT "
            );
            assertEquals(assertionMessage, expected, isLeap);
        }
    }
}