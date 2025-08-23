package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Month;
import java.time.MonthDay;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DayOfMonth}.
 */
class DayOfMonthTest {

    /**
     * Tests that atMonth(int) correctly combines the day with each month of the year.
     * This test uses a day (28) that is valid for all 12 months.
     */
    @Test
    void atMonth_withValidDayForAllMonths_createsCorrectMonthDay() {
        // Arrange
        DayOfMonth dayOfMonth28 = DayOfMonth.of(28);

        // Act & Assert
        // Verify that combining with any month from 1 to 12 works correctly.
        for (int month = 1; month <= 12; month++) {
            MonthDay expectedMonthDay = MonthDay.of(month, 28);
            MonthDay actualMonthDay = dayOfMonth28.atMonth(month);
            assertEquals(expectedMonthDay, actualMonthDay, "Failed for month: " + Month.of(month));
        }
    }
}