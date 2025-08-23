package org.joda.time.base;

import org.joda.time.YearMonth;
import org.joda.time.Years;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

// Note: The original test class name 'AbstractPartial_ESTestTest27' is kept as per the prompt's context.
// In a real-world scenario, a more descriptive name like 'YearMonthTest' would be preferable.
public class AbstractPartial_ESTestTest27 extends AbstractPartial_ESTest_scaffolding {

    /**
     * Verifies that subtracting a period of years from a YearMonth
     * correctly adjusts the year while leaving the month unchanged.
     */
    @Test(timeout = 4000)
    public void minusYears_fromYearMonth_adjustsYearAndRetainsMonth() {
        // Arrange: Set up the initial state and expectations.
        final int initialYear = 4;
        final int month = 4; // April
        YearMonth startYearMonth = new YearMonth(initialYear, month);
        Years periodToSubtract = Years.TWO;

        // Act: Perform the action under test.
        YearMonth resultYearMonth = startYearMonth.minus(periodToSubtract);

        // Assert: Verify the outcome.
        int expectedYear = 2; // 4 - 2
        assertEquals("The year should be correctly subtracted.", expectedYear, resultYearMonth.getYear());
        assertEquals("The month should remain unchanged.", month, resultYearMonth.getMonthOfYear());
    }
}