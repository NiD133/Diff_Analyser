package org.joda.time.field;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;

/**
 * Unit tests for the rounding behavior of PreciseDurationDateTimeField.
 */
public class PreciseDurationDateTimeFieldTest {

    /**
     * A mock implementation of PreciseDurationDateTimeField for testing.
     *
     * <p>This field is configured with a fixed, precise duration unit (e.g., 60 milliseconds).
     * The methods not relevant to the rounding tests are minimally implemented, as they are not called
     * by the method under test, {@code roundFloor()}.
     */
    private static class MockPreciseDateTimeField extends PreciseDurationDateTimeField {

        private static final long UNIT_MILLIS = 60L;

        MockPreciseDateTimeField() {
            super(DateTimeFieldType.secondOfMinute(),
                  new MockDurationField(DurationFieldType.seconds(), UNIT_MILLIS));
        }

        // The following methods are abstract and must be implemented, but they are not
        // invoked by the roundFloor() method being tested.
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
     * A mock DurationField that is precise and has a fixed unit.
     */
    private static class MockDurationField extends BaseDurationField {
        private final long iUnitMillis;

        MockDurationField(DurationFieldType type, long unitMillis) {
            super(type);
            iUnitMillis = unitMillis;
        }

        @Override
        public boolean isPrecise() {
            return true;
        }

        @Override
        public long getUnitMillis() {
            return iUnitMillis;
        }

        // Stub implementations for unused abstract methods.
        @Override
        public long getValueAsLong(long duration, long instant) { return 0; }
        @Override
        public long getMillis(int value, long instant) { return 0; }
        @Override
        public long getMillis(long value, long instant) { return 0; }
        @Override
        public long add(long instant, int value) { return 0; }
        @Override
        public long add(long instant, long value) { return 0; }
        @Override
        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) { return 0; }
    }

    @Test
    public void roundFloor_shouldRoundDownToNearestMultipleOfUnit() {
        // The field under test has a unit of 60 milliseconds.
        BaseDateTimeField field = new MockPreciseDateTimeField();
        long unit = field.getDurationField().getUnitMillis(); // 60L

        // Test with values that are exact multiples of the unit
        assertEquals(0L, field.roundFloor(0L));
        assertEquals(unit, field.roundFloor(unit));
        assertEquals(-unit, field.roundFloor(-unit));

        // Test with positive values, which should round down towards zero
        assertEquals(0L, field.roundFloor(1L));
        assertEquals(0L, field.roundFloor(unit - 1)); // 59L -> 0L

        // Test with negative values, which should round down towards negative infinity
        assertEquals(-unit, field.roundFloor(-1L));
        assertEquals(-unit, field.roundFloor(-(unit - 1))); // -59L -> -60L
        assertEquals(-2 * unit, field.roundFloor(-(unit + 1))); // -61L -> -120L
    }
}