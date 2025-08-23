package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link PreciseDurationDateTimeField}.
 * This focuses on the behavior of the set(long, int) method.
 */
public class PreciseDurationDateTimeFieldTest {

    //region Mocks for Testing

    /**
     * A concrete implementation of the abstract class under test.
     * <p>
     * This mock is configured with a unit duration of 60 milliseconds.
     * Its {@code get(instant)} method is defined as {@code instant / 60},
     * simulating a field like 'second of minute' where the "second" unit is 60ms long.
     */
    private static class MockDateTimeField extends PreciseDurationDateTimeField {

        MockDateTimeField() {
            super(DateTimeFieldType.secondOfMinute(), new SixtyMillisDurationField());
        }

        @Override
        public int get(long instant) {
            // Simple implementation for testing: value is the instant divided by the unit millis.
            return (int) (instant / getUnitMillis());
        }

        @Override
        public DurationField getRangeDurationField() {
            // A dummy range field is sufficient for this test's scope.
            return new SixtyMillisDurationField();
        }

        @Override
        public int getMaximumValue() {
            return 59;
        }
    }

    /**
     * A stateless, precise duration field with a fixed unit of 60 milliseconds.
     * This replaces the original stateful MockCountingDurationField.
     */
    private static class SixtyMillisDurationField extends BaseDurationField {

        SixtyMillisDurationField() {
            super(DurationFieldType.seconds());
        }

        @Override
        public boolean isPrecise() {
            return true;
        }

        @Override
        public long getUnitMillis() {
            return 60L;
        }

        // Unused methods for this test can be left as-is or return default values.
        @Override
        public long getValueAsLong(long duration, long instant) { return 0; }
        @Override
        public long getMillis(int value, long instant) { return 0; }
        @Override
        public long getMillis(long value, long instant) { return 0; }
        @Override
        public long add(long instant, int value) { return instant + (value * 60L); }
        @Override
        public long add(long instant, long value) { return instant + (value * 60L); }
        @Override
        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) { return 0; }
    }

    //endregion

    @Test
    public void set_whenNewValueIsLower_updatesInstantCorrectly() {
        // Arrange
        BaseDateTimeField field = new MockDateTimeField();
        long initialInstant = 120L;
        int newValueToSet = 0;

        // The mock field's current value at 120ms is get(120) = 120 / 60 = 2.
        // The set() method adjusts the instant based on the change in value.
        // Change in value: 0 (new) - 2 (current) = -2 units.
        // Change in millis: -2 units * 60 ms/unit = -120ms.
        // Expected instant: 120ms (initial) - 120ms (change) = 0ms.
        long expectedInstant = 0L;

        // Act
        long actualInstant = field.set(initialInstant, newValueToSet);

        // Assert
        assertEquals(expectedInstant, actualInstant);
    }

    @Test
    public void set_whenNewValueIsHigher_updatesInstantCorrectly() {
        // Arrange
        BaseDateTimeField field = new MockDateTimeField();
        long initialInstant = 120L;
        int newValueToSet = 29;

        // The mock field's current value at 120ms is get(120) = 120 / 60 = 2.
        // The set() method adjusts the instant based on the change in value.
        // Change in value: 29 (new) - 2 (current) = 27 units.
        // Change in millis: 27 units * 60 ms/unit = 1620ms.
        // Expected instant: 120ms (initial) + 1620ms (change) = 1740ms.
        long expectedInstant = 1740L;

        // Act
        long actualInstant = field.set(initialInstant, newValueToSet);

        // Assert
        assertEquals(expectedInstant, actualInstant);
    }
}