package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.ValueRange;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Test class for {@link DayOfMonth}.
 * This refactored snippet focuses on the test for the {@code now(Clock)} method.
 */
public class DayOfMonthTestTest52 {

    private static final ZoneId PARIS = ZoneId.of("Europe/Paris");

    // This nested class is not used by the refactored test below but is kept
    // on the assumption it is used by other tests in the original full test suite.
    private static class TestingField implements TemporalField {
        public static final TestingField INSTANCE = new TestingField();

        @Override
        public TemporalUnit getBaseUnit() {
            return ChronoUnit.DAYS;
        }

        @Override
        public TemporalUnit getRangeUnit() {
            return ChronoUnit.MONTHS;
        }

        @Override
        public ValueRange range() {
            return ValueRange.of(1, 28, 31);
        }

        @Override
        public boolean isDateBased() {
            return true;
        }

        @Override
        public boolean isTimeBased() {
            return false;
        }

        @Override
        public boolean isSupportedBy(TemporalAccessor temporal) {
            return temporal.isSupported(java.time.temporal.ChronoField.DAY_OF_MONTH);
        }

        @Override
        public ValueRange rangeRefinedBy(TemporalAccessor temporal) {
            return range();
        }

        @Override
        public long getFrom(TemporalAccessor temporal) {
            return temporal.getLong(java.time.temporal.ChronoField.DAY_OF_MONTH);
        }

        @Override
        @SuppressWarnings("unchecked")
        public <R extends Temporal> R adjustInto(R temporal, long newValue) {
            return (R) temporal.with(java.time.temporal.ChronoField.DAY_OF_MONTH, newValue);
        }
    }

    //-----------------------------------------------------------------------

    /**
     * Provides a stream of all possible day-of-month values (1 to 31) for the parameterized test.
     * @return a stream of integers from 1 to 31.
     */
    private static IntStream provider_all_days_of_month() {
        return IntStream.rangeClosed(1, 31);
    }

    @ParameterizedTest
    @MethodSource("provider_all_days_of_month")
    @DisplayName("DayOfMonth.now(clock) returns the day from the provided clock")
    void now_withClock_returnsDayFromClock(int dayOfMonth) {
        // Arrange: Create a fixed clock for a specific day.
        // January is used as it has 31 days, ensuring the date is always valid for the test range.
        LocalDate date = LocalDate.of(2008, Month.JANUARY, dayOfMonth);
        Instant instant = date.atStartOfDay(PARIS).toInstant();
        Clock clock = Clock.fixed(instant, PARIS);

        DayOfMonth expectedDayOfMonth = DayOfMonth.of(dayOfMonth);

        // Act: Get the DayOfMonth from the clock.
        DayOfMonth actualDayOfMonth = DayOfMonth.now(clock);

        // Assert: The result should match the day set in the clock.
        assertEquals(expectedDayOfMonth, actualDayOfMonth);
    }
}