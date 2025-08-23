package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test case for the abstract PreciseDurationDateTimeField class.
 * <p>
 * Since the class under test is abstract, a concrete mock implementation is
 * required to test its functionality. This test suite uses
 * {@link MockSecondOfMinuteDateTimeField} for this purpose.
 */
public class PreciseDurationDateTimeFieldTest {

    //-----------------------------------------------------------------------
    // Mock implementations for testing the abstract PreciseDurationDateTimeField
    //-----------------------------------------------------------------------

    /**
     * A concrete implementation of PreciseDurationDateTimeField for testing.
     * It simulates a "second of minute" field.
     */
    private static class MockSecondOfMinuteDateTimeField extends PreciseDurationDateTimeField {

        /**
         * Constructs a mock field of type "second of minute".
         */
        MockSecondOfMinuteDateTimeField() {
            // This mock uses a duration field where one "second" unit is 60 milliseconds.
            super(DateTimeFieldType.secondOfMinute(), new MockSixtyMillisDurationField(DurationFieldType.seconds()));
        }

        /**
         * Mock implementation: returns the instant divided by the unit millis (60).
         */
        @Override
        public int get(long instant) {
            return (int) (instant / getUnitMillis());
        }

        /**
         * Mock implementation: returns a duration field representing "minutes".
         */
        @Override
        public DurationField getRangeDurationField() {
            return new MockSixtyMillisDurationField(DurationFieldType.minutes());
        }

        /**
         * Mock implementation: hardcoded to 59, the typical max for a second-of-minute field.
         * This is the value verified by the test.
         */
        @Override
        public int getMaximumValue() {
            return 59;
        }
    }

    /**
     * A mock DurationField with a fixed, precise unit of 60 milliseconds.
     */
    private static class MockSixtyMillisDurationField extends BaseDurationField {

        protected MockSixtyMillisDurationField(DurationFieldType type) {
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

        // The following methods must be implemented but are not central to this test.
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
            return (minuendInstant - subtrahendInstant) / 60L;
        }

        @Override
        public long getValueAsLong(long duration, long instant) {
            return 0; // Not used in this test
        }

        @Override
        public long getMillis(int value, long instant) {
            return value * 60L;
        }

        @Override
        public long getMillis(long value, long instant) {
            return value * 60L;
        }
    }

    //-----------------------------------------------------------------------
    // Test cases
    //-----------------------------------------------------------------------

    /**
     * Tests that getMaximumValue() returns the value defined in the concrete mock implementation.
     * <p>
     * The method {@code getMaximumValue()} is abstract in the SUT's hierarchy and not
     * implemented by {@code PreciseDurationDateTimeField} itself. Therefore, this test
     * validates the behavior of our specific mock implementation. This ensures the test
     * harness is correct before being used in more complex tests.
     */
    @Test
    public void getMaximumValue_returnsValueFromMockImplementation() {
        // Arrange: Create an instance of our concrete mock implementation.
        // The mock is designed to return 59 for its maximum value.
        BaseDateTimeField field = new MockSecondOfMinuteDateTimeField();
        int expectedMaximumValue = 59;

        // Act: Call the method under test.
        int actualMaximumValue = field.getMaximumValue();

        // Assert: Verify that the returned value matches the mock's definition.
        assertEquals(expectedMaximumValue, actualMaximumValue);
    }
}