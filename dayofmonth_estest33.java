package org.threeten.extra;

import org.junit.Test;
import java.time.Month;
import java.time.YearMonth;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link DayOfMonth}.
 */
public class DayOfMonthTest {

    @Test
    public void isValidYearMonth_whenDayExistsInMonth_returnsTrue() {
        // Arrange: A day of the month that is valid for any month (e.g., the 14th).
        DayOfMonth dayOfMonth = DayOfMonth.of(14);
        YearMonth anyMonth = YearMonth.of(2023, Month.APRIL);

        // Act: Check if the day is valid for the given year-month.
        boolean isValid = dayOfMonth.isValidYearMonth(anyMonth);

        // Assert: The result should be true.
        assertTrue("The 14th should be a valid day for any month.", isValid);
    }
}