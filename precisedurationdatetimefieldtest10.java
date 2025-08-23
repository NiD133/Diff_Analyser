package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.ReadablePartial;
import org.joda.time.TimeOfDay;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link PreciseDurationDateTimeField}.
 * This class focuses on testing inherited behavior.
 */
public class PreciseDurationDateTimeFieldTest {

    /**
     * A concrete, testable implementation of the abstract PreciseDurationDateTimeField.
     * Its purpose is to provide an object on which to call methods for testing.
     */
    private static class TestablePreciseDurationDateTimeField extends PreciseDurationDateTimeField {

        /**
         * Constructor that provides a valid, precise unit duration field, as required
         * by the superclass constructor.
         */
        TestablePreciseDurationDateTimeField() {
            super(DateTimeFieldType.secondOfMinute(), new MockPreciseUnitDurationField());
        }

        // The following abstract methods must be implemented, but their logic is
        // not relevant to the test case in this file.
        @Override
        public int get(long instant) {
            return 0; // Dummy implementation
        }

        @Override
        public DurationField getRangeDurationField() {
            return null; // Dummy implementation
        }

        @Override
        public int getMaximumValue() {
            return 59; // Dummy implementation
        }
    }

    /**
     * A minimal, mock duration field that is "precise" and has a "unit millisecond"
     * value greater than zero, satisfying the constructor requirements of
     * {@link PreciseDurationDateTimeField}.
     */
    private static class MockPreciseUnitDurationField extends BaseDurationField {

        MockPreciseUnitDurationField() {
            super(DurationFieldType.seconds());
        }

        @Override
        public boolean isPrecise() {
            return true;
        }

        @Override
        public long getUnitMillis() {
            // A non-zero value is required by the PreciseDurationDateTimeField constructor.
            return 1L;
        }

        // The following abstract methods must be implemented, but their logic is
        // not relevant to the test case in this file.
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
    public void getAsText_withPartialAndLocale_shouldReturnStringRepresentationOfValue() {
        // This test verifies the default behavior of getAsText(ReadablePartial, int, Locale)
        // inherited from the superclass BaseDateTimeField. This method is expected to
        // ignore the ReadablePartial and Locale arguments and simply return the
        // string representation of the integer value.

        // Arrange
        BaseDateTimeField field = new TestablePreciseDurationDateTimeField();
        final int fieldValue = 20;
        final String expectedText = "20";
        // A ReadablePartial is required, but its specific value is irrelevant for this test.
        final ReadablePartial irrelevantPartial = new TimeOfDay(12, 30, 40, 50);

        // Act & Assert for a non-null locale
        String actualTextWithLocale = field.getAsText(irrelevantPartial, fieldValue, Locale.ENGLISH);
        assertEquals(expectedText, actualTextWithLocale);

        // Act & Assert for a null locale
        String actualTextWithNullLocale = field.getAsText(irrelevantPartial, fieldValue, null);
        assertEquals(expectedText, actualTextWithNullLocale);
    }
}