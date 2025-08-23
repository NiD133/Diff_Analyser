package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Month;
import java.time.MonthDay;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/**
 * Tests for {@link DayOfMonth#atMonth(Month)}.
 */
class DayOfMonthAtMonthTest {

    @DisplayName("atMonth() should create a correct MonthDay for a day valid in all months")
    @ParameterizedTest(name = "for {0}")
    @EnumSource(Month.class)
    void atMonth_whenDayIsValidForAllMonths_createsCorrectMonthDay(Month month) {
        // Day 28 is chosen because it is a valid day in every month of the year.
        int dayValue = 28;
        DayOfMonth dayOfMonth = DayOfMonth.of(dayValue);

        // Act
        MonthDay actualMonthDay = dayOfMonth.atMonth(month);

        // Assert
        MonthDay expectedMonthDay = MonthDay.of(month, dayValue);
        assertEquals(expectedMonthDay, actualMonthDay);
    }
}