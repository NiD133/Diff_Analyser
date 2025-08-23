package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.Month;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DayOfMonth}.
 */
class DayOfMonthTest {

    /**
     * Tests that DayOfMonth.from() correctly extracts the day from a LocalDate.
     * This test iterates across month boundaries in a leap year to ensure correctness.
     */
    @Test
    void from_localDate_extractsDayOfMonthCorrectly_duringLeapYear() {
        // 2008 is a leap year, which is ideal for testing month transitions,
        // especially the 29th of February.
        LocalDate startDate = LocalDate.of(2008, Month.JANUARY, 1);
        
        // Test the first three months to cover different month lengths and the leap day.
        int daysInFirstThreeMonths = Month.JANUARY.length(true) + Month.FEBRUARY.length(true) + Month.MARCH.length(true);

        for (int i = 0; i < daysInFirstThreeMonths; i++) {
            LocalDate currentDate = startDate.plusDays(i);
            int expectedDay = currentDate.getDayOfMonth();
            DayOfMonth actualDayOfMonth = DayOfMonth.from(currentDate);

            assertEquals(expectedDay, actualDayOfMonth.getValue(),
                    () -> "Failed for date: " + currentDate);
        }
    }
}