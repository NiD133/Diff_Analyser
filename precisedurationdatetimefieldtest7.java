package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the abstract PreciseDurationDateTimeField class.
 *
 * <p>Since {@link PreciseDurationDateTimeField} is abstract, this test uses a concrete
 * mock implementation to verify the behavior of its subclasses.
 */
public class PreciseDurationDateTimeFieldTest {

    /**
     * A concrete implementation of {@link PreciseDurationDateTimeField} for testing.
     * <p>
     * It simulates a field where the value is derived by simple integer division
     * of the instant by the unit millis of its duration field.
     */
    private static class MockDateTimeField extends PreciseDurationDateTimeField {

        protected MockDateTimeField(FixedDurationField unit) {
            super(DateTimeFieldType.secondOfMinute(), unit);
        }

        /**
         * Returns the field value, calculated as the instant divided by the unit millis.
         */
        @Override
        public int get(long instant) {
            // This implementation is specific to the mock and is the primary logic being tested.
            return (int) (instant / getUnitMillis());
        }

        // The methods below are not used in this test but are required by the abstract superclass.
        // They return dummy values suitable for this test's scope.
        @Override
        public DurationField getRangeDurationField() {
            return null;
        }

        @Override
        public int getMaximumValue() {
            return 59; // A typical value for secondOfMinute
        }
    }

    /**
     * A mock DurationField that is precise and has a fixed, non-standard unit length.
     */
    private static class FixedDurationField extends BaseDurationField {
        private static final long MOCK_UNIT_MILLIS = 60L;

        protected FixedDurationField() {
            super(DurationFieldType.seconds());
        }

        @Override
        public boolean isPrecise() {
            return true;
        }

        @Override
        public long getUnitMillis() {
            return MOCK_UNIT_MILLIS;
        }

        // The methods below are not used in this test but are required by the abstract superclass.
        // They return dummy values.
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

    //-----------------------------------------------------------------------

    @Test
    public void testGet_calculatesValueBasedOnUnitMillis() {
        // This test verifies the get() method of our mock implementation.
        // The mock's duration field uses a unit of 60ms.
        FixedDurationField durationField = new FixedDurationField();
        BaseDateTimeField field = new MockDateTimeField(durationField);

        long unitMillis = durationField.getUnitMillis();
        assertEquals(60L, unitMillis);

        // Test cases are based on the mock's logic: value = instant / 60ms
        assertEquals(0, field.get(0L), "0ms / 60ms = 0");
        assertEquals(1, field.get(60L), "60ms / 60ms = 1");
        assertEquals(2, field.get(123L), "123ms / 60ms = 2 (integer division)");
    }
}