package org.threeten.extra;

import org.junit.Test;
import java.time.Month;
import java.time.YearMonth;
import static org.junit.Assert.assertFalse;

// The original test class structure is kept for context.
// Unused imports from the original test have been removed for clarity.
public class DayOfMonth_ESTestTest32 extends DayOfMonth_ESTest_scaffolding {

    /**
     * Tests that isValidYearMonth() returns false for a day that does not exist in the given month.
     * For example, the 31st of April.
     */
    @Test
    public void isValidYearMonth_returnsFalse_forDay31_inMonthWithOnly30Days() {
        // Arrange: Create a day-of-month for the 31st and a YearMonth for April (which has 30 days).
        DayOfMonth day31 = DayOfMonth.of(31);
        YearMonth april = YearMonth.of(2023, Month.APRIL);

        // Act: Check if the 31st is a valid day for April.
        boolean isValid = day31.isValidYearMonth(april);

        // Assert: The result should be false, as April does not have 31 days.
        assertFalse("The 31st should be an invalid day for April.", isValid);
    }
}