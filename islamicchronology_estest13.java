package org.joda.time.chrono;

import org.joda.time.DateTimeZone;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the IslamicChronology class, focusing on leap year calculations.
 */
public class IslamicChronologyTest {

    /**
     * Verifies that year 30 is correctly identified as a leap year
     * when using the Habash al-Hasib leap year pattern.
     */
    @Test
    public void isLeapYear_withHabashAlHasibPattern_returnsTrueForYear30() {
        // Arrange
        // The source documentation for IslamicChronology specifies that for the
        // Habash al-Hasib pattern, the 30th year of a 30-year cycle is a leap year.
        IslamicChronology.LeapYearPatternType habashAlHasibPattern = IslamicChronology.LEAP_YEAR_HABASH_AL_HASIB;
        IslamicChronology chronology = IslamicChronology.getInstance(DateTimeZone.UTC, habashAlHasibPattern);
        int yearInCycle = 30;

        // Act
        boolean isLeap = chronology.isLeapYear(yearInCycle);

        // Assert
        assertTrue("Year 30 should be a leap year in the Habash al-Hasib pattern.", isLeap);
    }
}