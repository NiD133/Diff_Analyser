package org.threeten.extra;

import org.junit.Test;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the atYearMonth() method in the DayOfMonth class.
 */
public class DayOfMonthAtYearMonthTest {

    @Test
    public void atYearMonth_shouldCombineDayAndYearMonthIntoCorrectLocalDate() {
        // Arrange: Create a specific day-of-month and a year-month.
        DayOfMonth dayOfMonth = DayOfMonth.of(15);
        YearMonth yearMonth = YearMonth.of(2023, Month.AUGUST);
        LocalDate expectedDate = LocalDate.of(2023, Month.AUGUST, 15);

        // Act: Combine the day-of-month with the year-month.
        LocalDate actualDate = dayOfMonth.atYearMonth(yearMonth);

        // Assert: The resulting LocalDate should match the expected date.
        assertEquals(expectedDate, actualDate);
    }

    /**
     * The Javadoc for atYearMonth states that if the day is invalid for the
     * given month, the date is adjusted to the last valid day of that month.
     * This test verifies that behavior.
     */
    @Test
    public void atYearMonth_whenDayIsInvalidForMonth_shouldAdjustToLastValidDayOfMonth() {
        // Arrange: Day 31 is invalid for April, which has 30 days.
        DayOfMonth dayOfMonth = DayOfMonth.of(31);
        YearMonth april2023 = YearMonth.of(2023, Month.APRIL);
        LocalDate expectedDate = LocalDate.of(2023, Month.APRIL, 30);

        // Act
        LocalDate actualDate = dayOfMonth.atYearMonth(april2023);

        // Assert: The result should be adjusted to the last day of April.
        assertEquals(expectedDate, actualDate);
    }
}