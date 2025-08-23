package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Contains unit tests for the {@link IslamicChronology} class.
 */
public class IslamicChronologyTest {

    /**
     * Tests that the last month of a non-leap year correctly returns 29 days.
     *
     * <p>According to the tabular Islamic calendar, the 12th and final month
     * (Dhú'l-Hijja) has 29 days in a common year and is extended to 30 days
     * in a leap year. This test verifies the common year scenario.
     */
    @Test
    public void getDaysInYearMonth_forLastMonthInNonLeapYear_returns29() {
        // Arrange: Create a UTC IslamicChronology instance.
        IslamicChronology chronology = IslamicChronology.getInstanceUTC();
        
        // Year 12 AH is a non-leap year in the default (16-based) pattern.
        int nonLeapYear = 12;
        int lastMonthOfYear = 12;
        
        // This assertion makes the test's assumption explicit and self-verifying.
        assertFalse("Test precondition failed: Year " + nonLeapYear + " AH should not be a leap year.",
                    chronology.isLeapYear(nonLeapYear));

        // Act: Get the number of days in the specified month and year.
        int actualDays = chronology.getDaysInYearMonth(nonLeapYear, lastMonthOfYear);

        // Assert: Verify that the number of days is 29.
        int expectedDays = 29;
        assertEquals("The last month of a non-leap year should have 29 days.",
                     expectedDays, actualDays);
    }
}