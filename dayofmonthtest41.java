package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Month;
import java.time.MonthDay;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link DayOfMonth#atMonth(int)}.
 */
class DayOfMonthTest {

    /**
     * Provides test cases for each month of the year, with the expected day.
     * The `DayOfMonth.of(31)` should adjust down for months with fewer than 31 days.
     */
    private static Stream<Arguments> provider_atMonth_adjustsForShortMonths() {
        return Stream.of(
            Arguments.of(Month.JANUARY, 31),
            Arguments.of(Month.FEBRUARY, 29), // Adjusts to the last valid day (29 in a leap year context)
            Arguments.of(Month.MARCH, 31),
            Arguments.of(Month.APRIL, 30),   // Adjusts to 30
            Arguments.of(Month.MAY, 31),
            Arguments.of(Month.JUNE, 30),    // Adjusts to 30
            Arguments.of(Month.JULY, 31),
            Arguments.of(Month.AUGUST, 31),
            Arguments.of(Month.SEPTEMBER, 30), // Adjusts to 30
            Arguments.of(Month.OCTOBER, 31),
            Arguments.of(Month.NOVEMBER, 30), // Adjusts to 30
            Arguments.of(Month.DECEMBER, 31)
        );
    }

    @DisplayName("atMonth(int) with day 31 adjusts to the last day of shorter months")
    @ParameterizedTest(name = "for {0}, the result should be day {1}")
    @MethodSource("provider_atMonth_adjustsForShortMonths")
    void atMonth_whenDayIs31_adjustsToLastDayOfShorterMonths(Month month, int expectedDay) {
        // The DayOfMonth being tested is the 31st.
        DayOfMonth day31 = DayOfMonth.of(31);

        // The method under test combines the 31st with the given month.
        // It is expected to adjust the day downward for months with fewer than 31 days.
        MonthDay actualMonthDay = day31.atMonth(month.getValue());

        // The expected MonthDay is formed from the month and the expected (potentially adjusted) day.
        MonthDay expectedMonthDay = MonthDay.of(month, expectedDay);

        assertEquals(expectedMonthDay, actualMonthDay);
    }
}