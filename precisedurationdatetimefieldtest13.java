package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

/**
 * Tests the text representation methods of {@link PreciseDurationDateTimeField}.
 */
public class PreciseDurationDateTimeFieldTest {

    /**
     * A minimal mock implementation of the abstract SUT (PreciseDurationDateTimeField).
     * The key behavior for this test is the get(long) method, which is defined
     * to return the instant divided by the unit millis.
     */
    private static class MockDateTimeField extends PreciseDurationDateTimeField {

        // The original test used a unit of 60 milliseconds. We preserve that logic.
        static final long MOCK_UNIT_MILLIS = 60L;

        MockDateTimeField() {
            super(DateTimeFieldType.secondOfMinute(),
                  new PreciseDurationField(DurationFieldType.seconds(), MOCK_UNIT_MILLIS));
        }

        /**
         * Returns the field value, which is calculated directly from the instant.
         * The parent class's getAsShortText() method relies on this implementation.
         */
        @Override
        public int get(long instant) {
            return (int) (instant / MOCK_UNIT_MILLIS);
        }

        // --- Stubbed methods required by the abstract class, not relevant to this test ---

        @Override
        public long set(long instant, int value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public DurationField getRangeDurationField() {
            return null; // Not needed for this test
        }

        @Override
        public int getMaximumValue() {
            return 59; // An arbitrary but valid maximum value
        }

        @Override
        public boolean isLenient() {
            return false;
        }
    }

    @Test
    public void getAsShortText_shouldReturnStringRepresentationOfFieldValue() {
        // Arrange
        final int fieldValue = 29;
        final String expectedText = "29";

        // Create a field where get(instant) is defined as (instant / 60ms).
        BaseDateTimeField field = new MockDateTimeField();

        // Calculate an instant that will produce the desired field value based on the mock's logic.
        long instant = fieldValue * MockDateTimeField.MOCK_UNIT_MILLIS;

        // Act
        String actualTextWithLocale = field.getAsShortText(instant, Locale.ENGLISH);
        String actualTextWithNullLocale = field.getAsShortText(instant, null);

        // Assert
        // The getAsShortText method for a numerical field should simply convert the
        // integer value returned by get(instant) to a String.
        assertEquals(
            "The short text should be the string representation of the field's value.",
            expectedText,
            actualTextWithLocale
        );

        // The result should be independent of the Locale for a simple numerical field.
        assertEquals(
            "The short text should not be affected by a null locale.",
            expectedText,
            actualTextWithNullLocale
        );
    }
}