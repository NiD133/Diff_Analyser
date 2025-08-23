package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the abstract PreciseDurationDateTimeField class.
 * <p>
 * This test suite uses a concrete mock implementation to verify the behavior
 * of the base class.
 */
public class PreciseDurationDateTimeFieldTest {

    /**
     * This test verifies that the getAsText() method correctly formats the field's
     * integer value as a String. The method is inherited from BaseDateTimeField,
     * and its behavior depends on the get() method.
     */
    @Test
    public void getAsTextShouldReturnFieldValueAsString() {
        // Arrange
        // The BaseDateTimeField#getAsText(long) method delegates to get(long) and then
        // converts the result to a String. We use a mock field to test this behavior.
        MockDateTimeField field = new MockDateTimeField();

        // Define a value for the field and calculate the corresponding instant.
        // The mock's get(instant) is defined as (instant / 60), so the instant
        // must be the desired value multiplied by the unit size (60).
        final int fieldValue = 29;
        final long unitMillis = field.getDurationField().getUnitMillis();
        final long instant = fieldValue * unitMillis;
        final String expectedText = "29";

        // Act
        String actualText = field.getAsText(instant);

        // Assert
        assertEquals(expectedText, actualText);
    }

    // -----------------------------------------------------------------------
    // Mock implementations used for testing the abstract class.
    // -----------------------------------------------------------------------

    /**
     * A concrete implementation of the abstract PreciseDurationDateTimeField for testing.
     * <p>
     * It simulates a field like "second of minute", where the unit of duration
     * is a fixed 60 milliseconds.
     */
    private static class MockDateTimeField extends PreciseDurationDateTimeField {

        MockDateTimeField() {
            super(DateTimeFieldType.secondOfMinute(), new MockSixtyMillisDurationField());
        }

        /**
         * For this mock, the value is simply the instant divided by the unit size (60).
         */
        @Override
        public int get(long instant) {
            return (int) (instant / getUnitMillis());
        }

        @Override
        public DurationField getRangeDurationField() {
            // The range is not relevant for this test, but the method must be implemented.
            return new MockSixtyMillisDurationField(DurationFieldType.minutes());
        }

        @Override
        public int getMaximumValue() {
            return 59; // A typical maximum for a "second of minute" field.
        }

        // -------------------------------------------------------------------
        // Abstract methods that must be implemented but are not used in this test.
        // Their implementations are minimal to make this mock concrete.
        // -------------------------------------------------------------------

        @Override
        public boolean isLenient() {
            return false;
        }

        @Override
        public long set(long instant, int value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public DurationField getDurationField() {
            // For this simple mock, the duration field is the same as the unit field.
            return iUnitField;
        }

        @Override
        public int getMinimumValue() {
            return 0;
        }
    }

    /**
     * A mock DurationField with a fixed, precise unit of 60 milliseconds.
     */
    private static class MockSixtyMillisDurationField extends BaseDurationField {

        MockSixtyMillisDurationField() {
            this(DurationFieldType.seconds());
        }

        MockSixtyMillisDurationField(DurationFieldType type) {
            super(type);
        }

        @Override
        public boolean isPrecise() {
            return true;
        }

        @Override
        public long getUnitMillis() {
            return 60L;
        }

        // -------------------------------------------------------------------
        // Abstract methods that must be implemented but are not used in this test.
        // -------------------------------------------------------------------

        @Override
        public long getValueAsLong(long duration, long instant) {
            throw new UnsupportedOperationException();
        }

        @Override
        public long getMillis(int value, long instant) {
            throw new UnsupportedOperationException();
        }

        @Override
        public long getMillis(long value, long instant) {
            throw new UnsupportedOperationException();
        }

        @Override
        public long add(long instant, int value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public long add(long instant, long value) {
            throw new UnsupportedOperation-Exception();
        }

        @Override
        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            throw new UnsupportedOperationException();
        }
    }
}