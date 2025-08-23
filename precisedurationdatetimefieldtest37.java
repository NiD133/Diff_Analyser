package org.joda.time.field;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.TimeOfDay;
import org.junit.Test;

/**
 * Tests for the {@link PreciseDurationDateTimeField} class, focusing on its base functionality.
 */
public class PreciseDurationDateTimeFieldTest {

    //-----------------------------------------------------------------------
    // Mock Implementations for testing
    //-----------------------------------------------------------------------

    /**
     * A concrete implementation of PreciseDurationDateTimeField for testing purposes.
     * It mocks a "second of minute" field.
     */
    private static class MockPreciseDurationDateTimeField extends PreciseDurationDateTimeField {

        protected MockPreciseDurationDateTimeField() {
            super(DateTimeFieldType.secondOfMinute(), new MockSecondsDurationField());
        }

        // The following methods are required by the abstract parent class but are not
        // relevant for the test case in this file.
        @Override
        public int get(long instant) {
            return (int) (instant / 60L);
        }

        @Override
        public DurationField getRangeDurationField() {
            return new MockMinutesDurationField();
        }

        @Override
        public int getMaximumValue() {
            return 59;
        }
    }

    /**
     * A minimal, stateless mock for a precise seconds DurationField.
     */
    private static class MockSecondsDurationField extends BaseDurationField {
        protected MockSecondsDurationField() {
            super(DurationFieldType.seconds());
        }

        @Override
        public boolean isPrecise() {
            return true;
        }

        @Override
        public long getUnitMillis() {
            return 1000L; // 1 second
        }

        // Unused methods for this test
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
    
    /**
     * A minimal, stateless mock for a precise minutes DurationField.
     */
    private static class MockMinutesDurationField extends BaseDurationField {
        protected MockMinutesDurationField() {
            super(DurationFieldType.minutes());
        }

        @Override
        public boolean isPrecise() { return true; }
        @Override
        public long getUnitMillis() { return 60000L; } // 1 minute
        
        // Unused methods for this test
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
    // Test Cases
    //-----------------------------------------------------------------------

    @Test
    public void getMinimumValue_withPartial_returnsDefaultMinimumValue() {
        // This test verifies that getMinimumValue(ReadablePartial, int[]) delegates
        // to getMinimumValue(), which returns 0 by default from BaseDateTimeField.

        // Arrange
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        int expectedMinimumValue = 0;

        // Dummy arguments required by the method signature, but their values are ignored.
        TimeOfDay dummyPartial = new TimeOfDay();
        int[] dummyValues = new int[4];

        // Act
        int actualMinimumValue = field.getMinimumValue(dummyPartial, dummyValues);

        // Assert
        assertEquals(expectedMinimumValue, actualMinimumValue);
    }
}