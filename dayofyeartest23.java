package org.threeten.extra;

import static java.time.temporal.ChronoField.DAY_OF_YEAR;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;
import org.junit.jupiter.api.Test;

/**
 * Tests for the behavior of {@link DayOfYear}.
 * This class focuses on interactions with {@link TemporalField}.
 */
public class DayOfYearTest {

    /**
     * A test-specific implementation of {@link TemporalField} that delegates its
     * logic to the standard {@code DAY_OF_YEAR} field.
     * <p>
     * This is used to verify that {@code DayOfYear} correctly interacts with custom
     * field implementations via the {@code TemporalAccessor} interface.
     */
    private static class DelegatingDayOfYearField implements TemporalField {

        public static final DelegatingDayOfYearField INSTANCE = new DelegatingDayOfYearField();

        private DelegatingDayOfYearField() {
            // Prevent external instantiation
        }

        @Override
        public long getFrom(TemporalAccessor temporal) {
            // The core logic: delegate the query to the underlying DAY_OF_YEAR field.
            return temporal.getLong(DAY_OF_YEAR);
        }

        @Override
        public boolean isSupportedBy(TemporalAccessor temporal) {
            // This custom field is supported if the temporal object supports DAY_OF_YEAR.
            return temporal.isSupported(DAY_OF_YEAR);
        }

        @Override
        @SuppressWarnings("unchecked")
        public <R extends Temporal> R adjustInto(R temporal, long newValue) {
            // Delegate the adjustment to the underlying DAY_OF_YEAR field.
            return (R) temporal.with(DAY_OF_YEAR, newValue);
        }

        //<editor-fold desc="Boilerplate TemporalField methods">
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
            return DAY_OF_YEAR.range();
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
            return temporal.range(DAY_OF_YEAR);
        }
        //</editor-fold>
    }

    @Test
    void getLong_shouldReturnCorrectValue_forCustomDelegatingField() {
        // This test ensures that DayOfYear.getLong(TemporalField) correctly calls
        // the getFrom(TemporalAccessor) method on a custom TemporalField implementation.

        // Arrange
        int dayValue = 12;
        DayOfYear dayOfYear = DayOfYear.of(dayValue);
        TemporalField customField = DelegatingDayOfYearField.INSTANCE;

        // Act
        long actualValue = dayOfYear.getLong(customField);

        // Assert
        assertEquals(dayValue, actualValue, "The value from the custom field should match the DayOfYear's value.");
    }
}