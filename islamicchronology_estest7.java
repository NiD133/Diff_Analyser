package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This test class contains tests for the IslamicChronology class.
 * This specific test focuses on the behavior of the getDaysInYearMonth method.
 */
public class IslamicChronology_ESTestTest7 extends IslamicChronology_ESTest_scaffolding {

    /**
     * Tests that getDaysInYearMonth correctly handles a month value greater than 12
     * by wrapping it into subsequent years.
     *
     * <p>In the Islamic calendar, odd months have 30 days and even months have 29
     * (with an exception for the 12th month in a leap year). This test uses a month
     * value of 354, which is expected to wrap around multiple years.</p>
     *
     * <p>Calculation:
     * <ul>
     *   <li>Input month: 354
     *   <li>354 months = 29 years and 6 months (since 354 = 29 * 12 + 6)
     *   <li>The method should therefore calculate the days for the 6th month of the 29th year
     *       after the initial year (354).
     *   <li>Effective year: 354 + 29 = 383
     *   <li>Effective month: 6
     *   <li>The 6th month is an even-numbered month, so it should have 29 days.
     * </ul>
     * </p>
     */
    @Test
    public void getDaysInYearMonth_whenMonthOverflows_returnsDaysForCorrectlyCalculatedMonth() {
        // Arrange
        IslamicChronology islamicChronology = IslamicChronology.getInstance();
        int initialYear = 354;
        
        // Use a month value far outside the normal 1-12 range to test year-wrapping logic.
        int monthValue = 354; 
        
        // The 6th month in the Islamic calendar is an even month, which has 29 days.
        int expectedDaysInMonth = 29;

        // Act
        int actualDaysInMonth = islamicChronology.getDaysInYearMonth(initialYear, monthValue);

        // Assert
        assertEquals(expectedDaysInMonth, actualDaysInMonth);
    }
}