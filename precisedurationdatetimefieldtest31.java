package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * Unit tests for {@link PreciseDurationDateTimeField}.
 */
public class PreciseDurationDateTimeFieldTest {

    //-----------------------------------------------------------------------
    // Test isLeap(long)
    //-----------------------------------------------------------------------

    @Test
    public void isLeap_shouldAlwaysReturnFalse() {
        // The isLeap() method is inherited from BaseDateTimeField and always returns false.
        // This test verifies that a PreciseDurationDateTimeField, by extension, is never a leap field.

        // Arrange
        PreciseDurationDateTimeField field = new StubPreciseDurationDateTimeField();
        long anyInstant = 123456L;

        // Act
        boolean isLeap = field.isLeap(anyInstant);

        // Assert
        assertFalse("A precise duration field should never be a leap field", isLeap);
    }

    //-----------------------------------------------------------------------
    // Test Stubs
    //-----------------------------------------------------------------------

    /**
     * A minimal stub implementation of PreciseDurationDateTimeField for testing.
     * The methods are implemented with trivial return values because their logic is
     * not relevant for the isLeap() test.
     */
    private static class StubPreciseDurationDateTimeField extends PreciseDurationDateTimeField {

        StubPreciseDurationDateTimeField() {
            // The super constructor requires a precise duration field.
            super(DateTimeFieldType.millisOfSecond(), new StubPreciseDurationField());
        }

        // These abstract methods must be implemented, but are not called by the test.
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
    }

    /**
     * A minimal stub for a precise DurationField.
     */
    private static class StubPreciseDurationField extends BaseDurationField {

        StubPreciseDurationField() {
            super(DurationFieldType.millis());
        }

        // The following methods are abstract and must be implemented with trivial values.
        @Override
        public boolean isPrecise() {
            return true;
        }

        @Override
        public long getUnitMillis() {
            return 1; // Must be >= 1 for the PreciseDurationDateTimeField constructor
        }

        @Override
        public long add(long instant, int value) {
            return 0;
        }

        @Override
        public long add(long instant, long value) {
            return 0;
        }

        @Override
        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
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
        public long getValueAsLong(long duration, long instant) {
            return 0;
        }
    }
}