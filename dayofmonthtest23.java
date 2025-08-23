package org.threeten.extra;

import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.ValueRange;

import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the behavior of {@link DayOfMonth#getLong(TemporalField)} with custom field implementations.
 */
public class DayOfMonthGetLongWithCustomFieldTest {

    /**
     * A test-specific implementation of {@link TemporalField}.
     * <p>
     * This field mimics the behavior of {@code DAY_OF_MONTH}. Its purpose is to verify that
     * {@link DayOfMonth#getLong(TemporalField)} correctly delegates to the field's
     * {@code getFrom(TemporalAccessor)} method when the field is not a standard {@link java.time.temporal.ChronoField}.
     */
    private static class CustomFieldDelegatingToDayOfMonth implements TemporalField {
        public static final CustomFieldDelegatingToDayOfMonth INSTANCE = new CustomFieldDelegatingToDayOfMonth();

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
            return DAY_OF_MONTH.range();
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
            return temporal.isSupported(DAY_OF_MONTH);
        }

        @Override
        public ValueRange rangeRefinedBy(TemporalAccessor temporal) {
            return temporal.range(DAY_OF_MONTH);
        }

        @Override
        public long getFrom(TemporalAccessor temporal) {
            // This is the core delegation logic we are testing.
            return temporal.getLong(DAY_OF_MONTH);
        }

        @Override
        @SuppressWarnings("unchecked")
        public <R extends Temporal> R adjustInto(R temporal, long newValue) {
            return (R) temporal.with(DAY_OF_MONTH, newValue);
        }
    }

    @Test
    public void getLong_whenCalledWithCustomField_delegatesToField() {
        // This test verifies that DayOfMonth.getLong() correctly calls
        // field.getFrom(this) for non-standard TemporalField implementations,
        // as per the TemporalAccessor contract.

        // Arrange
        DayOfMonth dayOfMonth = DayOfMonth.of(12);
        TemporalField customField = CustomFieldDelegatingToDayOfMonth.INSTANCE;
        long expectedValue = 12L;

        // Act
        long actualValue = dayOfMonth.getLong(customField);

        // Assert
        assertEquals(expectedValue, actualValue);
    }
}