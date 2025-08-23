package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests the behavior of PreciseDurationDateTimeField, specifically focusing on
 * functionality inherited from its superclass, BaseDateTimeField.
 */
public class PreciseDurationDateTimeFieldTest {

    /**
     * A mock implementation of PreciseDurationDateTimeField for testing purposes.
     * It simulates a "second of minute" field to provide a concrete class for the test.
     */
    private static class MockSecondOfMinuteField extends PreciseDurationDateTimeField {

        // The sole purpose of this mock is to provide a concrete implementation
        // of PreciseDurationDateTimeField and a fixed maximum value for the test.
        MockSecondOfMinuteField() {
            super(DateTimeFieldType.secondOfMinute(), new MockSecondsDurationField());
        }

        // This is the key method for the test. It returns a fixed value.
        @Override
        public int getMaximumValue() {
            return 59;
        }

        // The following methods are not used in this test but are required by the
        // abstract superclass. They can be ignored.
        @Override
        public int get(long instant) {
            return 0;
        }

        @Override
        public DurationField getRangeDurationField() {
            return null;
        }
    }

    /**
     * A minimal mock of a precise duration field, required by the constructor
     * of PreciseDurationDateTimeField.
     */
    private static class MockSecondsDurationField extends BaseDurationField {

        MockSecondsDurationField() {
            super(DurationFieldType.seconds());
        }

        @Override
        public boolean isPrecise() {
            return true;
        }

        @Override
        public long getUnitMillis() {
            return 1000; // Standard milliseconds for a second
        }

        // The following methods are not used in this test but are required by the
        // abstract superclass. They return dummy values.
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
    public void getMaximumValue_withInstant_delegatesToParameterlessGetMaximumValue() {
        // Arrange
        // The test targets getMaximumValue(long), which is implemented in the superclass
        // BaseDateTimeField. It is expected to delegate its call to getMaximumValue().
        BaseDateTimeField field = new MockSecondOfMinuteField();
        int expectedMaximumValue = 59; // This value is hardcoded in the mock's getMaximumValue()

        // Act
        // The specific instant value should be ignored by the method under test.
        int actualMaximumValue = field.getMaximumValue(12345L);

        // Assert
        // Verify that the result from getMaximumValue(long) is the same as the one
        // provided by the mock's getMaximumValue(), confirming the delegation.
        assertEquals(expectedMaximumValue, actualMaximumValue);
    }
}