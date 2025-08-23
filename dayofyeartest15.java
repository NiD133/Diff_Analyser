package org.threeten.extra;

import static java.time.temporal.ChronoField.DAY_OF_YEAR;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/**
 * This class contains tests for the {@link DayOfYear#isSupported(TemporalField)} method.
 * The original test was refactored for clarity, structure, and maintainability.
 */
public class DayOfYearTestTest15 {

    private static final DayOfYear TEST_INSTANCE = DayOfYear.of(12);

    /**
     * A custom TemporalField implementation for testing purposes.
     * Its isSupportedBy implementation delegates to the temporal's support for DAY_OF_YEAR,
     * which makes it a supported field for DayOfYear.
     */
    private static class CustomSupportedField implements TemporalField {

        public static final CustomSupportedField INSTANCE = new CustomSupportedField();

        @Override
        public boolean isSupportedBy(TemporalAccessor temporal) {
            return temporal.isSupported(DAY_OF_YEAR);
        }

        // --- Other methods required by the TemporalField interface ---

        @Override
        public TemporalUnit getBaseUnit() {
            return ChronoUnit.DAYS;
        }

        @Override
        public TemporalUnit getRangeUnit() {
            return ChronoUnit.YEARS;
        }

        @Override
        public ValueRange range() {
            return ValueRange.of(1, 365, 366);
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
        public ValueRange rangeRefinedBy(TemporalAccessor temporal) {
            return range();
        }

        @Override
        public long getFrom(TemporalAccessor temporal) {
            return temporal.getLong(DAY_OF_YEAR);
        }

        @Override
        @SuppressWarnings("unchecked")
        public <R extends Temporal> R adjustInto(R temporal, long newValue) {
            return (R) temporal.with(DAY_OF_YEAR, newValue);
        }
    }

    @Nested
    @DisplayName("isSupported(TemporalField)")
    class IsSupported {

        @Test
        @DisplayName("should return true for the DAY_OF_YEAR field")
        void shouldReturnTrueForDayOfYearField() {
            assertTrue(TEST_INSTANCE.isSupported(DAY_OF_YEAR));
        }

        @Test
        @DisplayName("should return true for a custom field that is designed to be supported")
        void shouldReturnTrueForCustomSupportedField() {
            assertTrue(TEST_INSTANCE.isSupported(CustomSupportedField.INSTANCE));
        }

        @Test
        @DisplayName("should return false when the field is null")
        void shouldReturnFalseForNullField() {
            assertFalse(TEST_INSTANCE.isSupported((TemporalField) null));
        }

        @ParameterizedTest
        @EnumSource(
            value = ChronoField.class,
            names = "DAY_OF_YEAR",
            mode = EnumSource.Mode.EXCLUDE
        )
        @DisplayName("should return false for all other ChronoFields")
        void shouldReturnFalseForUnsupportedChronoFields(ChronoField field) {
            assertFalse(TEST_INSTANCE.isSupported(field), "Should not support " + field);
        }
    }
}