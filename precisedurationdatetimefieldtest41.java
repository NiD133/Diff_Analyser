package org.joda.time.field;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.ReadablePartial;
import org.joda.time.TimeOfDay;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;

/**
 * Tests for the PreciseDurationDateTimeField class, focusing on method delegation.
 */
public class PreciseDurationDateTimeFieldTest {

    /**
     * A minimal, concrete implementation of the abstract PreciseDurationDateTimeField.
     * This mock is designed to simulate a "second of minute" field, which has a
     * fixed maximum value of 59.
     */
    private static class TestDateTimeField extends PreciseDurationDateTimeField {

        private static final int MAX_VALUE = 59;

        TestDateTimeField() {
            // The superclass requires a precise duration field. We use a real one
            // to avoid the complexity of a mock duration field.
            super(DateTimeFieldType.secondOfMinute(), ISOChronology.getInstanceUTC().seconds());
        }

        // This is the key method whose behavior is being verified through delegation.
        @Override
        public int getMaximumValue() {
            return MAX_VALUE;
        }

        // The methods below are stubbed to satisfy the abstract class requirements,
        // but they are not relevant to this specific test's logic.
        @Override
        public int get(long instant) {
            return 0;
        }

        @Override
        public DurationField getRangeDurationField() {
            return ISOChronology.getInstanceUTC().minutes();
        }
    }

    @Test
    public void getMaximumValue_withPartial_delegatesToParameterlessVersion() {
        // Arrange
        // The method getMaximumValue(ReadablePartial, int[]) is defined in the superclass
        // BaseDateTimeField. This test verifies that for a precise field, it delegates
        // its call to the simpler, parameterless getMaximumValue() method.
        BaseDateTimeField secondOfMinuteField = new TestDateTimeField();

        // The partial and values arguments are not used in the delegation path for
        // precise fields, so their specific values do not matter.
        ReadablePartial dummyPartial = new TimeOfDay();
        int[] dummyValues = new int[4];

        int expectedMaximumValue = 59;

        // Act
        int actualMaximumValue = secondOfMinuteField.getMaximumValue(dummyPartial, dummyValues);

        // Assert
        assertEquals("The maximum value should be delegated from the parameterless method",
                expectedMaximumValue, actualMaximumValue);
    }
}