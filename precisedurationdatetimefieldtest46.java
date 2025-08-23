package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the rounding behavior of PreciseDurationDateTimeField.
 */
public class PreciseDurationDateTimeFieldTest {

    /**
     * A mock implementation of PreciseDurationDateTimeField for testing.
     * This field operates with a fixed unit duration of 60 milliseconds.
     */
    private static class TestDateTimeField extends PreciseDurationDateTimeField {

        protected TestDateTimeField() {
            // The super constructor requires a precise duration field.
            super(DateTimeFieldType.millisOfSecond(), new SixtyMillisDurationField());
        }

        // The following methods are not used by the roundHalfFloor test,
        // but are required to create a concrete class instance.
        @Override
        public int get(long instant) {
            return 0; // Not relevant for this test
        }

        @Override
        public DurationField getRangeDurationField() {
            return null; // Not relevant for this test
        }

        @Override
        public int getMaximumValue() {
            return 0; // Not relevant for this test
        }
    }

    /**
     * A minimal, precise duration field with a unit of 60 milliseconds.
     */
    private static class SixtyMillisDurationField extends BaseDurationField {

        private static final long UNIT_MILLIS = 60L;

        protected SixtyMillisDurationField() {
            super(DurationFieldType.millis());
        }

        @Override
        public boolean isPrecise() {
            return true;
        }

        @Override
        public long getUnitMillis() {
            return UNIT_MILLIS;
        }

        // The following methods are unused but required by the abstract parent class.
        @Override
        public long getValueAsLong(long duration, long instant) {
            throw new UnsupportedOperationException();
        }

        @Override
        public long getMillis(int value, long instant) {
            throw new UnsupportedOperationException();
        }

        @Override
        public long getMillis(long value, long instant) {
            throw new UnsupportedOperationException();
        }

        @Override
        public long add(long instant, int value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public long add(long instant, long value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            throw new UnsupportedOperationException();
        }
    }

    //-----------------------------------------------------------------------

    @Test
    public void roundHalfFloorShouldRoundDownAtHalfwayPoint() {
        // Arrange
        BaseDateTimeField field = new TestDateTimeField();
        long unitMillis = field.getDurationField().getUnitMillis(); // 60ms
        long halfUnit = unitMillis / 2; // 30ms

        // Assert: roundHalfFloor rounds to the nearest unit value.
        // When an instant is exactly halfway between two units, it rounds down (towards negative infinity).

        // Test values around the 0ms mark
        assertEquals("Rounding 0ms", 0L, field.roundHalfFloor(0L));
        assertEquals("Rounding just below halfway", 0L, field.roundHalfFloor(halfUnit - 1)); // 29ms -> 0ms
        assertEquals("Rounding exactly at halfway", 0L, field.roundHalfFloor(halfUnit));     // 30ms -> 0ms (rounds down)
        assertEquals("Rounding just above halfway", unitMillis, field.roundHalfFloor(halfUnit + 1)); // 31ms -> 60ms

        // Test value at the next unit mark
        assertEquals("Rounding at the unit boundary", unitMillis, field.roundHalfFloor(unitMillis));
    }
}