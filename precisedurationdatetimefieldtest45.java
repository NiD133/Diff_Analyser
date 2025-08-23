package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the roundCeiling() method in {@link PreciseDurationDateTimeField}.
 */
public class PreciseDurationDateTimeField_RoundCeilingTest {

    /**
     * A minimal test implementation of PreciseDurationDateTimeField.
     * The roundCeiling() method under test only depends on the iUnitMillis
     * value, so other methods can be minimally implemented.
     */
    private static class TestField extends PreciseDurationDateTimeField {

        /**
         * @param unitMillis The unit duration of this field in milliseconds.
         */
        TestField(long unitMillis) {
            // The DateTimeFieldType is arbitrary and not used by roundCeiling().
            super(DateTimeFieldType.millisOfSecond(), new StubDurationField(unitMillis));
        }

        // The following methods are abstract in the superclass but are not invoked
        // by the roundCeiling() implementation. They throw exceptions to ensure
        // the test fails if the SUT's behavior changes to depend on them.
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
     * A minimal DurationField stub used only to provide a precise unitMillis
     * to the PreciseDurationDateTimeField constructor.
     */
    private static class StubDurationField extends BaseDurationField {
        private final long iUnitMillis;

        StubDurationField(long unitMillis) {
            // The DurationFieldType is arbitrary.
            super(DurationFieldType.millis());
            this.iUnitMillis = unitMillis;
        }

        @Override
        public boolean isPrecise() {
            return true;
        }

        @Override
        public long getUnitMillis() {
            return iUnitMillis;
        }

        // Dummy implementations for other abstract methods.
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
    public void roundCeiling_roundsToNextMultipleOfUnit() {
        // GIVEN a field with a unit duration of 60 milliseconds.
        // The roundCeiling() method should round an instant to the smallest
        // multiple of 60 that is greater than or equal to the instant.
        final BaseDateTimeField field = new TestField(60L);

        // WHEN the instant is already a multiple of the unit
        // THEN it should return the instant itself.
        assertEquals(-60L, field.roundCeiling(-60L));
        assertEquals(0L, field.roundCeiling(0L));
        assertEquals(60L, field.roundCeiling(60L));

        // WHEN the instant is negative and not a multiple
        // THEN it should round up towards zero to the nearest multiple.
        assertEquals(-60L, field.roundCeiling(-61L)); // Rounds up from -61
        assertEquals(0L, field.roundCeiling(-59L));   // Rounds up from -59
        assertEquals(0L, field.roundCeiling(-1L));    // Rounds up from -1

        // WHEN the instant is positive and not a multiple
        // THEN it should round up away from zero to the nearest multiple.
        assertEquals(60L, field.roundCeiling(1L));    // Rounds up from 1
        assertEquals(60L, field.roundCeiling(30L));   // Rounds up from 30
        assertEquals(60L, field.roundCeiling(59L));   // Rounds up from 59
    }
}