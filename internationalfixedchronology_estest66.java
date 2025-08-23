package org.threeten.extra.chrono;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link InternationalFixedChronology} class.
 */
public class InternationalFixedChronologyTest {

    /**
     * Tests that creating a date from an era, year, and day-of-year
     * for a non-leap year produces the correct date components.
     */
    @Test
    public void dateYearDay_withEra_createsCorrectDateForNonLeapYear() {
        // Arrange
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        int year = 157; // 157 is not a leap year in the Gregorian/International Fixed system.
        int dayOfYear = 3;

        // Act
        InternationalFixedDate ifcDate = chronology.dateYearDay(InternationalFixedEra.CE, year, dayOfYear);

        // Assert
        // In the International Fixed calendar, all months have 28 days (except for special cases).
        // Therefore, the 3rd day of the year should be the 3rd day of the 1st month.
        assertEquals("Year should match", year, ifcDate.getYear());
        assertEquals("Month should be the first month", 1, ifcDate.getMonthValue());
        assertEquals("Day of month should match", 3, ifcDate.getDayOfMonth());
        assertEquals("Length of a non-leap year should be 365", 365, ifcDate.lengthOfYear());
    }
}