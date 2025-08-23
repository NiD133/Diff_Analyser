package org.joda.time.field;

import static org.junit.Assert.assertNull;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;

/**
 * Unit tests for {@link PreciseDurationDateTimeField}.
 *
 * <p>This test class verifies the behavior of methods on the abstract PreciseDurationDateTimeField,
 * particularly those inherited from its parent classes.
 */
public class PreciseDurationDateTimeFieldTest {

    //================================================================================
    // Test Helper Classes
    //================================================================================

    /**
     * A minimal, concrete implementation of the abstract {@link PreciseDurationDateTimeField}
     * for testing purposes. It provides dummy implementations for the abstract methods
     * required to instantiate the class.
     */
    private static class TestablePreciseDurationDateTimeField extends PreciseDurationDateTimeField {

        /**
         * Creates an instance with a valid, stubbed precise duration field.
         */
        TestablePreciseDurationDateTimeField() {
            super(DateTimeFieldType.secondOfMinute(), new StubbedPreciseDurationField());
        }

        // Provide minimal implementations for abstract methods. Their specific
        // logic is not relevant for the test cases in this class.
        @Override
        public int get(long instant) {
            return 0;
        }

        @Override
        public long set(long instant, int value) {
            return 0;
        }

        @Override
        public DurationField getRangeDurationField() {
            return null;
        }

        @Override
        public int getMinimumValue() {
            return 0;
        }

        @Override
        public int getMaximumValue() {
            return 59;
        }
    }

    /**
     * A stub {@link DurationField} that is "precise" and has a positive unit length,
     * as required by the {@link PreciseDurationDateTimeField} constructor.
     */
    private static class StubbedPreciseDurationField extends BaseDurationField {

        StubbedPreciseDurationField() {
            super(DurationFieldType.seconds());
        }

        @Override
        public boolean isPrecise() {
            return true;
        }

        @Override
        public long getUnitMillis() {
            // A non-zero value is required by the SUT's constructor.
            return 60L;
        }

        // The following methods are not used in this test and can return dummy values.
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
    }

    //================================================================================
    // Test Cases
    //================================================================================

    @Test
    public void getLeapDurationField_shouldReturnNull() {
        // Arrange
        // The method under test is inherited from a parent class. This test verifies
        // that for a precise field, there is no concept of a leap duration.
        BaseDateTimeField field = new TestablePreciseDurationDateTimeField();

        // Act
        DurationField leapField = field.getLeapDurationField();

        // Assert
        assertNull("A precise duration field should not have a leap duration field.", leapField);
    }
}