package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/**
 * Tests for {@link DayOfMonth#atYearMonth(YearMonth)}.
 */
class DayOfMonthAtYearMonthTest {

    private static final int TEST_YEAR = 2012; // Any year would work, 2012 is a leap year.

    @ParameterizedTest
    @EnumSource(Month.class) // Provides all 12 months to the test method
    @DisplayName("DayOfMonth(28) should form a correct LocalDate for any month")
    void atYearMonth_forDay28_returnsCorrectDateForAllMonths(Month month) {
        // Arrange
        DayOfMonth dayOfMonth28 = DayOfMonth.of(28);
        YearMonth yearMonth = YearMonth.of(TEST_YEAR, month);
        LocalDate expectedDate = LocalDate.of(TEST_YEAR, month, 28);

        // Act
        LocalDate actualDate = dayOfMonth28.atYearMonth(yearMonth);

        // Assert
        assertEquals(expectedDate, actualDate);
    }
}