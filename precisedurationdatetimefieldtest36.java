package org.joda.time.field;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.ReadablePartial;
import org.joda.time.TimeOfDay;
import org.junit.Test;

/**
 * Unit tests for the PreciseDurationDateTimeField class, focusing on its minimum value behavior.
 */
public class PreciseDurationDateTimeFieldTest {

    /**
     * Provides a minimal concrete implementation of PreciseDurationDateTimeField for testing.
     * This mock represents a "second of minute" field.
     */
    private static class MockSecondOfMinuteField extends PreciseDurationDateTimeField {

        protected MockSecondOfMinuteField() {
            // The super constructor requires a precise duration field.
            super(DateTimeFieldType.secondOfMinute(), new MockPreciseUnitDurationField());
        }

        // The following methods must be implemented to create a concrete class,
        // but they are not relevant to the test for getMinimumValue().

        @Override
        public int get(long instant) {
            return (int) (instant / 1000L % 60L);
        }

        @Override
        public DurationField getRangeDurationField() {
            return new MockPreciseUnitDurationField(DurationFieldType.minutes());
        }

        @Override
        public int getMaximumValue() {
            return 59;
        }
    }

    /**
     * A minimal mock of a precise duration field, required by the PreciseDurationDateTimeField constructor.
     * It represents a duration of 60 milliseconds.
     */
    private static class MockPreciseUnitDurationField extends BaseDurationField {

        protected MockPreciseUnitDurationField() {
            this(DurationFieldType.seconds());
        }

        protected MockPreciseUnitDurationField(DurationFieldType type) {
            super(type);
        }

        @Override
        public boolean isPrecise() {
            return true;
        }

        @Override
        public long getUnitMillis() {
            // Must be >= 1 for the PreciseDurationDateTimeField constructor.
            return 60;
        }

        // The following methods are not used in this test but must be implemented.
        @Override
        public long getValueAsLong(long duration, long instant) {
            return 0;
        }

        @Override
        public long getMillis(int value, long instant) {
            return 0;
        }

        @Override
        public long getMillis(long value, long instant) {
            return 0;
        }

        @Override
        public long add(long instant, int value) {
            return instant + (value * 60L);
        }

        @Override
        public long add(long instant, long value) {
            return instant + (value * 60L);
        }

        @Override
        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            return 0;
        }
    }

    //-----------------------------------------------------------------------

    @Test
    public void testGetMinimumValue_withPartial_returnsConstantMinimum() {
        // This test verifies that getMinimumValue(ReadablePartial) returns the
        // field's absolute minimum value (0), as defined in the BaseDateTimeField superclass.
        // The ReadablePartial argument is expected to be ignored for this type of field.

        // Arrange
        BaseDateTimeField field = new MockSecondOfMinuteField();
        ReadablePartial anyPartial = new TimeOfDay(); // The specific partial is not important.
        int expectedMinimumValue = 0;

        // Act
        int actualMinimumValue = field.getMinimumValue(anyPartial);

        // Assert
        assertEquals(expectedMinimumValue, actualMinimumValue);
    }
}