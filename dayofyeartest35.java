package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.Year;
import java.util.stream.IntStream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the behavior of {@link DayOfYear#adjustInto(Temporal)}.
 */
class DayOfYearAdjustIntoTest {

    private static final Year LEAP_YEAR = Year.of(2008);
    private static final int LEAP_YEAR_LENGTH = 366;

    /**
     * Provides a stream of all possible day-of-year values for a leap year (1 to 366).
     * @return An IntStream of days.
     */
    private static IntStream provider_allDaysInLeapYear() {
        return IntStream.rangeClosed(1, LEAP_YEAR_LENGTH);
    }

    /**
     * Verifies that adjustInto correctly sets the day-of-year on a date that is within a leap year.
     * This test covers every possible day of a leap year. The base date is intentionally set
     * to the end of the year to confirm that the adjustment works correctly regardless of
     * whether the new day is before or after the original day.
     *
     * @param day The day-of-year to test, provided by the MethodSource.
     */
    @ParameterizedTest
    @MethodSource("provider_allDaysInLeapYear")
    void adjustInto_whenTargetIsLeapYear_returnsCorrectDate(int day) {
        // Arrange
        DayOfYear dayOfYearToAdjustTo = DayOfYear.of(day);
        LocalDate baseDateInLeapYear = LocalDate.of(LEAP_YEAR.getValue(), 12, 31);
        LocalDate expectedDate = LocalDate.ofYearDay(LEAP_YEAR.getValue(), day);

        // Act
        LocalDate adjustedDate = dayOfYearToAdjustTo.adjustInto(baseDateInLeapYear);

        // Assert
        assertEquals(expectedDate, adjustedDate);
    }
}