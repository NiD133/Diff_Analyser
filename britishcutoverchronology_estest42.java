package org.threeten.extra.chrono;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link BritishCutoverChronology}.
 */
public class BritishCutoverChronologyTest {

    @Test
    public void isLeapYear_forGregorianLeapYearAfterCutover_returnsTrue() {
        // Arrange
        // The BritishCutoverChronology uses Gregorian rules for years after the 1752 cutover.
        // The year 3676 is after the cutover and is divisible by 4, so it is a leap year.
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        long gregorianLeapYear = 3676L;

        // Act
        boolean isLeap = chronology.isLeapYear(gregorianLeapYear);

        // Assert
        assertTrue("Year " + gregorianLeapYear + " should be a leap year under Gregorian rules.", isLeap);
    }
}