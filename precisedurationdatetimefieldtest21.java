package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the addWrapField method in BaseDateTimeField, as tested through
 * a concrete implementation, PreciseDurationDateTimeField.
 *
 * The test uses a mock DateTimeField that represents a "second of minute" field
 * where each "second" unit is 60 milliseconds long, and the field's value ranges from 0 to 59.
 */
public class PreciseDurationDateTimeFieldTest {

    /**
     * A mock implementation of PreciseDurationDateTimeField for testing.
     * <p>
     * This field models a "second" that is 60ms long. Its value is derived by
     * dividing the instant by 60, and it has a valid range of 0 to 59.
     */
    private static class MockSecondOfMinuteField extends PreciseDurationDateTimeField {

        private static final long UNIT_MILLIS = 60L;
        private static final int MIN_VALUE = 0;
        private static final int MAX_VALUE = 59;

        MockSecondOfMinuteField() {
            super(DateTimeFieldType.secondOfMinute(), new MockSecondsDurationField());
        }

        /**
         * The value of the field is the number of 60ms units in the instant.
         */
        @Override
        public int get(long instant) {
            return (int) (instant / UNIT_MILLIS);
        }

        /**
         * The `addWrapField` method, inherited from BaseDateTimeField, relies on `set`.
         * This implementation correctly sets the field's value on an instant.
         */
        @Override
        public long set(long instant, int value) {
            FieldUtils.verifyValueBounds(this, value, getMinimumValue(), getMaximumValue());
            // This formula replaces the old value with the new one, preserving any remainder.
            return instant - (long) get(instant) * iUnitMillis + (long) value * iUnitMillis;
        }

        @Override
        public int getMinimumValue() {
            return MIN_VALUE;
        }

        @Override
        public int getMaximumValue() {
            return MAX_VALUE;
        }

        // Unused methods required by the abstract parent class.
        @Override
        public DurationField getRangeDurationField() { return null; }
        @Override
        public boolean isLenient() { return false; }
        @Override
        public long roundFloor(long instant) { throw new UnsupportedOperationException(); }
        @Override
        public long roundCeiling(long instant) { throw new UnsupportedOperationException(); }
        @Override
        public long remainder(long instant) { throw new UnsupportedOperationException(); }
    }

    /**
     * A minimal, precise duration field required by the constructor of the mock field.
     */
    private static class MockSecondsDurationField extends BaseDurationField {
        protected MockSecondsDurationField() {
            super(DurationFieldType.seconds());
        }

        @Override
        public boolean isPrecise() {
            return true;
        }

        @Override
        public long getUnitMillis() {
            return MockSecondOfMinuteField.UNIT_MILLIS;
        }

        // Unused methods required by the abstract parent class.
        @Override
        public long getValueAsLong(long duration, long instant) { throw new UnsupportedOperationException(); }
        @Override
        public long getMillis(int value, long instant) { throw new UnsupportedOperationException(); }
        @Override
        public long getMillis(long value, long instant) { throw new UnsupportedOperationException(); }
        @Override
        public long add(long instant, int value) { throw new UnsupportedOperationException(); }
        @Override
        public long add(long instant, long value) { throw new UnsupportedOperationException(); }
        @Override
        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) { throw new UnsupportedOperationException(); }
    }

    private BaseDateTimeField field;
    private static final long UNIT_MILLIS = MockSecondOfMinuteField.UNIT_MILLIS;

    @Before
    public void setUp() {
        field = new MockSecondOfMinuteField();
    }

    @Test
    public void addWrapField_shouldDoNothingWhenAmountIsZero() {
        // Arrange: An instant where the field value is 29.
        long initialInstant = 29 * UNIT_MILLIS;
        int amountToAdd = 0;

        // Act
        long result = field.addWrapField(initialInstant, amountToAdd);

        // Assert: The instant should be unchanged.
        long expectedInstant = 29 * UNIT_MILLIS;
        assertEquals(expectedInstant, result);
    }

    @Test
    public void addWrapField_shouldAddAmountWithoutWrapping() {
        // Arrange: An instant with field value 29. Adding 30 results in 59, the max value.
        long initialInstant = 29 * UNIT_MILLIS;
        int amountToAdd = 30;

        // Act
        long result = field.addWrapField(initialInstant, amountToAdd);

        // Assert: The new instant corresponds to a field value of 59.
        long expectedInstant = 59 * UNIT_MILLIS;
        assertEquals(expectedInstant, result);
    }

    @Test
    public void addWrapField_shouldWrapAroundWhenSumExceedsMaximum() {
        // Arrange: An instant with field value 29. Adding 31 results in 60, which should wrap to 0.
        long initialInstant = 29 * UNIT_MILLIS;
        int amountToAdd = 31;

        // Act
        long result = field.addWrapField(initialInstant, amountToAdd);

        // Assert: The new instant corresponds to a field value of 0 (the minimum).
        long expectedInstant = 0 * UNIT_MILLIS;
        assertEquals(expectedInstant, result);
    }
}