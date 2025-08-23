package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.TimeOfDay;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link PreciseDurationDateTimeField}, focusing on text generation methods.
 */
public class PreciseDurationDateTimeFieldGetAsTextTest {

    // --- Test Helper Classes ---

    /**
     * A mock implementation of PreciseDurationDateTimeField for testing purposes.
     * This mock is designed to represent the 'secondOfMinute' field to test
     * behavior inherited from the {@link BaseDateTimeField} superclass.
     */
    private static class MockSecondOfMinuteField extends PreciseDurationDateTimeField {

        MockSecondOfMinuteField() {
            // The constructor requires a precise duration field.
            super(DateTimeFieldType.secondOfMinute(), new MockPreciseDurationField(DurationFieldType.seconds()));
        }

        /**
         * This method is required to make the class concrete, but it is NOT CALLED
         * by the test case below. The superclass `BaseDateTimeField`'s implementation of
         * `getAsShortText(ReadablePartial, ...)` directly retrieves the value from the
         * ReadablePartial object, bypassing this method.
         */
        @Override
        public int get(long instant) {
            throw new UnsupportedOperationException("get(long) should not be called in this test.");
        }

        /**
         * Required for compilation. Returns a mock minutes field. Not used in this test.
         */
        @Override
        public DurationField getRangeDurationField() {
            return new MockPreciseDurationField(DurationFieldType.minutes());
        }

        /**
         * Required for compilation. Not used in this test.
         */
        @Override
        public int getMaximumValue() {
            return 59;
        }

        // Note: Other abstract methods like set() and isLenient() are assumed to be
        // implemented in a superclass in the version of Joda-Time this test was written for.
    }

    /**
     * A minimal, precise mock DurationField.
     * Required by the PreciseDurationDateTimeField constructor. This mock is a simplified
     * and clarified version of the original MockCountingDurationField.
     */
    private static class MockPreciseDurationField extends BaseDurationField {

        protected MockPreciseDurationField(DurationFieldType type) {
            super(type);
        }

        @Override
        public boolean isPrecise() {
            // Must be precise for the SUT's constructor to succeed.
            return true;
        }

        @Override
        public long getUnitMillis() {
            // Must return a value > 0. The original test used 60, which was arbitrary.
            // Using 1 is equally valid and simpler.
            return 1;
        }

        // The following methods are not used by the test and can be minimal.
        // The original mock had complex implementations and static counters which were unused.
        @Override public long add(long instant, int value) { return 0; }
        @Override public long add(long instant, long value) { return 0; }
        @Override public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) { return 0; }
        @Override public long getValueAsLong(long duration, long instant) { return 0; }
        @Override public long getMillis(int value, long instant) { return 0; }
        @Override public long getMillis(long value, long instant) { return 0; }
    }

    @Test
    public void getAsShortText_withReadablePartial_returnsFieldValueAsString() {
        // Arrange
        // The field under test is a mock 'secondOfMinute' field. The test verifies
        // the behavior of `getAsShortText`, which is implemented in the superclass
        // `BaseDateTimeField`.
        BaseDateTimeField secondOfMinuteField = new MockSecondOfMinuteField();

        // A TimeOfDay object representing 12:30:40.050.
        // We expect the 'secondOfMinute' field to extract the value 40.
        TimeOfDay time = new TimeOfDay(12, 30, 40, 50);
        String expectedText = "40";

        // Act & Assert
        // The method should extract the field's value from the TimeOfDay partial
        // and return it as a string. The behavior is not locale-dependent for numbers,
        // so testing with a specific locale and a null locale covers the same logic.
        assertEquals("Text with English locale should match", expectedText, secondOfMinuteField.getAsShortText(time, Locale.ENGLISH));
        assertEquals("Text with null locale should match", expectedText, secondOfMinuteField.getAsShortText(time, null));
    }
}