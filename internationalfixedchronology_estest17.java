package org.threeten.extra.chrono;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link InternationalFixedChronology} class.
 */
public class InternationalFixedChronologyTest {

    /**
     * Tests that a date created from epoch day 0 (1970-01-01) correctly reports the length of its year.
     * The year 1970 is not a leap year, so it should have 365 days.
     */
    @Test
    public void dateEpochDay_forNonLeapYear_returnsCorrectYearLength() {
        // Arrange
        // Epoch day 0 corresponds to 1970-01-01 in the ISO calendar.
        // The International Fixed Chronology shares the same leap year rules.
        // 1970 is not a leap year.
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        long epochDayFor1970 = 0L;
        int expectedDaysInYear = 365;

        // Act
        InternationalFixedDate date = chronology.dateEpochDay(epochDayFor1970);

        // Assert
        assertEquals("The year 1970 should have 365 days", expectedDaysInYear, date.lengthOfYear());
    }
}