package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

/**
 * Test case for the PreciseDurationDateTimeField class, focusing on the
 * getAsText(int, Locale) method inherited from BaseDateTimeField.
 */
public class PreciseDurationDateTimeFieldTest {

    /**
     * A minimal concrete implementation of the abstract PreciseDurationDateTimeField.
     * <p>
     * This class is necessary to create an instance to test the concrete method
     * {@code getAsText(int, Locale)}, which is inherited from {@link BaseDateTimeField}.
     * The implemented abstract methods are not relevant to this specific test.
     */
    private static class TestPreciseDurationDateTimeField extends PreciseDurationDateTimeField {

        protected TestPreciseDurationDateTimeField() {
            // Provide a standard, precise duration field to satisfy the super constructor.
            super(DateTimeFieldType.secondOfMinute(), ISOChronology.getInstanceUTC().seconds());
        }

        // The following methods must be implemented but are not called by the test.
        @Override
        public int get(long instant) {
            return 0;
        }

        @Override
        public DurationField getRangeDurationField() {
            return null;
        }

        @Override
        public int getMaximumValue() {
            // This value is intentionally less than the value used in the test (80)
            // to demonstrate that getAsText does not perform validation.
            return 59;
        }
    }

    @Test
    public void getAsText_shouldConvertValueToStringAndIgnoreLocale() {
        // Arrange
        // The getAsText(int, Locale) method is inherited from BaseDateTimeField.
        // This test verifies that it simply converts the integer to its string
        // representation, ignoring the provided Locale.
        BaseDateTimeField field = new TestPreciseDurationDateTimeField();
        int value = 80;

        // The test uses a value (80) that is higher than the field's declared
        // maximum value (59) to confirm that getAsText does not validate the input.

        // Act
        String textWithEnglishLocale = field.getAsText(value, Locale.ENGLISH);
        String textWithNullLocale = field.getAsText(value, null);

        // Assert
        String expectedText = "80";
        assertEquals("getAsText should return the string representation of the value.",
                expectedText, textWithEnglishLocale);
        assertEquals("getAsText should ignore the locale.",
                expectedText, textWithNullLocale);
    }
}