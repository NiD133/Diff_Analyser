package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the inner class {@link IslamicChronology.LeapYearPatternType}.
 *
 * This test class focuses on verifying the core functionality of the leap year
 * pattern logic in a clear and maintainable way, replacing an obscure,
 * auto-generated test case.
 */
public class IslamicChronologyLeapYearPatternTest {

    // The LEAP_YEAR_16_BASED pattern is a commonly used variant for the
    // Islamic calendar. According to its definition, the leap years
    // within its 30-year cycle are: 2, 5, 7, 10, 13, 16, 18, 21, 24, 26, and 29.
    private final IslamicChronology.LeapYearPatternType pattern16Based =
            IslamicChronology.LEAP_YEAR_16_BASED;

    @Test
    public void isLeapYear_shouldReturnTrue_forYearsThatAreLeapInThePattern() {
        // Arrange: Years known to be leap years in the 16-based pattern.
        int firstLeapYearInCycle = 2;
        int aMiddleLeapYearInCycle = 16;
        int anotherLeapYearInCycle = 29;

        // Act & Assert: Verify that the method correctly identifies these as leap years.
        assertTrue("Year 2 should be a leap year in the 16-based pattern",
                pattern16Based.isLeapYear(firstLeapYearInCycle));
        assertTrue("Year 16 should be a leap year in the 16-based pattern",
                pattern16Based.isLeapYear(aMiddleLeapYearInCycle));
        assertTrue("Year 29 should be a leap year in the 16-based pattern",
                pattern16Based.isLeapYear(anotherLeapYearInCycle));
    }

    @Test
    public void isLeapYear_shouldReturnFalse_forYearsThatAreNotLeapInThePattern() {
        // Arrange: Years known to be non-leap years in the 16-based pattern.
        int firstYearInCycle = 1;
        int aNonLeapYearInCycle = 17;
        int lastYearInCycle = 30;

        // Act & Assert: Verify that the method correctly identifies these as non-leap years.
        assertFalse("Year 1 should not be a leap year in the 16-based pattern",
                pattern16Based.isLeapYear(firstYearInCycle));
        assertFalse("Year 17 should not be a leap year in the 16-based pattern",
                pattern16Based.isLeapYear(aNonLeapYearInCycle));
        assertFalse("Year 30 should not be a leap year in the 16-based pattern",
                pattern16Based.isLeapYear(lastYearInCycle));
    }
}