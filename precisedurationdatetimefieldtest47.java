package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the rounding behavior of {@link PreciseDurationDateTimeField}.
 * This test focuses on the {@code roundHalfCeiling} method.
 */
public class PreciseDurationDateTimeFieldRoundingTest {

    /**
     * A mock DateTimeField that uses a fixed 60-millisecond duration field.
     * This setup is necessary to test the rounding logic, which depends on the field's unit size.
     * The methods here are required to create a concrete instance of the abstract
     * {@code PreciseDurationDateTimeField} but are not directly used by the rounding test.
     */
    private static class FieldWithSixtyMillisUnit extends PreciseDurationDateTimeField {

        FieldWithSixtyMillisUnit() {
            super(DateTimeFieldType.secondOfMinute(), new MockSixtyMillisDurationField());
        }

        // The following methods are abstract in a parent class and must be implemented.
        // They return dummy values as they are not relevant to the rounding logic being tested.
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
            return 0;
        }

        @Override
        public int getMinimumValue() {
            return 0;
        }
    }

    /**
     * A mock duration field that provides a precise unit of 60 milliseconds.
     * This is the core dependency for the rounding logic, as {@code roundHalfCeiling}
     * operates based on the value returned by {@code getUnitMillis()}.
     */
    private static class MockSixtyMillisDurationField extends BaseDurationField {

        MockSixtyMillisDurationField() {
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

        // The following methods are abstract and must be implemented, but are not
        // used by the rounding logic, so they can return dummy values.
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
    public void roundHalfCeiling_shouldRoundUpAtMidpoint() {
        // ARRANGE: Create a field where the rounding unit is 60ms.
        BaseDateTimeField field = new FieldWithSixtyMillisUnit();
        final long unitMillis = 60L;
        final long halfUnit = unitMillis / 2; // 30L

        // ACT & ASSERT

        // Test rounding for values around the midpoint (30)
        assertEquals("Rounding down: value just below midpoint should round to zero",
                0L, field.roundHalfCeiling(halfUnit - 1)); // 29 -> 0

        assertEquals("Rounding up: value at midpoint should round up to the next unit",
                unitMillis, field.roundHalfCeiling(halfUnit)); // 30 -> 60

        assertEquals("Rounding up: value just above midpoint should round up to the next unit",
                unitMillis, field.roundHalfCeiling(halfUnit + 1)); // 31 -> 60

        // Test rounding at the boundaries
        assertEquals("Rounding boundary: zero should remain zero",
                0L, field.roundHalfCeiling(0L));

        assertEquals("Rounding boundary: a value on the unit should remain on the unit",
                unitMillis, field.roundHalfCeiling(unitMillis));
    }
}