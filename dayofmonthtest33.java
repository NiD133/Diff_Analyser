package org.threeten.extra;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import java.time.Month;
import java.time.YearMonth;

/**
 * This class contains improved tests for the DayOfMonth class.
 * The original test was auto-generated and has been refactored for better understandability.
 */
public class DayOfMonth_ESTestTest33 extends DayOfMonth_ESTest_scaffolding {

    /**
     * Tests that isValidYearMonth returns true for a day that is guaranteed to be valid
     * for any given month.
     *
     * <p><b>Refactoring Rationale:</b>
     * The original test relied on a mocked clock to provide the current date, which made
     * the test's actual values unclear without inspecting the test's setup. This refactored
     * version uses explicit, hardcoded values (e.g., the 14th of the month) to make the
     * test case self-contained and immediately understandable. The method and variable names
     * have also been made more descriptive to clearly convey the test's intent.
     */
    @Test
    public void isValidYearMonth_whenDayExistsInMonth_shouldReturnTrue() {
        // Arrange: Define a day of the month that exists in every possible month (e.g., 14).
        // A YearMonth is also defined to perform the check against.
        DayOfMonth day14 = DayOfMonth.of(14);
        YearMonth february2023 = YearMonth.of(2023, Month.FEBRUARY);

        // Act: Check if the day is valid for the given year and month.
        boolean isValid = day14.isValidYearMonth(february2023);

        // Assert: The method should return true because the 14th is a valid day in February.
        assertTrue("The 14th should be a valid day in any month, including February 2023.", isValid);
    }
}