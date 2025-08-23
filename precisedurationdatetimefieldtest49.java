package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the remainder() method in PreciseDurationDateTimeField.
 * The remainder is calculated based on the field's unit size (iUnitMillis).
 */
public class PreciseDurationDateTimeFieldRemainderTest {

    /**
     * A minimal concrete implementation of PreciseDurationDateTimeField for testing.
     * The remainder() method is inherited from BaseDateTimeField and its behavior
     * depends on the unit millis of the provided duration field.
     */
    private static class TestDateTimeField extends PreciseDurationDateTimeField {

        /**
         * @param unit The duration field that defines the unit size for this field.
         */
        protected TestDateTimeField(DurationField unit) {
            super(DateTimeFieldType.millisOfSecond(), unit);
        }

        // The following methods are abstract and must be implemented, but they are
        // not called by the remainder() method, so they can have dummy implementations.
        @Override
        public int get(long instant) {
            throw new UnsupportedOperationException();
        }

        @Override
        public DurationField getRangeDurationField() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getMaximumValue() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * A stub for DurationField that provides a precise, fixed-size unit.
     */
    private static class StubPreciseDurationField extends BaseDurationField {
        private final long unitMillis;

        StubPreciseDurationField(long unitMillis) {
            super(DurationFieldType.millis());
            this.unitMillis = unitMillis;
        }

        @Override
        public boolean isPrecise() {
            return true; // Required by the PreciseDurationDateTimeField constructor.
        }

        @Override
        public long getUnitMillis() {
            return this.unitMillis;
        }

        // Unused methods required by the abstract parent class.
        @Override public long getValueAsLong(long duration, long instant) { throw new UnsupportedOperationException(); }
        @Override public long getMillis(int value, long instant) { throw new UnsupportedOperationException(); }
        @Override public long getMillis(long value, long instant) { throw new UnsupportedOperationException(); }
        @Override public long add(long instant, int value) { throw new UnsupportedOperationException(); }
        @Override public long add(long instant, long value) { throw new UnsupportedOperationException(); }
        @Override public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) { throw new UnsupportedOperationException(); }
    }

    @Test
    public void remainderShouldReturnTheRestOfDivisionByUnitMillis() {
        // The remainder() method should behave like the modulo operator (instant % unitMillis)
        // for non-negative instants.

        // GIVEN a date-time field with a unit size of 60 milliseconds.
        final long unitMillis = 60L;
        BaseDateTimeField field = new TestDateTimeField(new StubPreciseDurationField(unitMillis));

        // WHEN calling remainder() with various instants,
        // THEN the result is the instant modulo the unit size.

        // An instant of 0 should have a remainder of 0.
        assertEquals(0L, field.remainder(0L));

        // Instants smaller than the unit size should return themselves.
        assertEquals(29L, field.remainder(29L));
        assertEquals(30L, field.remainder(30L));
        assertEquals(31L, field.remainder(31L));

        // An instant equal to the unit size should have a remainder of 0.
        assertEquals(0L, field.remainder(60L));

        // An instant larger than the unit size should have a non-zero remainder.
        assertEquals(1L, field.remainder(61L));
    }
}