package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link PreciseDurationDateTimeField}.
 * This test focuses on verifying that the addition logic is correctly delegated
 * to the underlying duration field.
 */
class PreciseDurationDateTimeFieldTest {

    /**
     * A test-specific implementation of PreciseDurationDateTimeField.
     * The method under test, `add(long, int)`, is inherited from the parent
     * {@link BaseDateTimeField}, so we only need to provide minimal implementations
     * for the abstract methods.
     */
    private static class TestPreciseDurationDateTimeField extends PreciseDurationDateTimeField {

        TestPreciseDurationDateTimeField(DateTimeFieldType type, DurationField unit) {
            super(type, unit);
        }

        // The following methods are abstract and must be implemented, but they
        // are not relevant for the test case being refactored.
        @Override
        public int get(long instant) {
            return 0;
        }

        @Override
        public DurationField getRangeDurationField() {
            return null;
        }

        @Override
        public int getMaximumValue() {
            return 59;
        }
    }

    /**
     * A mock DurationField that counts method invocations to allow for verification
     * of behavior (i.e., that a method was called).
     */
    private static class CountingDurationField extends BaseDurationField {
        private final long unitMillis;
        private int addIntCallCount = 0;

        CountingDurationField(DurationFieldType type, long unitMillis) {
            super(type);
            this.unitMillis = unitMillis;
        }

        @Override
        public long add(long instant, int value) {
            addIntCallCount++;
            return instant + (value * unitMillis);
        }

        public int getAddIntCallCount() {
            return addIntCallCount;
        }

        // --- Boilerplate for abstract parent class ---
        @Override public boolean isPrecise() { return true; }
        @Override public long getUnitMillis() { return unitMillis; }
        @Override public long getValueAsLong(long duration, long instant) { return 0; }
        @Override public long getMillis(int value, long instant) { return 0; }
        @Override public long getMillis(long value, long instant) { return 0; }
        @Override public long add(long instant, long value) { return 0; }
        @Override public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) { return 0; }
    }

    @Test
    void add_shouldDelegateToUnderlyingDurationField() {
        // Arrange
        final long testUnitMillis = 60L;
        final long initialInstant = 1L;
        final int valueToAdd = 1;

        // Create a mock duration field that we can inspect after the test action.
        CountingDurationField mockDurationField = new CountingDurationField(DurationFieldType.seconds(), testUnitMillis);
        
        // Create the field under test, injecting our mock.
        // The method being tested, add(long, int), is inherited from BaseDateTimeField.
        BaseDateTimeField field = new TestPreciseDurationDateTimeField(DateTimeFieldType.secondOfMinute(), mockDurationField);

        // Act
        long result = field.add(initialInstant, valueToAdd);

        // Assert
        // 1. Verify the calculation is correct.
        long expectedInstant = initialInstant + (valueToAdd * testUnitMillis);
        assertEquals(expectedInstant, result, "The resulting instant should be correctly calculated.");

        // 2. Verify the call was delegated to the duration field's add method.
        assertEquals(1, mockDurationField.getAddIntCallCount(), "DurationField.add(long, int) should be called exactly once.");
    }
}