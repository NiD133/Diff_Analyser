package org.threeten.extra;

import static java.time.Month.APRIL;
import static java.time.Month.AUGUST;
import static java.time.Month.DECEMBER;
import static java.time.Month.FEBRUARY;
import static java.time.Month.JANUARY;
import static java.time.Month.JULY;
import static java.time.Month.JUNE;
import static java.time.Month.MARCH;
import static java.time.Month.MAY;
import static java.time.Month.NOVEMBER;
import static java.time.Month.OCTOBER;
import static java.time.Month.SEPTEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.time.Month;
import java.time.MonthDay;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link DayOfMonth}.
 */
public class DayOfMonthTest {

    /**
     * Provides test cases for combining DayOfMonth(31) with each month.
     *
     * @return a stream of arguments, where each argument is an input Month
     *         and the expected resulting MonthDay.
     */
    private static Stream<Arguments> provider_atMonthWithDay31() {
        return Stream.of(
                // For months with 31 days, the day is unchanged
                arguments(JANUARY, MonthDay.of(JANUARY, 31)),
                arguments(MARCH, MonthDay.of(MARCH, 31)),
                arguments(MAY, MonthDay.of(MAY, 31)),
                arguments(JULY, MonthDay.of(JULY, 31)),
                arguments(AUGUST, MonthDay.of(AUGUST, 31)),
                arguments(OCTOBER, MonthDay.of(OCTOBER, 31)),
                arguments(DECEMBER, MonthDay.of(DECEMBER, 31)),

                // For months with 30 days, the day is adjusted down to 30
                arguments(APRIL, MonthDay.of(APRIL, 30)),
                arguments(JUNE, MonthDay.of(JUNE, 30)),
                arguments(SEPTEMBER, MonthDay.of(SEPTEMBER, 30)),
                arguments(NOVEMBER, MonthDay.of(NOVEMBER, 30)),

                // For February, the day is adjusted down to 29 (its maximum possible day)
                arguments(FEBRUARY, MonthDay.of(FEBRUARY, 29))
        );
    }

    @ParameterizedTest
    @MethodSource("provider_atMonthWithDay31")
    public void atMonth_whenDayIs31_adjustsToLastDayOfShorterMonths(Month month, MonthDay expectedMonthDay) {
        // Arrange
        DayOfMonth day31 = DayOfMonth.of(31);

        // Act
        MonthDay actualMonthDay = day31.atMonth(month);

        // Assert
        assertEquals(expectedMonthDay, actualMonthDay);
    }
}