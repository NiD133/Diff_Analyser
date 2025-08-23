package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneId;
import java.util.stream.IntStream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Test class for {@link DayOfYear}.
 */
public class DayOfYearTest {

    private static final Year STANDARD_YEAR = Year.of(2007);
    private static final int STANDARD_YEAR_LENGTH = 365;
    private static final ZoneId PARIS = ZoneId.of("Europe/Paris");

    /**
     * Provides a stream of integers representing each day of a standard (non-leap) year.
     *
     * @return A stream of integers from 1 to 365.
     */
    private static IntStream provideDaysForStandardYear() {
        return IntStream.rangeClosed(1, STANDARD_YEAR_LENGTH);
    }

    /**
     * Tests that DayOfYear.now(clock) correctly identifies the day of the year
     * for every day in a standard (non-leap) year.
     *
     * @param dayOfYear The day of the year to test, provided by the MethodSource.
     */
    @ParameterizedTest
    @MethodSource("provideDaysForStandardYear")
    void nowWithClock_returnsCorrectDayOfYear_forEveryDayInStandardYear(int dayOfYear) {
        // Arrange: Create a fixed clock for a specific day in a standard year.
        LocalDate date = STANDARD_YEAR.atDay(dayOfYear);
        Instant instant = date.atStartOfDay(PARIS).toInstant();
        Clock fixedClock = Clock.fixed(instant, PARIS);

        // Act: Call the method under test.
        DayOfYear actualDayOfYear = DayOfYear.now(fixedClock);

        // Assert: The result should match the expected day of the year.
        assertEquals(dayOfYear, actualDayOfYear.getValue());
    }
}