package org.joda.time.field;

import static org.junit.Assert.assertEquals;

import java.util.Locale;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.ReadablePartial;
import org.joda.time.TimeOfDay;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;

/**
 * Unit tests for the getAsText(ReadablePartial, Locale) method in PreciseDurationDateTimeField.
 */
public class PreciseDurationDateTimeFieldGetAsTextTest {

    /**
     * A minimal, concrete implementation of PreciseDurationDateTimeField for testing.
     * The test for getAsText(ReadablePartial, ...) only depends on the field's type,
     * not its calculation logic, so most methods can be stubbed out.
     */
    private static class StubPreciseSecondOfMinuteField extends PreciseDurationDateTimeField {

        StubPreciseSecondOfMinuteField() {
            // Use a standard, precise duration field for simplicity and clarity.
            super(DateTimeFieldType.secondOfMinute(), ISOChronology.getInstanceUTC().seconds());
        }

        // The following methods are abstract in parent classes and must be implemented.
        // They are not called by this test, so they throw an exception to ensure
        // they are not used unexpectedly.

        @Override
        public int get(long instant) {
            throw new UnsupportedOperationException();
        }

        @Override
        public long set(long instant, int value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public DurationField getRangeDurationField() {
            // A real implementation would return minutes(), but it's not needed for this test.
            return ISOChronology.getInstanceUTC().minutes();
        }

        @Override
        public int getMinimumValue() {
            return 0;
        }

        @Override
        public int getMaximumValue() {
            return 59;
        }

        @Override
        public boolean isLenient() {
            return false;
        }

        @Override
        public long roundFloor(long instant) {
            throw new UnsupportedOperationException();
        }

        @Override
        public long roundCeiling(long instant) {
            throw new UnsupportedOperationException();
        }

        @Override
        public long remainder(long instant) {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Tests that getAsText correctly extracts the field's value from a ReadablePartial
     * and returns it as a string, regardless of the specified Locale.
     */
    @Test
    public void getAsText_withReadablePartial_shouldReturnFieldValueAsString() {
        // Arrange
        BaseDateTimeField secondOfMinuteField = new StubPreciseSecondOfMinuteField();
        int secondValue = 40;
        ReadablePartial time = new TimeOfDay(12, 30, secondValue, 50);
        String expectedText = "40";

        // Act
        String actualTextWithLocale = secondOfMinuteField.getAsText(time, Locale.ENGLISH);
        String actualTextWithNullLocale = secondOfMinuteField.getAsText(time, null);

        // Assert
        // The method should simply convert the integer value to a string, ignoring the locale.
        assertEquals("Text with English locale should match", expectedText, actualTextWithLocale);
        assertEquals("Text with null locale should match", expectedText, actualTextWithNullLocale);
    }
}