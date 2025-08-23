package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.Year;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link DayOfYear#adjustInto(Temporal)}.
 */
class DayOfYearAdjustIntoTest {

    private static final Year NON_LEAP_YEAR = Year.of(2007);

    // An arbitrary date within the non-leap year, used as the base for adjustment.
    // The specific day doesn't matter, as adjustInto() should overwrite it.
    private static final LocalDate A_DATE_IN_NON_LEAP_YEAR = NON_LEAP_YEAR.atDay(100); // 2007-04-10

    /**
     * Provides test cases for adjusting a date in a non-leap year.
     * @return A stream of arguments, each containing a day-of-year value and the expected LocalDate result.
     */
    private static Stream<Arguments> nonLeapYearAdjustmentCases() {
        return Stream.of(
            // Case 1: First day of the year
            Arguments.of(1, NON_LEAP_YEAR.atDay(1)),   // Jan 1st
            // Case 2: The day before where a leap day would be in a leap year
            Arguments.of(59, NON_LEAP_YEAR.atDay(59)),  // Feb 28th
            // Case 3: The day immediately after the "skipped" leap day
            Arguments.of(60, NON_LEAP_YEAR.atDay(60)),  // Mar 1st
            // Case 4: Last day of the year
            Arguments.of(365, NON_LEAP_YEAR.atDay(365)) // Dec 31st
        );
    }

    @ParameterizedTest(name = "Day {0} in a non-leap year should adjust date to {1}")
    @MethodSource("nonLeapYearAdjustmentCases")
    void adjustInto_shouldSetCorrectDayInNonLeapYear(int dayOfYearValue, LocalDate expectedDate) {
        // Arrange
        DayOfYear dayOfYearToAdjustTo = DayOfYear.of(dayOfYearValue);

        // Act
        LocalDate adjustedDate = dayOfYearToAdjustTo.adjustInto(A_DATE_IN_NON_LEAP_YEAR);

        // Assert
        assertEquals(expectedDate, adjustedDate);
    }
}