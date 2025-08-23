package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Month;
import java.time.YearMonth;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@DisplayName("DayOfMonth")
class DayOfMonthTest {

    @Nested
    @DisplayName("isValidYearMonth for day 29")
    class IsValidForDay29 {

        private static final DayOfMonth DAY_29 = DayOfMonth.of(29);
        private static final int LEAP_YEAR = 2012;
        private static final int COMMON_YEAR = 2011;

        @DisplayName("should be valid for all months in a leap year")
        @ParameterizedTest(name = "for {0}, {1}")
        @EnumSource(Month.class)
        void isAlwaysValidInLeapYear(Month month) {
            // Arrange: A YearMonth in a leap year (e.g., 2012)
            YearMonth leapYearMonth = YearMonth.of(LEAP_YEAR, month);

            // Act & Assert: Day 29 is valid for any month in a leap year.
            assertTrue(DAY_29.isValidYearMonth(leapYearMonth));
        }

        @Test
        @DisplayName("should be invalid for February in a common year")
        void isInvalidForFebruaryInCommonYear() {
            // Arrange: February in a common year (e.g., 2011)
            YearMonth commonYearFebruary = YearMonth.of(COMMON_YEAR, Month.FEBRUARY);

            // Act & Assert: Day 29 is not a valid day in February of a common year.
            assertFalse(DAY_29.isValidYearMonth(commonYearFebruary));
        }

        @Test
        @DisplayName("should be valid for March in a common year")
        void isValidForMarchInCommonYear() {
            // Arrange: March in a common year (e.g., 2011)
            YearMonth commonYearMarch = YearMonth.of(COMMON_YEAR, Month.MARCH);

            // Act & Assert: Day 29 is a valid day for months other than February.
            assertTrue(DAY_29.isValidYearMonth(commonYearMarch));
        }
    }
}