package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link DayOfYear#now(Clock)}.
 */
public class DayOfYearTestTest51 {

    private static final ZoneId PARIS = ZoneId.of("Europe/Paris");
    private static final int LEAP_YEAR = 2008;

    /**
     * Provides a stream of every date within a single leap year.
     * This stream is used as the source for the parameterized test.
     *
     * @return A stream of {@link LocalDate} objects for the year 2008.
     */
    private static Stream<LocalDate> leapYearDateProvider() {
        LocalDate startOfLeapYear = LocalDate.of(LEAP_YEAR, 1, 1);
        LocalDate startOfNextYear = startOfLeapYear.plusYears(1);
        return startOfLeapYear.datesUntil(startOfNextYear);
    }

    /**
     * Verifies that DayOfYear.now(clock) correctly determines the day-of-year
     * for any given date within a leap year.
     *
     * @param date The specific date in the leap year to be tested.
     */
    @ParameterizedTest(name = "for {0}, day-of-year should be {0, dayOfYear}")
    @MethodSource("leapYearDateProvider")
    void nowWithClock_returnsCorrectDayOfYear_forEveryDayInLeapYear(LocalDate date) {
        // Arrange: Create a clock fixed to the start of the given date in the Paris time zone.
        int expectedDayOfYear = date.getDayOfYear();
        Instant instant = date.atStartOfDay(PARIS).toInstant();
        Clock clock = Clock.fixed(instant, PARIS);

        // Act: Obtain the DayOfYear from the fixed clock.
        DayOfYear actualDayOfYear = DayOfYear.now(clock);

        // Assert: The obtained DayOfYear's value should match the expected day of the year.
        assertEquals(expectedDayOfYear, actualDayOfYear.getValue());
    }
}