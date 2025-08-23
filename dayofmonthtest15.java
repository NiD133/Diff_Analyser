package org.threeten.extra;

import static java.time.temporal.ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH;
import static java.time.temporal.ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR;
import static java.time.temporal.ChronoField.ALIGNED_WEEK_OF_MONTH;
import static java.time.temporal.ChronoField.ALIGNED_WEEK_OF_YEAR;
import static java.time.temporal.ChronoField.AMPM_OF_DAY;
import static java.time.temporal.ChronoField.CLOCK_HOUR_OF_AMPM;
import static java.time.temporal.ChronoField.CLOCK_HOUR_OF_DAY;
import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.DAY_OF_WEEK;
import static java.time.temporal.ChronoField.DAY_OF_YEAR;
import static java.time.temporal.ChronoField.EPOCH_DAY;
import static java.time.temporal.ChronoField.ERA;
import static java.time.temporal.ChronoField.HOUR_OF_AMPM;
import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.INSTANT_SECONDS;
import static java.time.temporal.ChronoField.MICRO_OF_DAY;
import static java.time.temporal.ChronoField.MICRO_OF_SECOND;
import static java.time.temporal.ChronoField.MILLI_OF_DAY;
import static java.time.temporal.ChronoField.MILLI_OF_SECOND;
import static java.time.temporal.ChronoField.MINUTE_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.NANO_OF_DAY;
import static java.time.temporal.ChronoField.NANO_OF_SECOND;
import static java.time.temporal.ChronoField.OFFSET_SECONDS;
import static java.time.temporal.ChronoField.PROLEPTIC_MONTH;
import static java.time.temporal.ChronoField.SECOND_OF_DAY;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;
import static java.time.temporal.ChronoField.YEAR;
import static java.time.temporal.ChronoField.YEAR_OF_ERA;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.temporal.ChronoUnit;
import java.time.temporal.IsoFields;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the isSupported(TemporalField) method of DayOfMonth.
 */
@DisplayName("DayOfMonth.isSupported(TemporalField)")
class DayOfMonthIsSupportedTest {

    private static final DayOfMonth TEST_DOM = DayOfMonth.of(12);

    @Test
    @DisplayName("should return true for DAY_OF_MONTH")
    void isSupported_forDayOfMonthField_returnsTrue() {
        assertTrue(TEST_DOM.isSupported(DAY_OF_MONTH));
    }

    @Test
    @DisplayName("should return true for custom fields that delegate to DAY_OF_MONTH")
    void isSupported_forCustomDelegatingField_returnsTrue() {
        assertTrue(TEST_DOM.isSupported(new TestingField()));
    }

    @Test
    @DisplayName("should return false for null input")
    void isSupported_forNull_returnsFalse() {
        assertFalse(TEST_DOM.isSupported((TemporalField) null));
    }

    @ParameterizedTest(name = "should return false for {0}")
    @MethodSource("unsupportedFields")
    @DisplayName("should return false for unsupported fields")
    void isSupported_forUnsupportedFields_returnsFalse(TemporalField field) {
        assertFalse(TEST_DOM.isSupported(field));
    }

    private static Stream<TemporalField> unsupportedFields() {
        return Stream.of(
            // Time-based fields
            NANO_OF_SECOND, NANO_OF_DAY, MICRO_OF_SECOND, MICRO_OF_DAY,
            MILLI_OF_SECOND, MILLI_OF_DAY, SECOND_OF_MINUTE, SECOND_OF_DAY,
            MINUTE_OF_HOUR, MINUTE_OF_DAY, HOUR_OF_AMPM, CLOCK_HOUR_OF_AMPM,
            HOUR_OF_DAY, CLOCK_HOUR_OF_DAY, AMPM_OF_DAY,

            // Other date-based fields
            DAY_OF_WEEK, DAY_OF_YEAR, ALIGNED_DAY_OF_WEEK_IN_MONTH,
            ALIGNED_DAY_OF_WEEK_IN_YEAR, ALIGNED_WEEK_OF_MONTH, ALIGNED_WEEK_OF_YEAR,
            EPOCH_DAY, MONTH_OF_YEAR, PROLEPTIC_MONTH, YEAR_OF_ERA, YEAR, ERA,
            IsoFields.DAY_OF_QUARTER,

            // Other fields
            INSTANT_SECONDS, OFFSET_SECONDS
        );
    }

    /**
     * A test-only TemporalField that delegates its isSupportedBy check to DAY_OF_MONTH,
     * mimicking the behavior described in the DayOfMonth.isSupported Javadoc.
     */
    private static class TestingField implements TemporalField {
        @Override
        public boolean isSupportedBy(TemporalAccessor temporal) {
            return temporal.isSupported(DAY_OF_MONTH);
        }
        
        // Other methods are not relevant for this test and are omitted for brevity.
        @Override public TemporalUnit getBaseUnit() { return ChronoUnit.DAYS; }
        @Override public TemporalUnit getRangeUnit() { return ChronoUnit.MONTHS; }
        @Override public ValueRange range() { return ValueRange.of(1, 28, 31); }
        @Override public boolean isDateBased() { return true; }
        @Override public boolean isTimeBased() { return false; }
        @Override public ValueRange rangeRefinedBy(TemporalAccessor temporal) { return range(); }
        @Override public long getFrom(TemporalAccessor temporal) { return temporal.getLong(DAY_OF_MONTH); }
        @Override public <R extends Temporal> R adjustInto(R temporal, long newValue) { return (R) temporal.with(DAY_OF_MONTH, newValue); }
    }
}