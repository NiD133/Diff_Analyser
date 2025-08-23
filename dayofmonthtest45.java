package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.YearMonth;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Test class for {@link DayOfMonth}.
 */
public class DayOfMonthTest {

    @DisplayName("atYearMonth() with day 31 should create a valid date, adjusting to the last day of shorter months")
    @ParameterizedTest(name = "for {0}-{1}, the resulting date should be {0}-{1}-{2}")
    @CsvSource({
            // Months with 31 days
            "2012,  1, 31", // January
            "2012,  3, 31", // March
            "2012,  5, 31", // May
            "2012,  7, 31", // July
            "2012,  8, 31", // August
            "2012, 10, 31", // October
            "2012, 12, 31", // December
            // Months with 30 days (adjusted from 31)
            "2012,  4, 30", // April
            "2012,  6, 30", // June
            "2012,  9, 30", // September
            "2012, 11, 30", // November
            // February in a leap year (adjusted from 31)
            "2012,  2, 29",
            // February in a non-leap year (adjusted from 31)
            "2011,  2, 28"
    })
    void atYearMonth_withDay31_adjustsForShorterMonths(int year, int month, int expectedDay) {
        // Arrange
        DayOfMonth dayOfMonth31 = DayOfMonth.of(31);
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate expectedDate = LocalDate.of(year, month, expectedDay);

        // Act
        LocalDate actualDate = dayOfMonth31.atYearMonth(yearMonth);

        // Assert
        assertEquals(expectedDate, actualDate);
    }
}