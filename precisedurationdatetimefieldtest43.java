package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link PreciseDurationDateTimeField}.
 * <p>
 * This test focuses on the {@code getMaximumShortTextLength} method, which is inherited
 * from the {@link BaseDateTimeField} superclass.
 */
public class PreciseDurationDateTimeFieldTest {

    @Test
    public void getMaximumShortTextLength_shouldReturnStringLengthOfMaximumValue() {
        // Arrange
        // The getMaximumShortTextLength() method's behavior depends on the field's maximum value.
        // We create a mock field that returns a maximum value of 59.
        BaseDateTimeField fieldWithMaxValue59 = new DateTimeFieldWithFixedMaximumValue();
        
        // The expected length is the number of digits in the maximum value, "59".
        int expectedLength = 2;

        // Act
        int actualLength = fieldWithMaxValue59.getMaximumShortTextLength(Locale.ENGLISH);

        // Assert
        assertEquals("The maximum short text length should be the number of digits in the maximum value (59).",
                expectedLength, actualLength);
    }

    //-----------------------------------------------------------------------
    // Mock implementations for testing purposes
    //-----------------------------------------------------------------------

    /**
     * A mock PreciseDurationDateTimeField that provides a fixed maximum value.
     * This is used to test methods that depend on getMaximumValue().
     */
    private static class DateTimeFieldWithFixedMaximumValue extends PreciseDurationDateTimeField {

        DateTimeFieldWithFixedMaximumValue() {
            // The super constructor requires a non-null, precise DurationField.
            super(DateTimeFieldType.secondOfMinute(), new StubPreciseDurationField());
        }

        /**
         * Returns a fixed maximum value of 59 for this field.
         */
        @Override
        public int getMaximumValue() {
            return 59;
        }

        // The following methods must be implemented as they are abstract in a superclass,
        // but they are not relevant for this specific test.
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
     * A stub implementation of a precise DurationField.
     * Its only purpose is to satisfy the constructor of PreciseDurationDateTimeField,
     * which requires a precise duration field with a unit size of at least 1 millisecond.
     */
    private static class StubPreciseDurationField extends BaseDurationField {

        StubPreciseDurationField() {
            super(DurationFieldType.seconds());
        }

        @Override
        public boolean isPrecise() {
            return true;
        }

        @Override
        public long getUnitMillis() {
            // Any value >= 1 is valid for the PreciseDurationDateTimeField constructor.
            return 1L;
        }

        // The following methods are inherited and not used in this test.
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
}