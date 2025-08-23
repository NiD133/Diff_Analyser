package org.joda.time.field;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;

/**
 * Tests for the set() method in PreciseDurationDateTimeField, inherited from BaseDateTimeField.
 *
 * Note: The original class name was PreciseDurationDateTimeFieldTestTest28 and used JUnit 3.
 * It has been renamed to TestPreciseDurationDateTimeField (based on the original suite() method)
 * and updated to use modern JUnit 4 conventions for better readability.
 */
public class TestPreciseDurationDateTimeField {

    //-----------------------------------------------------------------------
    // Mock implementations for testing the abstract PreciseDurationDateTimeField
    //-----------------------------------------------------------------------

    /**
     * A mock DurationField with a fixed, precise unit of 60 milliseconds.
     */
    private static class MockSixtyMillisDurationField extends BaseDurationField {
        private static final long serialVersionUID = 1L;

        MockSixtyMillisDurationField() {
            super(DurationFieldType.millis());
        }

        @Override
        public boolean isPrecise() {
            return true;
        }

        @Override
        public long getUnitMillis() {
            return 60L;
        }

        // Other methods are not needed for this test and will throw
        // UnsupportedOperationException if called, which is the correct default behavior.
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
            throw new UnsupportedOperationException();
        }

        @Override
        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * A mock PreciseDurationDateTimeField that uses the MockSixtyMillisDurationField.
     * It represents a field with a maximum value of 59.
     */
    private static class MockField extends PreciseDurationDateTimeField {
        private static final long serialVersionUID = 1L;

        MockField() {
            super(DateTimeFieldType.millisOfSecond(), new MockSixtyMillisDurationField());
        }

        /**
         * The get() method is crucial for the set() logic in the superclass.
         * It calculates the field's value for a given instant.
         */
        @Override
        public int get(long instant) {
            // For a given instant, calculate how many 60ms units have passed.
            return (int) (instant / getUnitMillis());
        }

        @Override
        public DurationField getRangeDurationField() {
            // This field is not strictly needed for this test to pass, as getMaximumValue()
            // is overridden. We return a standard minutes field for completeness.
            return ISOChronology.getInstanceUTC().minutes();
        }

        @Override
        public int getMaximumValue() {
            return 59;
        }
    }

    //-----------------------------------------------------------------------
    // Test cases
    //-----------------------------------------------------------------------

    @Test
    public void testSet_withStringValue_updatesInstantCorrectly() {
        // Arrange
        BaseDateTimeField field = new MockField();
        final long unitMillis = field.getUnitMillis();
        assertEquals("Self-check: Mock unit millis should be 60", 60L, unitMillis);

        // The set(long, String) method is inherited from BaseDateTimeField.
        // Its core logic for a precise field is:
        // newInstant = instant - get(instant) * unitMillis + value * unitMillis
        // This formula effectively preserves the remainder within a unit (e.g., millis within a second)
        // while setting the field to the new value.

        // --- Test Case 1: Set value on an instant that is a multiple of the unit (0) ---
        long instantAtUnitStart = 0L;
        // For this instant: get(0L) returns 0.
        // set(0L, "29") = 0 - (0 * 60) + (29 * 60) = 1740.
        long expectedInstant1 = 29 * unitMillis;
        assertEquals(expectedInstant1, field.set(instantAtUnitStart, "29"));

        // --- Test Case 2: Set value on an instant that is NOT a multiple of the unit ---
        long instantInMiddleOfUnit = 123L; // Represents 2 units of 60ms, with a remainder of 3ms.
        // For this instant: get(123L) returns 2.
        // set(123L, "29") = 123 - (2 * 60) + (29 * 60) = 123 - 120 + 1740 = 3 + 1740 = 1743.
        long remainder = instantInMiddleOfUnit % unitMillis;
        long expectedInstant2 = remainder + (29 * unitMillis);
        assertEquals(expectedInstant2, field.set(instantInMiddleOfUnit, "29"));

        // --- Test Case 3: Setting the value to "0" should preserve the remainder ---
        assertEquals(0L, field.set(instantAtUnitStart, "0"));
        assertEquals(remainder, field.set(instantInMiddleOfUnit, "0"));
    }
}