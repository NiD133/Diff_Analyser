package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.ReadablePartial;
import org.joda.time.TimeOfDay;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the PreciseDurationDateTimeField class.
 * This test focuses on the behavior of methods inherited from BaseDateTimeField.
 */
public class PreciseDurationDateTimeFieldTest {

    /**
     * Tests that getMaximumValue(ReadablePartial) delegates to getMaximumValue().
     * <p>
     * The implementation of getMaximumValue(ReadablePartial) in the superclass (BaseDateTimeField)
     * simply calls the no-argument getMaximumValue(). This test creates a mock field with a known
     * maximum value and confirms that this value is returned, regardless of the partial instance provided.
     */
    @Test
    public void getMaximumValue_withReadablePartial_returnsFieldMaximumValue() {
        // Arrange
        // Create a concrete implementation of the abstract class under test.
        BaseDateTimeField field = new MockFieldWithKnownMaximumValue();

        // The specific ReadablePartial instance should be ignored by the method.
        ReadablePartial dummyPartial = new TimeOfDay();

        // Act
        int actualMaximum = field.getMaximumValue(dummyPartial);

        // Assert
        int expectedMaximum = 59; // This is the hardcoded maximum value in our mock.
        assertEquals(expectedMaximum, actualMaximum);
    }

    /**
     * A mock implementation of PreciseDurationDateTimeField for testing purposes.
     * It simulates a "second of minute" field with a fixed maximum value of 59.
     */
    private static class MockFieldWithKnownMaximumValue extends PreciseDurationDateTimeField {

        /**
         * Constructs the mock field.
         * Uses a real, precise duration field from ISOChronology to satisfy the superclass constructor.
         */
        MockFieldWithKnownMaximumValue() {
            super(DateTimeFieldType.secondOfMinute(), ISOChronology.getInstanceUTC().seconds());
        }

        /**
         * This is the key method for the test, providing a known maximum value.
         */
        @Override
        public int getMaximumValue() {
            return 59;
        }

        // --- Dummy implementations for abstract methods not used in this test ---

        @Override
        public int get(long instant) {
            // Not relevant for this test.
            return 0;
        }

        @Override
        public DurationField getRangeDurationField() {
            // Return a sensible, non-null value.
            return ISOChronology.getInstanceUTC().minutes();
        }
    }
}