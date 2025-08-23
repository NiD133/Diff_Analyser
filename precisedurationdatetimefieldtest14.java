package org.joda.time.field;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;

/**
 * Tests for {@link PreciseDurationDateTimeField}.
 */
public class PreciseDurationDateTimeFieldTest {

    /**
     * A mock PreciseDurationDateTimeField for testing.
     * Its `get(long)` method is implemented to simply divide the instant by the unit millis,
     * simulating a field like 'second of minute'.
     */
    private static class TestablePreciseDurationDateTimeField extends PreciseDurationDateTimeField {

        protected TestablePreciseDurationDateTimeField() {
            super(DateTimeFieldType.secondOfMinute(), new MockSixtyMillisDurationField());
        }

        @Override
        public int get(long instant) {
            return (int) (instant / getUnitMillis());
        }

        @Override
        public DurationField getRangeDurationField() {
            // This method must be implemented, but is not relevant for this test.
            // We can return a simple mock or null if the contract allows.
            // Here, we return a mock duration field for minutes.
            return new MockPreciseDurationField(DurationFieldType.minutes(), 60 * 60L);
        }

        @Override
        public int getMaximumValue() {
            // This must be implemented, but is not relevant for this test.
            return 59;
        }
    }

    /**
     * A mock DurationField that is precise and has a unit of 60 milliseconds.
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
        
        // The following methods are not used in this test and are stubbed to return 0.
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
    public void getAsShortTextReturnsFieldValueAsString() {
        // The getAsShortText(long) method is inherited from BaseDateTimeField.
        // This test verifies that it correctly returns the string representation 
        // of the value computed by the get(long) method.

        // Arrange
        // The mock field calculates its value by dividing the instant by its unit millis (60).
        final int fieldValue = 29;
        final long unitMillis = 60L;
        final long testInstant = fieldValue * unitMillis;
        final String expectedText = "29";

        BaseDateTimeField field = new TestablePreciseDurationDateTimeField();

        // Act
        String actualText = field.getAsShortText(testInstant);

        // Assert
        assertEquals(expectedText, actualText);
    }
}