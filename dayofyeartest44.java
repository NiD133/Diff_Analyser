package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.stream.IntStream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link DayOfYear#atYear(int)}.
 */
class DayOfYearAtYearTest {

    private static final int LEAP_YEAR = 2008;

    /**
     * Provides a stream of all 366 days for a leap year.
     *
     * @return A stream of integers from 1 to 366.
     */
    private static IntStream provider_allDaysInLeapYear() {
        // A leap year has 366 days.
        return IntStream.rangeClosed(1, 366);
    }

    @ParameterizedTest
    @MethodSource("provider_allDaysInLeapYear")
    void atYear_forEveryDayInLeapYear_returnsCorrectDate(int dayOfYearValue) {
        // Arrange: Create the DayOfYear object and determine the expected LocalDate.
        // LocalDate.ofYearDay is a reliable way to calculate the expected date.
        DayOfYear dayOfYear = DayOfYear.of(dayOfYearValue);
        LocalDate expectedDate = LocalDate.ofYearDay(LEAP_YEAR, dayOfYearValue);

        // Act: Combine the DayOfYear with a leap year.
        LocalDate actualDate = dayOfYear.atYear(LEAP_YEAR);

        // Assert: The resulting date should match the expected date.
        assertEquals(expectedDate, actualDate);
    }
}